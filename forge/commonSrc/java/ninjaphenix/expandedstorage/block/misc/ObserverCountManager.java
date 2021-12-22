package ninjaphenix.expandedstorage.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class ObserverCountManager {
    private static int RECOUNT_DELAY = 5;
    private int observers;

    public void open(Player player, Level world, BlockPos pos, BlockState state) {
        if (observers == 0) {
            this.onOpen(world, pos, state);
            this.scheduleObserverRecount(world, pos, state);
        }
        this.onObserverCountUpdated(world, pos, state, observers, ++observers);
    }

    private void scheduleObserverRecount(Level world, BlockPos pos, BlockState state) {
        world.getBlockTicks().scheduleTick(pos, state.getBlock(), ObserverCountManager.RECOUNT_DELAY);
    }

    public void close(Player player, Level world, BlockPos pos, BlockState state) {
        int oldObserverCount = observers--;
        if (observers == 0) {
            this.onClose(world, pos, state);
        }
        this.onObserverCountUpdated(world, pos, state, oldObserverCount, observers);
    }

    public void updateObserverCount(ServerLevel world, BlockPos pos, BlockState state) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int realObservers = world.getEntities(EntityType.PLAYER, new AABB(x - 5, y - 5, z - 5, x + 6, y + 6, z + 6), this::isPlayerViewing).size();
        int oldObserverCount = observers;
        if (realObservers != oldObserverCount) {
            if (oldObserverCount == 0) {
                this.onOpen(world, pos, state);
            } else if (realObservers == 0) {
                this.onClose(world, pos, state);
            }
            observers = realObservers;
            this.onObserverCountUpdated(world, pos, state, oldObserverCount, realObservers);
        }
        if (realObservers != 0) {
            this.scheduleObserverRecount(world, pos, state);
        }
    }

    protected abstract void onOpen(Level world, BlockPos pos, BlockState state);

    protected abstract void onClose(Level world, BlockPos pos, BlockState state);

    protected void onObserverCountUpdated(Level world, BlockPos pos, BlockState state, int oldCount, int newCount) {

    }

    protected abstract boolean isPlayerViewing(Player player);
}
