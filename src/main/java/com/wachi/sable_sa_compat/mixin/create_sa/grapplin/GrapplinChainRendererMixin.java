package com.wachi.sable_sa_compat.mixin.create_sa.grapplin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.wachi.sable_sa_compat.SableSaCompat;
import dev.ryanhcode.sable.Sable;
import net.mcreator.createstuffadditions.procedures.GrapplinWhiskChainsLineProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrapplinWhiskChainsLineProcedure.class)
public abstract class GrapplinChainRendererMixin {

    @Unique private static Vec3 weCompanion$original = null, weCompanion$converted = null;

    @Inject(
            method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;D)V",
            at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", shift = At.Shift.AFTER), remap = false)
    private static void cleanThisMess(Event event, LevelAccessor world, double partialTick, CallbackInfo ci){
        if(weCompanion$converted != null || weCompanion$original!=null) {
            weCompanion$converted = null;
            weCompanion$original = null;
        }
    }

    /// This is a fix for the crash when a hooked sublevel gets unloaded
    /// This mixing in general applies to any case where the grappling is hooked to a position out of its range
    @Inject(
            method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;D)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;sqrt(D)D"), remap = false)
    private static void limitGrapplinRenderDistance(Event event, LevelAccessor world, double partialTick, CallbackInfo ci, @Local(name = "lcl_distance") LocalDoubleRef lcl_distance){
        lcl_distance.set(Math.clamp(lcl_distance.get(), 0, SableSaCompat.grapplinRange));
    }

    @Redirect(
            method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;D)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getDouble(Ljava/lang/String;)D"), remap = false)
    private static double hookToPos(CompoundTag instance, String key){
        if(weCompanion$converted == null || weCompanion$original == null) {
            Vec3 pos = new Vec3(instance.getDouble("xPostion") + 0.5,
                    instance.getDouble("yPostion") + 0.5,
                    instance.getDouble("zPostion") + 0.5
            );
            weCompanion$original = pos;
            try {
                pos = Sable.HELPER.projectOutOfSubLevel(Minecraft.getInstance().level, pos);
            } catch (Exception ignored){}

            weCompanion$converted = pos;
        }
        return switch (key) {
            case "xPostion" -> weCompanion$converted.x;
            case "yPostion" -> weCompanion$converted.y;
            case "zPostion" -> weCompanion$converted.z;
            default -> instance.getDouble(key);
        };
    }

    //the hookToPos mixin changes EVERY mention of the hooked pos to the one projected out of the sublevel. But the level#isemptyblock requires
    //the original sublevel coords to check de blck material
//    @Redirect(
//            method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;D)V",
//            at = @At(value = "INVOKE", target = "net/minecraft/world/level/LevelAccessor.isEmptyBlock (Lnet/minecraft/core/BlockPos;)Z"), remap = false)
//    private static boolean hookToPos2(LevelAccessor instance, BlockPos original){
//        var bPos = weCompanion$original != null ? BlockPos.containing(weCompanion$original) : original;
//        return instance.isEmptyBlock(bPos);
//    }
// 2.1.2 version removes isEMPTY call on the render (which makes sense) so this redirect is now useless!

    @ModifyConstant(
            method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;D)V",
            constant = @Constant(doubleValue = 0.5, ordinal = 19), remap = false
    )
    private static double dontGet(double constant){
        return 0.5;
    }

    @ModifyConstant(
            method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;D)V",
            constant = @Constant(doubleValue = 0.5, ordinal = 9), remap = false
    )
    private static double dontGet2(double constant){
        return 0.5;
    }

    @ModifyConstant(
            method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;D)V",
            constant = @Constant(doubleValue = 0.5), remap = false
    )
    private static double get(double constant){
        return 0;
    }

}
