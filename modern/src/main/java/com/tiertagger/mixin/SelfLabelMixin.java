package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Vanilla kendi isim etiketini gizler; F5'te gorunmesi icin acar (Mojang isimleri). */
@Mixin(LivingEntityRenderer.class)
public abstract class SelfLabelMixin {
    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void tiertagger$self(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        TierConfig.selfLabel(entity, cir);
    }
}
