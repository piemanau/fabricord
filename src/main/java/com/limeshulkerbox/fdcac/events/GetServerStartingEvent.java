package com.limeshulkerbox.fdcac.events;

import com.limeshulkerbox.fdcac.other.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fdcac.other.ChatThroughDiscord.serverStartingMethod;

public class GetServerStartingEvent implements ServerLifecycleEvents.ServerStarting {
    @Override
    public void onServerStarting(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStartingMethod();
        }
    }
}
