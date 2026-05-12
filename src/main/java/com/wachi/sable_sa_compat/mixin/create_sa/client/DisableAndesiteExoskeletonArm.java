package com.wachi.sable_sa_compat.mixin.create_sa.client;

import com.wachi.sable_sa_compat.SableSaCompat;
import net.mcreator.createstuffadditions.client.renderer.exoskeleton.AndesiteExoskeletonFirstPersonRenderer;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AndesiteExoskeletonFirstPersonRenderer.class)
public class DisableAndesiteExoskeletonArm {

    @Inject(method = "onRenderPlayerHand", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void cancelNetheriteHand(RenderArmEvent event, CallbackInfo ci){
        if(SableSaCompat.hideFpArmorHand) ci.cancel();
    }

}
