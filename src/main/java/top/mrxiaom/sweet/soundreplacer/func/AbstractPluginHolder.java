package top.mrxiaom.sweet.soundreplacer.func;
        
import top.mrxiaom.sweet.soundreplacer.SweetSoundReplacer;

@SuppressWarnings({"unused"})
public abstract class AbstractPluginHolder extends top.mrxiaom.pluginbase.func.AbstractPluginHolder<SweetSoundReplacer> {
    public AbstractPluginHolder(SweetSoundReplacer plugin) {
        super(plugin);
    }

    public AbstractPluginHolder(SweetSoundReplacer plugin, boolean register) {
        super(plugin, register);
    }
}
