package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Player kendi getDisplayName'ini override ediyorsa yakalar; yoksa sessizce atlanir. */
@Mixin(Player.class)
public abstract class PlayerNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, require = 0)
    private void tiertagger$tag(CallbackInfoReturnable<Component> cir) {
        Player self = (Player) (Object) this;
        cir.setReturnValue(TierConfig.decorate(cir.getReturnValue(), self.getName().getString()));
    }
}
