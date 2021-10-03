package com.limeshulkerbox.fdcac.events;

import com.limeshulkerbox.fdcac.other.ServerInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import static com.limeshulkerbox.fdcac.other.ChatThroughDiscord.serverStartedMethod;

public class GetServerStartedEvent implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        if (ServerInitializer.config.getPromptsEnabled()) {
            serverStartedMethod();
        }
    }
}
