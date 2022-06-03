package ellemes.expandedstorage.block.strategies;

import net.minecraft.world.entity.player.Player;

public interface Observable {
    Observable NOT = new Observable() {
        @Override
        public void playerStartViewing(Player player) {

        }

        @Override
        public void playerStopViewing(Player player) {

        }
    };

    void playerStartViewing(Player player);

    void playerStopViewing(Player player);
}
