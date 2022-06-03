package ninjaphenix.expandedstorage.block.misc;

import net.minecraft.util.StringRepresentable;
import ninjaphenix.expandedstorage.api.EsChestType;
import ninjaphenix.expandedstorage.api.ExpandedStorageAccessors;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;

/**
 * Note to self, do not rename, used by chest tracker.
 *
 * @deprecated Use {@link EsChestType} and {@link ExpandedStorageAccessors} instead.
 */
@Deprecated
@ApiStatus.Internal
public enum CursedChestType implements StringRepresentable {
    TOP(-1),
    BOTTOM(-1),
    FRONT(0),
    BACK(2),
    LEFT(1),
    RIGHT(3),
    SINGLE(-1);

    private final String name;
    private final int offset;

    CursedChestType(int offset) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.offset = offset;
    }

    public static CursedChestType of(EsChestType type) {
        if (type == EsChestType.TOP) return CursedChestType.TOP;
        else if (type == EsChestType.BOTTOM) return CursedChestType.BOTTOM;
        else if (type == EsChestType.FRONT) return CursedChestType.FRONT;
        else if (type == EsChestType.BACK) return CursedChestType.BACK;
        else if (type == EsChestType.LEFT) return CursedChestType.LEFT;
        else if (type == EsChestType.RIGHT) return CursedChestType.RIGHT;
        else if (type == EsChestType.SINGLE) return CursedChestType.SINGLE;
        throw new IllegalStateException("Invalid type passed");
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public int getOffset() {
        return offset;
    }

    public CursedChestType getOpposite() {
        if (this == CursedChestType.TOP) {
            return CursedChestType.BOTTOM;
        } else if (this == CursedChestType.BOTTOM) {
            return CursedChestType.TOP;
        } else if (this == CursedChestType.FRONT) {
            return CursedChestType.BACK;
        } else if (this == CursedChestType.BACK) {
            return CursedChestType.FRONT;
        } else if (this == CursedChestType.LEFT) {
            return CursedChestType.RIGHT;
        } else if (this == CursedChestType.RIGHT) {
            return CursedChestType.LEFT;
        }
        throw new IllegalStateException("CursedChestType.SINGLE has no opposite type.");
    }
}
