package org.moshang.bigbeaconsforforge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class BasicPacket {
    private final boolean dummy;
    public BasicPacket(boolean dummy) {
        this.dummy = dummy;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(dummy);
    }

    public abstract void handle(Supplier<NetworkEvent.Context> ctx);
}
