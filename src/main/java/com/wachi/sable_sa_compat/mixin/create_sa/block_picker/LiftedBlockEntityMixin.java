package com.wachi.sable_sa_compat.mixin.create_sa.block_picker;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.mcreator.createstuffadditions.entity.LiftedBlockEntity;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LiftedBlockEntity.class)
public class LiftedBlockEntityMixin {

    @Redirect(
            method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;containing(DDD)Lnet/minecraft/core/BlockPos;", remap = false)
    )
    public BlockPos containing(double x, double y, double z){
        final var entity = (LiftedBlockEntity)((Object)this);
        for (SubLevel subLevel : Sable.HELPER.getAllIntersecting(entity.level(), new BoundingBox3d(entity.getBoundingBox()))) {
            var converted = subLevel.logicalPose().transformPositionInverse(new Vector3d(x, y, z));
            return BlockPos.containing(converted.x(), converted.y(), converted.z());
        }
        return BlockPos.containing(x, y, z);
    }
}
