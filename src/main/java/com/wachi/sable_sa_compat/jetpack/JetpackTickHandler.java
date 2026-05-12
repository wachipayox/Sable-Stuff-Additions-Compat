package com.wachi.sable_sa_compat.jetpack;

import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.HitResult;

public class JetpackTickHandler {

    public static boolean doClip(LevelAccessor level, Entity entity, double jetpackRadius){
        return level.clip(
                new ClipContext(
                        entity.position(),
                        entity.position().add(0, -jetpackRadius, 0),
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity
                )).getType().equals(HitResult.Type.BLOCK);
    }

    //returns the same variable but stores it at entity's data
    private static boolean doReturn(Entity entity, boolean value){
        ((IJetpackacableEntity) entity).weCompanion$setLastJetpackResult(value);
        return value;
    }

    /// Do the ground check for all the jetpacks.
    /// @return true if there is not a block below jetpack range, or false otherwise
    public static boolean checkIfEmptyTickIteration(LevelAccessor level, Entity entity, LocalIntRef index0, double jetpackRadius){
        index0.set((int) jetpackRadius); //destroys the original "for" method, instead we are using ours

        if((level.getLevelData().getGameTime() + entity.getUUID().hashCode()) % 5 != 0) {
            return ((IJetpackacableEntity) entity).weCompanion$getLastJetpackResult();
        }

        for(int i = 0; i < jetpackRadius;  i++){
            if(level.isEmptyBlock(entity.blockPosition().below(i)))
                continue;
            return doReturn(entity, false);
        } //Checks for ground with less expensive method, just using clip when necessary

        return doReturn(entity, !doClip(level, entity, jetpackRadius));
    }
}
