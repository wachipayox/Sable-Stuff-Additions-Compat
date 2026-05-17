package com.wachi.sable_sa_compat.mixin.create_sa.grapplin;

import dev.ryanhcode.sable.Sable;
import net.mcreator.createstuffadditions.procedures.GrapplinWhiskItemInHandTickProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrapplinWhiskItemInHandTickProcedure.class)
public class GrapplinTickMixin {

    @Unique
    private static Vec3 weCompanion$original = null, weCompanion$converted = null;

    @Inject(
            method = "execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "HEAD"), remap = false)
    private static void onTickStart(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack, CallbackInfo ci){
        var compound = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        weCompanion$original = new Vec3(
                compound.getDouble("xPostion"),
                compound.getDouble("yPostion"),
                compound.getDouble("zPostion")
        );
        weCompanion$converted = world instanceof Level level
                ? Sable.HELPER.projectOutOfSubLevel(level, weCompanion$original)
                : weCompanion$original
        ;
    }

    @Redirect(
            method = "execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getDouble(Ljava/lang/String;)D", remap = false)
    )
    private static double hookToPos(CompoundTag instance, String key){
        if(weCompanion$converted != null){
            return switch (key) {
                case "xPostion" -> weCompanion$converted.x;
                case "yPostion" -> weCompanion$converted.y;
                case "zPostion" -> weCompanion$converted.z;
                default -> instance.getDouble(key);
            };
        }
        return instance.getDouble(key);
    }

    @Redirect(
            method = "execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/mcreator/createstuffadditions/CreateSaMod;queueServerWork(ILjava/lang/Runnable;)V", remap = false)
    )
    private static void hookToPos(int tick, Runnable action){
        //disable this
    }

    //the hookToPos mixin changes EVERY mention of the hooked pos to the one projected out of the sublevel. But the level#isemptyblock requires
    //the original sublevel coords to check de blck material
    @Redirect(
            method = "execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "net/minecraft/world/level/LevelAccessor.isEmptyBlock (Lnet/minecraft/core/BlockPos;)Z"), remap = false)
    private static boolean hookToPos2(LevelAccessor instance, BlockPos original){
        var bPos = weCompanion$original != null ? BlockPos.containing(weCompanion$original) : original;
        return instance.isEmptyBlock(bPos);
    }
}
