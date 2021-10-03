package com.limeshulkerbox.fdcac.events;

import com.limeshulkerbox.fdcac.other.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fdcac.other.ChatThroughDiscord.serverStoppingMethod;

public class GetServerStoppingEvent implements ServerLifecycleEvents.ServerStopping {
    @Override
    public void onServerStopping(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStoppingMethod();
        }
    }
}