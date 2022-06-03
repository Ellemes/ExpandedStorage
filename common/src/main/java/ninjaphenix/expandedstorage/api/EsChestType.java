package ninjaphenix.expandedstorage.api;

import net.minecraft.util.StringRepresentable;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;

public enum EsChestType implements StringRepresentable {
    TOP,
    BOTTOM,
    FRONT,
    BACK,
    LEFT,
    RIGHT,
    SINGLE;

    private final String name;

    EsChestType() {
        name = this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * @deprecated Temporary method, will be removed in the future.
     */
    @Deprecated
    @ApiStatus.Internal
    public static EsChestType of(CursedChestType type) {
        if (type == CursedChestType.TOP) return EsChestType.TOP;
        else if (type == CursedChestType.BOTTOM) return EsChestType.BOTTOM;
        else if (type == CursedChestType.FRONT) return EsChestType.FRONT;
        else if (type == CursedChestType.BACK) return EsChestType.BACK;
        else if (type == CursedChestType.LEFT) return EsChestType.LEFT;
        else if (type == CursedChestType.RIGHT) return EsChestType.RIGHT;
        else if (type == CursedChestType.SINGLE) return EsChestType.SINGLE;
        throw new IllegalStateException("Invalid type passed");
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
