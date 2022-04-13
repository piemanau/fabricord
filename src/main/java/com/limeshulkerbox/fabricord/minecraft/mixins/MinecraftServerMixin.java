package com.limeshulkerbox.fabricord.minecraft.mixins;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(at = {@At("TAIL")}, method = {"tick"})
    private void onEndTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        ServerInitializer.onTick(API.getServerVariable());
    }
}
