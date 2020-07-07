package net.elytraautoflight.mixin;

import net.elytraautoflight.ElytraAutoFlight;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.IllagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    ElytraAutoFlight elytraAutoFlight;

    @Inject(at = @At(value = "RETURN"), method = "addEntity")
    private void addEntity(int int_1, Entity entity_1, CallbackInfo ci) {

        //ClientWorld test;
        //test.addBlockEntity()

        if (elytraAutoFlight == null) elytraAutoFlight = ElytraAutoFlight.instance;

        if (entity_1 instanceof DrownedEntity) {
            //System.out.println(entity_1.toString());

            elytraAutoFlight.addDrowned((DrownedEntity)entity_1);
        }

        if (entity_1 instanceof IllagerEntity) {
            elytraAutoFlight.addIllager((IllagerEntity)entity_1);
        }

    }
}
