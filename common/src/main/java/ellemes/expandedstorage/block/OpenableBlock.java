package ellemes.expandedstorage.block;

import ellemes.container_library.api.v2.OpenableBlockEntityProviderV2;
import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class OpenableBlock extends Block implements OpenableBlockEntityProviderV2, EntityBlock {
    private final ResourceLocation blockId;
    private final ResourceLocation blockTier;
    private final ResourceLocation openingStat;
    private final int slotCount;

    public OpenableBlock(Properties settings, ResourceLocation blockId, ResourceLocation blockTier, ResourceLocation openingStat, int slotCount) {
        super(settings);
        this.blockId = blockId;
        this.blockTier = blockTier;
        this.openingStat = openingStat;
        this.slotCount = slotCount;
    }

    public Component getInventoryTitle() {
        return this.getName();
    }

    public abstract ResourceLocation getBlockType();

    public final ResourceLocation getBlockId() {
        return blockId;
    }

    public final int getSlotCount() {
        return slotCount;
    }

    public final ResourceLocation getBlockTier() {
        return blockTier;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean bl) {
        if (state.getBlock().getClass() != newState.getBlock().getClass()) {
            if (world.getBlockEntity(pos) instanceof OpenableBlockEntity entity) {
                Containers.dropContents(world, pos, entity.getItems());
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, bl);
        } else {
            if (state.getBlock() != newState.getBlock() && world.getBlockEntity(pos) instanceof OpenableBlockEntity entity) {
                CompoundTag tag = entity.saveWithoutMetadata();
                world.removeBlockEntity(pos);
                if (world.getBlockEntity(pos) instanceof OpenableBlockEntity newEntity) {
                    newEntity.load(tag);
                }
            }
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName() && world.getBlockEntity(pos) instanceof OpenableBlockEntity entity) {
            entity.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onInitialOpen(ServerPlayer player) {
        player.awardStat(openingStat);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return this.ncl_onBlockUse(world, state, pos, player, hand, hit);
    }
}
