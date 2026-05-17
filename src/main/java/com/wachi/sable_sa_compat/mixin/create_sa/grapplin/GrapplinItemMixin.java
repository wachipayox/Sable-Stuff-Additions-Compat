package com.wachi.sable_sa_compat.mixin.create_sa.grapplin;

import com.wachi.sable_sa_compat.SableSaCompat;
import net.mcreator.createstuffadditions.item.GrapplinWhiskItem;
import net.mcreator.createstuffadditions.procedures.GrapplinWhiskItemInHandTickProcedure;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrapplinWhiskItem.class)
public class GrapplinItemMixin {

    // this replaces the original queueServerWork call in GrapplinWhiskItemInHandTickProcedure#execute
    // originally create_sa uses queueServerWork for removing tagHooked when item is not selected and causes offhand problems
    @Inject(method = "inventoryTick", at = @At("TAIL"), remap = false)
    public void afterTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected, CallbackInfo ci){
        if(!selected && entity instanceof LivingEntity lEnt) {
            if(lEnt.getOffhandItem().equals(itemstack)) {
                SableSaCompat.LOGGER.info(lEnt.toString());
                GrapplinWhiskItemInHandTickProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack);
            } else if (
                    itemstack.getOrDefault(
                            DataComponents.CUSTOM_DATA,
                            CustomData.EMPTY
                    ).copyTag().getBoolean("tagHooked")
            ) CustomData.update(DataComponents.CUSTOM_DATA, itemstack, (tag) -> tag.putBoolean("tagHooked", false));
        }
    }
}
