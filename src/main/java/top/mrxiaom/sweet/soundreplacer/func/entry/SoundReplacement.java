package top.mrxiaom.sweet.soundreplacer.func.entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundReplacement {
    private final @NotNull String source;
    private final @NotNull String target;

    public SoundReplacement(@NotNull String source, @NotNull String target) {
        this.source = source;
        this.target = target;
    }

    public @NotNull String getSource() {
        return source;
    }

    public @NotNull String getTarget() {
        return target;
    }
}
