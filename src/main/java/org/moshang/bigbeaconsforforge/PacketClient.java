package org.moshang.bigbeaconsforforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketClient extends BasicPacket {
    public PacketClient(boolean dummy) {
        super(dummy);
    }

    public static PacketClient decode(FriendlyByteBuf buf) {
        return new PacketClient(buf.readBoolean());
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    BigBeaconsForForge.INSTANCE.sendToServer(new PacketServer(true));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
