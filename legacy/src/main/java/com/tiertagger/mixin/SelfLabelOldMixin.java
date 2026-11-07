package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.20.1 - 1.21.1: vanilla kendi isim etiketini gizler (hasLabel(entity)). F5'te gorunmesi icin acar.
 * Metot bu imzayla yoksa (yeni surumler) sessizce atlanir.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class SelfLabelOldMixin {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void tiertagger$self(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        TierConfig.selfLabel(entity, cir);
    }
}
