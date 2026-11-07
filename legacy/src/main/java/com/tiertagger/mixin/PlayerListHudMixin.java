package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** TAB listesinde (yalnizca yerel olarak) tier gosterir. */
@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void tiertagger$tab(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        String name = TierConfig.profileName(entry.getProfile());
        cir.setReturnValue(TierConfig.decorate(cir.getReturnValue(), name));
    }
}
