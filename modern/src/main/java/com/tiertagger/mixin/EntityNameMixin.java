package com.tiertagger.mixin;

import com.tiertagger.TierConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Oyuncu isim etiketi Entity#getDisplayName uzerinden gelir (Mojang isimleri). */
@Mixin(Entity.class)
public abstract class EntityNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, require = 0)
    private void tiertagger$tag(CallbackInfoReturnable<Component> cir) {
        if ((Object) this instanceof Player player) {
            cir.setReturnValue(TierConfig.decorate(cir.getReturnValue(), player.getName().getString()));
        }
    }
}
