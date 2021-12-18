package com.limeshulkerbox.fabricord.discord;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

public final class UpdateConfigsCommand implements Command<Object>, UpdateConfigsInterface {
    @Override
    public int run(CommandContext<Object> context) {
        updateConfigs();
        return 0;
    }
}