package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ApplyBonusCount.class)
abstract class FortuneInject {

    @Final
    @Shadow
    Enchantment enchantment;

    @Inject(method = "run", at = @At(value = "HEAD"), cancellable = true)
    private void inject(ItemStack pStack, LootContext pContext, CallbackInfoReturnable<ItemStack> cir) {
        if (pContext.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity entity) {
            if (entity.hasEffect(MobEffects.LUCK) && this.enchantment == Enchantments.BLOCK_FORTUNE) {
                int i = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, entity.getMainHandItem());
                int count = pStack.getCount();
                i = i + entity.getEffect(MobEffects.LUCK).getAmplifier() + 1;  // the +1 is because the amplifier is the number of levels over one
                int bonus = pContext.getRandom().nextInt(i + 2) - 1; //these lines just integrate fortune functionality
                if (bonus < 0) {
                    bonus = 0;
                }
                pStack.setCount(count * (bonus + 1));
                cir.setReturnValue(pStack);
            }
        }

    }
}