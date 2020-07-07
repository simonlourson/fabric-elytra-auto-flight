package net.elytraautoflight.mixin;

import net.elytraautoflight.ElytraAutoFlight;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
/*
    ElytraAutoFlight elytraAutoFlight;

    @Inject(at = @At(value = "RETURN"), method = "addBlockEntity")
    public void addBlockEntity(BlockEntity blockEntity, CallbackInfoReturnable ci) {
    }
*/
}
