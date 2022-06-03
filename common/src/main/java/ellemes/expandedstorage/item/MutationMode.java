package ellemes.expandedstorage.item;

import java.util.Locale;

public enum MutationMode {
    MERGE,
    SPLIT,
    ROTATE,
    SWAP_THEME;

    private static final MutationMode[] VALUES = MutationMode.values();

    public static MutationMode from(byte index) {
        if (index >= 0 && index < MutationMode.VALUES.length) {
            return MutationMode.VALUES[index];
        }
        return null;
    }

    public byte toByte() {
        return (byte) this.ordinal();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public MutationMode next() {
        return MutationMode.VALUES[(ordinal() + 1) % MutationMode.VALUES.length];
    }
}
