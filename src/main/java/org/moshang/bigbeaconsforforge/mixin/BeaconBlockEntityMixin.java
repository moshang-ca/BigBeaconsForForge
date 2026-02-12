package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import org.moshang.bigbeaconsforforge.BigBeaconsForForge;
import org.moshang.bigbeaconsforforge.PlayerModdedDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Shadow @Final @Mutable public static MobEffect[][] BEACON_EFFECTS;

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Arrays;stream([Ljava/lang/Object;)Ljava/util/stream/Stream;"
            ))
    private static Stream<MobEffect[]> modifyBeaconEffects(Object[] array) {
        BEACON_EFFECTS = new MobEffect[][]{
                {MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED},
                {MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP},
                {MobEffects.DAMAGE_BOOST},
                {MobEffects.REGENERATION},
                {MobEffects.FIRE_RESISTANCE},
                {MobEffects.SATURATION},
                {MobEffects.ABSORPTION},
                {MobEffects.LUCK}
        };
        return Arrays.stream(BEACON_EFFECTS);
    }

    @ModifyConstant(method = "updateBase", constant = @Constant(intValue = 4))
    private static int moreLevels(int curr) {
        return 16;
    }

    @Inject(method = "applyEffects", at = @At("RETURN"))
    private static void applyLevelThreeEffects(Level pLevel, BlockPos pPos, int pLevels, MobEffect pPrimary, MobEffect pSecondary, CallbackInfo ci) {
        if(!pLevel.isClientSide) {
            double d0 = pLevels * 10 + 10;
            AABB aabb = (new AABB(pPos)).inflate(d0).expandTowards(0.0D, (double) pLevel.getHeight(), 0.0D);
            List<Player> list = pLevel.getEntitiesOfClass(Player.class, aabb);
            int j = (9 + pLevels * 2) * 20;
            if (pLevels >= 10 && pPrimary != null && Objects.equals(pPrimary, pSecondary)) {
                for (Player player1 : list) {
                    player1.addEffect(new MobEffectInstance(pPrimary, j, 2, true, true));
                }
            }
            if (pLevel.getGameRules().getBoolean(BigBeaconsForForge.BEACON_FLIGHT) && pLevels >= 16) {
                MobEffect flight = BigBeaconsForForge.FLIGHT_EFFECT.get();
                // we can't give the effect to vanilla players, it will cause them to disconnect since they don't know what it is
                // so we check if they have the mod locally (not an SPE) or if the server has them as having the mod
                for (Player player1 : list) {
                    if (!(player1 instanceof ServerPlayer) || ((PlayerModdedDuck) player1).bigBeaconsForForge$hasMod()) {
                        player1.addEffect(new MobEffectInstance(flight, j, 0, true, false));
                    }
                }
            }
        }
    }
}
