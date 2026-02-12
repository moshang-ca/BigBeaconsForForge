package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BeaconScreen.BeaconConfirmButton.class)
public class BeaconConfirmButtonMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 220))
    private static int resizeHeight(int value) {
        return 234;
    }
}
