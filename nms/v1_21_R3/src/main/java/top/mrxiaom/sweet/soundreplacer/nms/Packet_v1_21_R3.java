package top.mrxiaom.sweet.soundreplacer.nms;

import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutEntitySound;
import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftNamespacedKey;
import org.jetbrains.annotations.Nullable;

public class Packet_v1_21_R3 implements PacketAPI {
    @Override
    public @Nullable NamespacedKey getPacketSoundEffect(PacketContainer packet) {
        Object handle = packet.getHandle();
        if (handle instanceof PacketPlayOutNamedSoundEffect p) {
            return CraftNamespacedKey.fromMinecraft(p.b().a().a());
        }
        if (handle instanceof PacketPlayOutEntitySound p) {
            return CraftNamespacedKey.fromMinecraft(p.b().a().a());
        }
        return null;
    }

    @Override
    public @Nullable Object overrideSound(PacketContainer packet, NamespacedKey sound) {
        Object handle = packet.getHandle();
        Holder<SoundEffect> holder = Holder.a(SoundEffect.a(CraftNamespacedKey.toMinecraft(sound)));
        if (handle instanceof PacketPlayOutNamedSoundEffect p) {
            return new PacketPlayOutNamedSoundEffect(holder, p.e(), p.f(), p.g(), p.h(), p.i(), p.j(), p.k());
        }
        if (handle instanceof PacketPlayOutEntitySound p) {
            return new PacketPlayOutEntitySound(holder, p.e(), new DummyEntity(p.f()), p.g(), p.h(), p.i());
        }
        return null;
    }

    public static class DummyEntity extends Entity {
        public DummyEntity(int id) {
            super(EntityTypes.aD, null);
            e(id);
        }

        @Override
        protected void a(DataWatcher.a a) {
        }
        @Override
        public boolean a(WorldServer worldServer, DamageSource damageSource, float v) {
            return false;
        }
        @Override
        protected void a(NBTTagCompound nbtTagCompound) {
        }
        @Override
        protected void b(NBTTagCompound nbtTagCompound) {
        }
    }
}
