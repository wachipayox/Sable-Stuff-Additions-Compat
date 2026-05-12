package com.wachi.sable_sa_compat.mixin.create_sa.jetpack.entity;

import com.wachi.sable_sa_compat.jetpack.IJetpackacableEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class StoreLastJetPackResultInEntity implements IJetpackacableEntity {

    @Unique private boolean weCompanion$lastJetpackResult = false;

    @Override
    public boolean weCompanion$getLastJetpackResult() {
        return weCompanion$lastJetpackResult;
    }

    @Override
    public void weCompanion$setLastJetpackResult(boolean result) {
        this.weCompanion$lastJetpackResult = result;
    }
}
