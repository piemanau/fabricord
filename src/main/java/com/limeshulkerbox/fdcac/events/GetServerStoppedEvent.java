package com.limeshulkerbox.fdcac.events;

import com.limeshulkerbox.fdcac.other.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fdcac.other.ChatThroughDiscord.serverStoppedMethod;

public class GetServerStoppedEvent implements ServerLifecycleEvents.ServerStopped {
    @Override
    public void onServerStopped(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStoppedMethod();
            ServerInitializer.stopDiscordBot();
        }
    }
}