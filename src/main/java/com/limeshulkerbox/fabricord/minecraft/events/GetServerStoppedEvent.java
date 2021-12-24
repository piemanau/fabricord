package com.limeshulkerbox.fabricord.minecraft.events;

import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fabricord.discord.ChatThroughDiscord.serverStoppedMethod;

public class GetServerStoppedEvent implements ServerLifecycleEvents.ServerStopped {
    @Override
    public void onServerStopped(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStoppedMethod();
            ServerInitializer.stopDiscordBot();
            GetServerStartedEvent.thread.interrupt();
        }
    }
}