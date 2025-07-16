package top.mrxiaom.sweet.soundreplacer.func;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.soundreplacer.SweetSoundReplacer;
import top.mrxiaom.sweet.soundreplacer.func.entry.SoundReplacement;
import top.mrxiaom.sweet.soundreplacer.nms.PacketAPI;

import java.util.HashMap;
import java.util.Map;

@AutoRegister(requirePlugins = "ProtocolLib")
public class PacketReplacer extends AbstractModule {
    private final Map<String, SoundReplacement> replacementMap = new HashMap<>();
    private final ProtocolManager protocolManager;
    private final PacketAPI packetAPI;

    public PacketReplacer(SweetSoundReplacer plugin) {
        super(plugin);
        packetAPI = plugin.getPacketAPI();
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
        PacketContainer newPacket = overridePacket(packet);
        if (newPacket != null) {
            event.setPacket(newPacket);
        }
    }

    private void onSendingNamedSoundEffect(PacketEvent event, PacketContainer packet) {
        if (packet.getMeta("modified").isPresent()) return;
        PacketContainer newPacket = overridePacket(packet);
        if (newPacket != null) {
            event.setPacket(newPacket);
        }
    }

    private PacketContainer overridePacket(PacketContainer packet) {
        NamespacedKey sound = plugin.getPacketAPI().getPacketSoundEffect(packet);
        if (sound == null) return null;
        SoundReplacement replacement = match(sound);
        if (replacement != null) {
            Object handle = packetAPI.overrideSound(packet, NamespacedKey.fromString(replacement.getTarget()));
            if (handle != null) {
                PacketContainer newPacket = new PacketContainer(packet.getType(), handle);
                newPacket.setMeta("modified", true);
                return newPacket;
            }
        }
        return null;
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
}
