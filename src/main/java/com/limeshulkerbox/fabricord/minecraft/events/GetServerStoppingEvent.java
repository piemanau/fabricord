package com.limeshulkerbox.fabricord.minecraft.events;

import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fabricord.discord.ChatThroughDiscord.serverStoppingMethod;

public class GetServerStoppingEvent implements ServerLifecycleEvents.ServerStopping {
    @Override
    public void onServerStopping(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStoppingMethod();
        }
    }
}