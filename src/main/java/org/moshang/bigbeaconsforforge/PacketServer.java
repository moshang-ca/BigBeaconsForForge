package org.moshang.bigbeaconsforforge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketServer extends BasicPacket {
    public PacketServer(boolean dummy) {
        super(dummy);
    }

    public static PacketServer decode(FriendlyByteBuf buf) {
        return new PacketServer(buf.readBoolean());
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ((PlayerModdedDuck) player).bigBeaconsForForge$setHasMod(true);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
