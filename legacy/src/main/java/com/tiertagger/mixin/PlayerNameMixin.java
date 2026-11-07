package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Bazi surumlerde PlayerEntity getDisplayName'i kendisi override eder; o durumda bu yakalar.
 * Metot yoksa (require = 0) sessizce atlanir. Cift etiket TierConfig.decorate icinde engellenir.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, require = 0)
    private void tiertagger$tag(CallbackInfoReturnable<Text> cir) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        cir.setReturnValue(TierConfig.decorate(cir.getReturnValue(), self.getName().getString()));
    }
}
