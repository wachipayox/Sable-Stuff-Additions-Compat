package com.wachi.sable_sa_compat.mixin.create_sa.block_picker;

import com.llamalad7.mixinextras.sugar.Local;
import dev.ryanhcode.sable.Sable;
import net.mcreator.createstuffadditions.procedures.BlockPickerItemInHandTickProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockPickerItemInHandTickProcedure.class)
public abstract class BlockPickerTickMixin {

    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/BlockHitResult;getBlockPos()Lnet/minecraft/core/BlockPos;"),
            remap = false)
    private static BlockPos projectClipIfSable(BlockHitResult instance, @Local(argsOnly = true) LevelAccessor world) {
        var centerPos = instance.getBlockPos().getCenter();

        if(world instanceof Level level && Sable.HELPER.isInPlotGrid(level, centerPos))
            centerPos = Sable.HELPER.projectOutOfSubLevel(level, centerPos);

        return BlockPos.containing(centerPos);
    }




}
