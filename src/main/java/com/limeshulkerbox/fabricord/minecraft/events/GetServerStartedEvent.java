package com.limeshulkerbox.fabricord.minecraft.events;

import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fabricord.discord.ChatThroughDiscord.serverStartedMethod;

public class GetServerStartedEvent implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStartedMethod();
        }
    }
}
