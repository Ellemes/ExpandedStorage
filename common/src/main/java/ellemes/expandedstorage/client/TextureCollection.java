package ellemes.expandedstorage.client;

import net.minecraft.resources.ResourceLocation;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;

public final class TextureCollection {
    private final ResourceLocation single;
    private final ResourceLocation left;
    private final ResourceLocation right;
    private final ResourceLocation top;
    private final ResourceLocation bottom;
    private final ResourceLocation front;
    private final ResourceLocation back;

    public TextureCollection(ResourceLocation single, ResourceLocation left, ResourceLocation right,
                             ResourceLocation top, ResourceLocation bottom, ResourceLocation front, ResourceLocation back) {
        this.single = single;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.front = front;
        this.back = back;
    }

    public ResourceLocation getTexture(CursedChestType type) {
        if (type == CursedChestType.TOP) {
            return top;
        } else if (type == CursedChestType.BOTTOM) {
            return bottom;
        } else if (type == CursedChestType.FRONT) {
            return front;
        } else if (type == CursedChestType.BACK) {
            return back;
        } else if (type == CursedChestType.LEFT) {
            return left;
        } else if (type == CursedChestType.RIGHT) {
            return right;
        } else if (type == CursedChestType.SINGLE) {
            return single;
        }
        throw new IllegalArgumentException("TextureCollection#getTexture received an unknown CursedChestType.");
    }
}
