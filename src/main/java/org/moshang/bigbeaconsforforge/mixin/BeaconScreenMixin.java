package org.moshang.bigbeaconsforforge.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.moshang.bigbeaconsforforge.BigBeaconsForForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings("removal")
@Mixin(BeaconScreen.class)
public abstract class BeaconScreenMixin extends AbstractContainerScreen<BeaconMenu> {

    public BeaconScreenMixin(BeaconMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0
            )
    )
    private static ResourceLocation showNewSprite(String vanillaId) {
        return new ResourceLocation(BigBeaconsForForge.MOD_ID, "textures/gui/container/bigbeacon.png");
    }

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 219))
    private static int resizeGui(int curr) {
        return 233;
    }

    @Redirect(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"))
    private void dontDrawItems(GuiGraphics instance, ItemStack pStack, int pX, int pY) {}

    @ModifyConstant(method = "init", constant = @Constant(intValue = 164))
    private static int moveDoneButton(int curr) {
        return 61;
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 190))
    private static int moveCancelButton(int curr) {
        return 87;
    }

    @ModifyArg(
            method = "init",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/screens/inventory/BeaconScreen$BeaconPowerButton;<init>(Lnet/minecraft/client/gui/screens/inventory/BeaconScreen;IILnet/minecraft/world/effect/MobEffect;ZI)V"),
            index = 1
    )
    private int realignEffectButtons(int curr) {
        int offset = curr - this.leftPos - 76;
        if (offset == -11 || offset == -23) {   // only button in row or left hand button
            return this.leftPos + 22;
        } else {
            return this.leftPos + 22 + 24;    // right hand button
        }
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 2, ordinal = 0))
    private static int changeNumberOfEffects(int curr) {
        return 7;
    }

    // this is necessary because of the jump from 3 to 5 on the effects on the left
    @ModifyArg(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/BeaconScreen$BeaconPowerButton;<init>(Lnet/minecraft/client/gui/screens/inventory/BeaconScreen;IILnet/minecraft/world/effect/MobEffect;ZI)V",
                    ordinal = 0
            ),
            index = 5
    )
    private int changeLevelOnAddedEffects(int level) {
        return level >= 3 ? level + 1 : level;
    }

    // Level 3 Button stuff

    @Shadow
    MobEffect primary;

    @Shadow
    @Final
    private List<BeaconScreen.BeaconButton> beaconButtons;

    // just made an anonymous class instead of LevelThreeEffectButtonWidget
    @Inject(method = "init", at = @At("TAIL"))
    private void addLevelThreeButton(CallbackInfo ci) {
        BeaconScreen.BeaconPowerButton effectButtonWidget = ((BeaconScreen)(Object)this).new BeaconPowerButton(this.leftPos + 157, this.topPos+ 100, BeaconBlockEntity.BEACON_EFFECTS[0][0], false, 9) {
            @Override
            protected MutableComponent createEffectDescription(MobEffect pEffect) {
                return Component.translatable(effect.getDescriptionId()).append(" III");
            }

            @Override
            public void updateStatus(int pBeaconTier) {
                if (BeaconScreenMixin.this.primary != null) {
                    this.visible = true;
                    this.setEffect(BeaconScreenMixin.this.primary);
                    super.updateStatus(pBeaconTier);
                } else {
                    this.visible = false;
                }
            }
        };
        effectButtonWidget.active = false;
        this.addRenderableWidget(effectButtonWidget);
        this.beaconButtons.add(effectButtonWidget);
    }


    // Texture stuff (numbers and text)

    @Unique
    private static final Component TERTIARY_POWER_TEXT = Component.translatable("block.minecraft.beacon.tertiary");

    @Inject(method = "renderLabels", at = @At("TAIL"))
    private void drawNumbersAndText(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, CallbackInfo ci) {
        pGuiGraphics.drawCenteredString(this.font, TERTIARY_POWER_TEXT, 169, 84, 14737632);

        for (int i = 1; i < 9; i++) {
            pGuiGraphics.drawCenteredString(this.font, Component.literal("" + (i <= 3 ? i : i + 1)), 16, 29 + (i - 1) * 25, 14737632);
        }
        pGuiGraphics.drawCenteredString(this.font, Component.literal("" + 4), 138, 54, 14737632);
        pGuiGraphics.drawCenteredString(this.font, Component.literal("" + 10), 149, 108, 14737632);
    }

}
