package com.wachi.sable_sa_compat.mixin.create_sa.block_picker;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.mcreator.createstuffadditions.entity.LiftedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(LiftedBlockEntity.class)
public class LiftedBlockEntityMixin {

    @Redirect(
            method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;containing(DDD)Lnet/minecraft/core/BlockPos;", remap = false)
    )
    public BlockPos containing(double x, double y, double z){
        final var entity = (LiftedBlockEntity)((Object)this);

        var feetPos = entity.getOnPos();
        if(Sable.HELPER.getContaining(
                entity.level(),
                feetPos
        ) instanceof SubLevel subLevel) {
            var bPos = BlockPos.containing(
                    subLevel.logicalPose().transformPositionInverse(
                            entity.getBoundingBox().getCenter()
            ));
            for (Direction direction : Direction.values())
                if (!subLevel.getLevel().isEmptyBlock(bPos.relative(direction)))
                    return bPos;

            var lateral = Set.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
            for (Direction direction : lateral)
                if (!subLevel.getLevel().isEmptyBlock(bPos.above().relative(direction))
                    || !subLevel.getLevel().isEmptyBlock(bPos.below().relative(direction)))
                    return bPos;
        }

        return BlockPos.containing(x, y, z);
    }
}
