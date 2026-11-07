package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** TAB listesinde (yalnizca yerel olarak) tier gosterir. */
@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {
    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
    private void tiertagger$tab(PlayerInfo info, CallbackInfoReturnable<Component> cir) {
        String name = TierConfig.profileName(info.getProfile());
        cir.setReturnValue(TierConfig.decorate(cir.getReturnValue(), name));
    }
}
