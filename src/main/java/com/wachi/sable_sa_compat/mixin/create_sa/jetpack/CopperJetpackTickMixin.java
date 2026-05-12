package com.wachi.sable_sa_compat.mixin.create_sa.jetpack;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.wachi.sable_sa_compat.jetpack.JetpackTickHandler;
import net.mcreator.createstuffadditions.network.CreateSaModVariables;
import net.mcreator.createstuffadditions.procedures.CopperPropelerBodyTickEventProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CopperPropelerBodyTickEventProcedure.class)
public class CopperJetpackTickMixin {

    @Redirect(
            method = "execute",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;isEmptyBlock(Lnet/minecraft/core/BlockPos;)Z"),
            remap = false
    )
    private static boolean isEmptyAt(LevelAccessor world, BlockPos pos, @Local(argsOnly = true) Entity entity, @Local(name = "index0") LocalIntRef index0) {
        var jetpackRadius = CreateSaModVariables.MapVariables.get(world).copperJetpackMaxHeight;
        return JetpackTickHandler.checkIfEmptyTickIteration(world, entity, index0, jetpackRadius);
    }
}
