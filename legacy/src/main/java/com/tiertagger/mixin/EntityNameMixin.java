package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Oyuncu isim etiketi (kafa ustu) Entity#getDisplayName uzerinden gelir.
 * Bu metot 1.20.1'den 1.21.11'e kadar ayni kaldigi icin tek jar yeterli.
 */
@Mixin(Entity.class)
public abstract class EntityNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, require = 0)
    private void tiertagger$tag(CallbackInfoReturnable<Text> cir) {
        if ((Object) this instanceof PlayerEntity player) {
            cir.setReturnValue(TierConfig.decorate(cir.getReturnValue(), player.getName().getString()));
        }
    }
}
