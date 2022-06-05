package ellemes.expandedstorage.client;

import ellemes.expandedstorage.api.EsChestType;
import net.minecraft.resources.ResourceLocation;

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

    public ResourceLocation getTexture(EsChestType type) {
        if (type == EsChestType.TOP) {
            return top;
        } else if (type == EsChestType.BOTTOM) {
            return bottom;
        } else if (type == EsChestType.FRONT) {
            return front;
        } else if (type == EsChestType.BACK) {
            return back;
        } else if (type == EsChestType.LEFT) {
            return left;
        } else if (type == EsChestType.RIGHT) {
            return right;
        } else if (type == EsChestType.SINGLE) {
            return single;
        }
        throw new IllegalArgumentException("TextureCollection#getTexture received an unknown EsChestType.");
    }
}
