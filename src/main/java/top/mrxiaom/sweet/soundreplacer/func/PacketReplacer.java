package top.mrxiaom.sweet.soundreplacer.func;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutEntitySound;
import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftNamespacedKey;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.soundreplacer.SweetSoundReplacer;
import top.mrxiaom.sweet.soundreplacer.func.entry.SoundReplacement;

import java.util.HashMap;
import java.util.Map;

@AutoRegister(requirePlugins = "ProtocolLib")
public class PacketReplacer extends AbstractModule {
    private final Map<String, SoundReplacement> replacementMap = new HashMap<>();
    private final ProtocolManager protocolManager;
    public PacketReplacer(SweetSoundReplacer plugin) {
        super(plugin);
        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new Impl());
    }

    @Nullable
    private SoundReplacement match(NamespacedKey key) {
        SoundReplacement fullMatch = replacementMap.get(key.toString());
        if (fullMatch != null) {
            return fullMatch;
        }
        if (key.getNamespace().equals("minecraft")) {
            return replacementMap.get(key.getKey());
        }
        return null;
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        replacementMap.clear();
        ConfigurationSection section = config.getConfigurationSection("replace-sounds");
        if (section != null) for (String source : section.getKeys(false)) {
            ConfigurationSection s = section.getConfigurationSection(source);
            if (s == null) continue;
            String target = s.getString("replace-to");
            if (target == null) continue;
            replacementMap.put(source, new SoundReplacement(source, target));
        }
    }

    private void onSendingEntitySound(PacketEvent event, PacketContainer packet) {
        if (packet.getMeta("modified").isPresent()) return;
        PacketPlayOutEntitySound p = (PacketPlayOutEntitySound) packet.getHandle();
        SoundEffect soundEffect = p.a().a();
        NamespacedKey sound = CraftNamespacedKey.fromMinecraft(soundEffect.a());
        info("发送实体音效 " + sound);
        SoundReplacement replacement = match(sound);
        if (replacement != null) {
            Holder<SoundEffect> holder = Holder.a(SoundEffect.a(new MinecraftKey(replacement.getTarget())));
            PacketPlayOutEntitySound handle = new PacketPlayOutEntitySound(holder, p.d(), new DummyEntity(p.e()), p.f(), p.g(), p.h());
            PacketContainer newPacket = new PacketContainer(packet.getType(), handle);
            newPacket.setMeta("modified", true);
            info("修改为 " + holder.a().a());
            event.setPacket(newPacket);
        }
    }

    private void onSendingNamedSoundEffect(PacketEvent event, PacketContainer packet) {
        if (packet.getMeta("modified").isPresent()) return;
        PacketPlayOutNamedSoundEffect p = (PacketPlayOutNamedSoundEffect) packet.getHandle();
        SoundEffect soundEffect = p.a().a();
        NamespacedKey sound = CraftNamespacedKey.fromMinecraft(soundEffect.a());
        info("发送音效 " + sound);
        SoundReplacement replacement = match(sound);
        if (replacement != null) {
            Holder<SoundEffect> holder = Holder.a(SoundEffect.a(new MinecraftKey(replacement.getTarget())));
            PacketPlayOutNamedSoundEffect handle = new PacketPlayOutNamedSoundEffect(holder, p.d(), p.e(), p.f(), p.g(), p.h(), p.i(), p.j());
            PacketContainer newPacket = new PacketContainer(packet.getType(), handle);
            newPacket.setMeta("modified", true);
            info("修改为 " + holder.a().a());
            event.setPacket(newPacket);
        }
    }

    public class Impl extends PacketAdapter {
        public Impl() {
            super(new AdapterParameteters()
                    .plugin(PacketReplacer.this.plugin)
                    .serverSide()
                    .types(
                            PacketType.Play.Server.ENTITY_SOUND,
                            PacketType.Play.Server.NAMED_SOUND_EFFECT
                    ));
        }

        @Override
        public void onPacketSending(PacketEvent event) {
            PacketType type = event.getPacketType();
            if (PacketType.Play.Server.ENTITY_SOUND.equals(type)) {
                onSendingEntitySound(event, event.getPacket());
            }
            if (PacketType.Play.Server.NAMED_SOUND_EFFECT.equals(type)) {
                onSendingNamedSoundEffect(event, event.getPacket());
            }
        }
    }

    @Override
    public void onDisable() {
        protocolManager.removePacketListeners(plugin);
    }

    public static PacketReplacer inst() {
        return instanceOf(PacketReplacer.class);
    }

    public static class DummyEntity extends Entity {
        public DummyEntity(int id) {
            super(EntityTypes.aD, null);
            e(id);
        }

        @Override
        protected void c_() {
        }

        @Override
        protected void a(NBTTagCompound nbtTagCompound) {
        }

        @Override
        protected void b(NBTTagCompound nbtTagCompound) {
        }
    }
}
