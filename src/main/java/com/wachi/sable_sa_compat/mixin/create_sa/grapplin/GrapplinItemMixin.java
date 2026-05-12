package com.wachi.sable_sa_compat.mixin.create_sa.grapplin;

import net.mcreator.createstuffadditions.item.GrapplinWhiskItem;
import net.mcreator.createstuffadditions.procedures.GrapplinWhiskItemInHandTickProcedure;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrapplinWhiskItem.class)
public class GrapplinItemMixin {

    //Makes posible grapplin in offhand
    @Inject(method = "inventoryTick", at = @At("TAIL"), remap = false)
    public void afterTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected, CallbackInfo ci){
        if(entity instanceof LivingEntity ent && ent.getOffhandItem().equals(itemstack))
            GrapplinWhiskItemInHandTickProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack);
    }
}
