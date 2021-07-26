package ninjaphenix.expandedstorage.base.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import ninjaphenix.expandedstorage.base.wrappers.NetworkWrapper;

import java.util.function.Supplier;

public final class RemovePlayerPreferenceCallbackMessage {
    public static void encode(RemovePlayerPreferenceCallbackMessage message, FriendlyByteBuf buffer) {

    }

    public static RemovePlayerPreferenceCallbackMessage decode(FriendlyByteBuf buffer) {
        //noinspection InstantiationOfUtilityClass
        return new RemovePlayerPreferenceCallbackMessage();
    }

    public static void handle(RemovePlayerPreferenceCallbackMessage message, Supplier<NetworkEvent.Context> wrappedContext) {
        NetworkEvent.Context context = wrappedContext.get();
        ServerPlayer player = context.getSender();
        if (player != null) {
            context.enqueueWork(() -> NetworkWrapper.getInstance().removeTypeSelectCallback(player));
            context.setPacketHandled(true);
        }
    }
}
