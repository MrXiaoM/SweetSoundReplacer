package top.mrxiaom.sweet.soundreplacer.nms;

import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutEntitySound;
import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.jetbrains.annotations.Nullable;

public class Packet_v1_19_R3 implements PacketAPI {
    @Override
    public @Nullable NamespacedKey getPacketSoundEffect(PacketContainer packet) {
        Object handle = packet.getHandle();
        if (handle instanceof PacketPlayOutNamedSoundEffect p) {
            return CraftNamespacedKey.fromMinecraft(p.a().a().a());
        }
        if (handle instanceof PacketPlayOutEntitySound p) {
            return CraftNamespacedKey.fromMinecraft(p.a().a().a());
        }
        return null;
    }

    @Override
    public @Nullable Object overrideSound(PacketContainer packet, NamespacedKey sound) {
        Object handle = packet.getHandle();
        Holder<SoundEffect> holder = Holder.a(SoundEffect.a(CraftNamespacedKey.toMinecraft(sound)));
        if (handle instanceof PacketPlayOutNamedSoundEffect p) {
            return new PacketPlayOutNamedSoundEffect(holder, p.c(), p.d(), p.e(), p.f(), p.g(), p.h(), p.i());
        }
        if (handle instanceof PacketPlayOutEntitySound p) {
            return new PacketPlayOutEntitySound(holder, p.c(), new DummyEntity(p.d()), p.e(), p.f(), p.g());
        }
        return null;
    }

    public static class DummyEntity extends Entity {
        public DummyEntity(int id) {
            super(EntityTypes.aD, null);
            e(id);
        }
        @Override
        protected void a_() {
        }
        @Override
        protected void a(NBTTagCompound nbtTagCompound) {
        }
        @Override
        protected void b(NBTTagCompound nbtTagCompound) {
        }
    }
}
