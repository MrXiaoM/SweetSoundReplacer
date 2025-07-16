package top.mrxiaom.sweet.soundreplacer.nms;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class NMS {
    private static final Map<String, String> VERSION_TO_REVISION = new HashMap<String, String>() {{
        put("1.20", "v1_20_R1");
        put("1.20.1", "v1_20_R1");
        put("1.20.2", "v1_20_R2");
        put("1.20.3", "v1_20_R3");
        put("1.20.4", "v1_20_R3");
        put("1.20.5", "v1_20_R4");
        put("1.20.6", "v1_20_R4");
        put("1.21", "v1_21_R1");
        put("1.21.1", "v1_21_R1");
        put("1.21.2", "v1_21_R2");
        put("1.21.3", "v1_21_R2");
        put("1.21.4", "v1_21_R3");
        put("1.21.5", "v1_21_R4");
        put("1.21.6", "v1_21_R5");
        put("1.21.7", "v1_21_R5");
    }};

    private static PacketAPI packetAPI;
    private NMS() {}

    public static void init(Logger logger) {
        String version;
        try {
            String pkg = Bukkit.getServer().getClass().getPackage().getName();
            String ver = pkg.split("\\.")[3];
            logger.info("Found Minecraft: " + ver + "! Trying to find NMS support");
            version = ver;
        } catch (Throwable e) {
            String bukkit = Bukkit.getServer().getBukkitVersion();
            int index = bukkit.indexOf('-');
            String ver = index > 4 ? bukkit.substring(0, index) : bukkit;
            version = VERSION_TO_REVISION.getOrDefault(ver, "unknown");
            logger.info("Found Minecraft: " + ver + " (" + version + ")! Trying to find NMS support");
        }
        try {
            String packageName = NMS.class.getPackage().getName();
            packetAPI = (PacketAPI) Class.forName(packageName + ".Packet_" + version).getConstructor().newInstance();

            logger.info("NMS support '" + version + "' loaded!");
        } catch (Throwable t) {
            throw new IllegalStateException("插件不支持当前服务端版本 " + version);
        }
    }

    public static PacketAPI getPacketAPI() {
        return packetAPI;
    }
}
