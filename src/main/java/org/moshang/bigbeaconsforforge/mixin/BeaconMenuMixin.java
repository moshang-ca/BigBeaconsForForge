package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import org.jetbrains.annotations.Nullable;
import org.moshang.bigbeaconsforforge.PlayerModdedDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BeaconMenu.class)
public abstract class BeaconMenuMixin extends AbstractContainerMenu {

    protected BeaconMenuMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    // kinda hacky, but the only way to make it so that the vanilla slots aren't off by one from the modded slots afaik
    @ModifyConstant(
            method = "<init>(ILnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
            constant = @Constant(intValue = 136)
    )
    private int movePaymentSlotOffScreen(int curr) {
        return 1000000;
    }

    // move player inventory slots as they aren't built by a function.
    @Redirect(
            method = "<init>(ILnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/Container;III)Lnet/minecraft/world/inventory/Slot;")
    )
    private Slot modifySlotPosition(Container pContainer, int pSlot, int pX, int pY) {
        if(pContainer instanceof Inventory inv) {
            Player player = inv.player;
            if((!(player instanceof ServerPlayer) || ((PlayerModdedDuck) player).bigBeaconsForForge$hasMod()))
                return new Slot(pContainer, pSlot, pX + 26, pY + 7);
        }
        return new Slot(pContainer, pSlot, pX, pY);
    }

    @Redirect(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;hasItem()Z"))
    private boolean preventShiftClickingIntoPaymentSlot(Slot instance) {
        if(instance instanceof BeaconMenu.PaymentSlot) return true;
        return instance.hasItem();
    }


    @Redirect(method = "updateEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/BeaconMenu$PaymentSlot;hasItem()Z"))
    private boolean yesItHasStack(BeaconMenu.PaymentSlot slot) {
        return true;
    }


    /**
     * @author ColdLavaLamp
     * @reason BigBeacons removes the payment slot altogether
     */
    @Overwrite
    public boolean hasPayment() {
        return true;
    }
}
