package ellemes.expandedstorage.quilt;

import org.objectweb.asm.tree.ClassNode;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public final class MixinPlugin implements IMixinConfigPlugin {
    private static final int MIXIN_PACKAGE_LENGTH = "ellemes.expandedstorage.quilt.mixin".length() + 1;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String className = mixinClassName.substring(MIXIN_PACKAGE_LENGTH);
        return switch (className) {
            case "HTMChestCompat", "HTMLockableBlockEntityCompat" -> QuiltLoader.isModLoaded("htm");
            case "ToweletteCompat" -> QuiltLoader.isModLoaded("towelette");
            default -> true;
        };
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
