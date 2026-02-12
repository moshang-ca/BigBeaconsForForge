package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import org.moshang.bigbeaconsforforge.BigBeaconsForForge;
import org.moshang.bigbeaconsforforge.PlayerModdedDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin implements PlayerModdedDuck {

    @Unique
    boolean bigbeaconsforforge$hasMod = false;

    public boolean bigBeaconsForForge$hasMod() {
        return this.bigbeaconsforforge$hasMod;
    }

    public void bigBeaconsForForge$setHasMod(boolean modded) {
        this.bigbeaconsforforge$hasMod = modded;
    }

    @Inject(method = "onEffectRemoved", at = @At("TAIL"))
    private void doRemoveFlight(MobEffectInstance pEffect, CallbackInfo ci) {

        if (pEffect.getEffect() == BigBeaconsForForge.FLIGHT_EFFECT.get()) {
            ServerPlayer player = (ServerPlayer) (Object)this;
            if (!player.isSpectator() && !player.isCreative()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();

                player.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE, player));
            }
        }
    }


}
