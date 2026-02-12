package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    // Adds luck level to looting level, kind of weird place to do this, not totally sure looting checks are always routed through this method
    @Inject(method = "getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I", at = @At("RETURN"), cancellable = true)
    private static void addLuckToLooting(Enchantment pEnchantment, LivingEntity pEntity, CallbackInfoReturnable<Integer> cir) {
        if(pEnchantment == Enchantments.MOB_LOOTING) {
            var luckEffect = pEntity.getEffect(MobEffects.LUCK);
            if(luckEffect != null) {
                int luckLevel = luckEffect.getAmplifier() + 1;
                cir.setReturnValue(cir.getReturnValue() + luckLevel);
            }
        }
    }

}
