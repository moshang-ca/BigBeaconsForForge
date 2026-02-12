package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BeaconScreen.BeaconScreenButton.class)
public class BeaconScreenButtonMixin {
    @ModifyConstant(method = "renderWidget", constant = @Constant(intValue = 219))
    private static int resizeHeight(int value) {
        return 233;
    }
}
