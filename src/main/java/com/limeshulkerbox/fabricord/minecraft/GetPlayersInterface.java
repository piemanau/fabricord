package com.limeshulkerbox.fabricord.minecraft;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public interface GetPlayersInterface {

    default String getPlayers() {
        return null;
    }
}
