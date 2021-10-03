package com.limeshulkerbox.fdcac.mixins;

import com.limeshulkerbox.fdcac.other.ServerInitializer;
import net.minecraft.server.Main;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Main.class)
public class GetServerMixin {

    @ModifyVariable(method = "main", at = @At(value = "STORE"), ordinal = 0)
    private static MinecraftDedicatedServer getDedicatedServer(MinecraftDedicatedServer minecraftDedicatedServer) {
        //Get the server variable
        ServerInitializer.setServerVariable(minecraftDedicatedServer);
        return minecraftDedicatedServer;
    }
}