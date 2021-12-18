package com.limeshulkerbox.fabricord.minecraft.events;

import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fabricord.discord.ChatThroughDiscord.serverStartingMethod;

public class GetServerStartingEvent implements ServerLifecycleEvents.ServerStarting {
    @Override
    public void onServerStarting(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStartingMethod();
        }
    }
}
