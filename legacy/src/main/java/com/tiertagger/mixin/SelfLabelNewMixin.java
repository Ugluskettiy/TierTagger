package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.21.2 - 1.21.11: hasLabel(entity, mesafe) imzasi. Eski surumlerde bu imza yoktur, sessizce atlanir.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class SelfLabelNewMixin {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void tiertagger$self(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        TierConfig.selfLabel(entity, cir);
    }
}
