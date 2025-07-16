package top.mrxiaom.sweet.soundreplacer.nms;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

public interface PacketAPI {
    @Nullable NamespacedKey getPacketSoundEffect(PacketContainer packet);
    @Nullable Object overrideSound(PacketContainer packet, NamespacedKey sound);
}
