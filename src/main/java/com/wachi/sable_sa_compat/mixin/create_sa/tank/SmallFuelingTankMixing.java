package com.wachi.sable_sa_compat.mixin.create_sa.tank;

import net.mcreator.createstuffadditions.procedures.SmallFuelingTankItemInInventoryTickProcedure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SmallFuelingTankItemInInventoryTickProcedure.class)
public class SmallFuelingTankMixing {

    @ModifyConstant(
            method = "execute",
            constant = @Constant(doubleValue = 10)
    )
    private static double fix(double constant){
        return 1;
    }

    @ModifyConstant(
            method = "execute",
            constant = @Constant(intValue = 10)
    )
    private static int fix2(int constant){
        return 1;
    }
}
