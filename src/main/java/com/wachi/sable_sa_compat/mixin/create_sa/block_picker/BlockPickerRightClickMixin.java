package com.wachi.sable_sa_compat.mixin.create_sa.block_picker;

import com.wachi.sable_sa_compat.SableSaCompat;
import net.mcreator.createstuffadditions.procedures.BlockPickerRightclickedOnBlockProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockPickerRightclickedOnBlockProcedure.class)
public class BlockPickerRightClickMixin {

    @Redirect(
            method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;isEmptyBlock(Lnet/minecraft/core/BlockPos;)Z",
            ordinal = 0), remap = false
    )
    private static boolean checkBlockEntity(LevelAccessor instance, BlockPos blockPos){
        return instance.isEmptyBlock(blockPos)
                || (!SableSaCompat.allowBlockEntityPicking && instance.getBlockEntity(blockPos) != null);
    }
}
