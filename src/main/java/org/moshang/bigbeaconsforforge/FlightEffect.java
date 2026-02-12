package org.moshang.bigbeaconsforforge;

import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

public class FlightEffect extends MobEffect {

    public FlightEffect() {
        super(
                MobEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
                0x30F8FF); // color in RGB
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    // This method is called when it applies the status effect. We implement custom functionality here.
    @Override
    public void applyEffectTick(net.minecraft.world.entity.LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            allowFlying(player);
        }
    }

    private void allowFlying(Player player) {
        player.getAbilities().mayfly = true;
        if(player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
        }
        if(player.level().isClientSide) {
            player.onUpdateAbilities();
        }
    }
}