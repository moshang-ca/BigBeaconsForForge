package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffectInstance;
import org.moshang.bigbeaconsforforge.BigBeaconsForForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;

@Mixin(EffectRenderingInventoryScreen.class)
public class EffectRenderingInventoryScreenMixin {

    @ModifyVariable(method = "renderEffects", ordinal = 0, at = @At(value = "STORE"))
    private Collection<MobEffectInstance> dontRenderFlight(Collection<MobEffectInstance> collection) {
        collection.removeIf(inst -> inst.getEffect() == BigBeaconsForForge.FLIGHT_EFFECT.get());
        return collection;
    }
}
