package top.mrxiaom.sweet.soundreplacer;
        
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.utils.scheduler.FoliaLibScheduler;
import top.mrxiaom.sweet.soundreplacer.nms.NMS;
import top.mrxiaom.sweet.soundreplacer.nms.PacketAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import static top.mrxiaom.pluginbase.func.AbstractPluginHolder.reloadAllConfig;

public class SweetSoundReplacer extends BukkitPlugin {
    public static SweetSoundReplacer getInstance() {
        return (SweetSoundReplacer) BukkitPlugin.getInstance();
    }

    public SweetSoundReplacer() {
        super(options()
                .bungee(false)
                .adventure(false)
                .database(false)
                .reconnectDatabaseWhenReloadConfig(false)
                .scanIgnore("top.mrxiaom.sweet.soundreplacer.libs")
        );
        this.scheduler = new FoliaLibScheduler(this);
    }
    PacketAPI packetAPI;
    YamlConfiguration config;

    @Override
    protected void beforeLoad() {
        NMS.init(getLogger());
        packetAPI = NMS.getPacketAPI();
    }

    @NotNull
    @Override
    public YamlConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public PacketAPI getPacketAPI() {
        return packetAPI;
    }

    @Override
    public void reloadConfig() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveResource("config.yml", file);
        }
        config = new YamlConfiguration();
        config.options().pathSeparator('/');
        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        beforeReloadConfig(config);
        reloadAllConfig(config);
    }

    @Override
    protected void afterEnable() {
        getLogger().info("SweetSoundReplacer 加载完毕");
    }
}
