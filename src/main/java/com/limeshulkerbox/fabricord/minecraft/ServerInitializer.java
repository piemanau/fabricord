package com.limeshulkerbox.fabricord.minecraft;

import blue.endless.jankson.Jankson;
import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.discord.DiscordChat;
import com.limeshulkerbox.fabricord.minecraft.events.MinecraftConsoleAppender;
import com.limeshulkerbox.fabricord.minecraft.events.GetServerPromptEvents;
import com.limeshulkerbox.fabricord.other.Config;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.logging.Logger;

import static net.minecraft.server.command.CommandManager.literal;

@Environment(EnvType.SERVER)
public class ServerInitializer implements DedicatedServerModInitializer {

    public static final Jankson jankson = Jankson.builder().build();
    public static final UUID modUUID = UUID.fromString("0c4fc385-8b46-4ef2-8375-fcd19d71f45e");
    public static final int messageSplitterAmount = 2000;
    public static boolean canUseBot = false;
    public static Config config;
    public static boolean jdaReady = false;
    static JDA api;
    static Config defaultConfig = new Config(2,
            "Add bot token here",
            "",
            5000,
            true,
            "",
            true,
            true,
            true,
            true,
            "",
            true,
            true,
            false,
            true,
            false,
            false,
            "Add webhook URL here",
            "Server starting",
            "Server started",
            "Server stopping",
            "Server stopped",
            false,
            true,
            2000);
    private static int ticksPassed = 0;
    private static double tps;

    public static JDA getDiscordApi() {
        return api;
    }

    public static void setDiscordApiToNull() {
        api = null;
    }

    public static void onTick(MinecraftServer server) {
        ticksPassed++;
        if (ticksPassed <= 40)
            return;
        ticksPassed = 0;
        double avgTickTime = server.lastTickLengths[99] * 1.0E-6D;
        tps = Math.min(1000.0D / avgTickTime, 20.0D);
    }

    public static double getTPS() {
        return tps;
    }

    public static void startDiscordBot() {
        //Make the Discord bot come alive
        try {
            api = JDABuilder.createDefault(config.getBotToken()).addEventListeners(new DiscordChat()).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        canUseBot = true;
    }

    @Override
    public void onInitializeServer() {

        //Create default config file
        try {
            if (!Files.exists(API.getConfigPath())) {
                if (!Files.exists(API.getConfigPath().getParent())) {
                    Files.createDirectory(API.getConfigPath().getParent());
                }

                String str = jankson.toJson(defaultConfig).toJson(true, true);
                Files.writeString(API.getConfigPath(), str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the configs from the json
        API.reloadConfig(true);

        //Update configs command in MC
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> {
            dispatcher.register(literal("fabricord").then(CommandManager.literal("config").then(CommandManager.literal("reload")

                    .executes((context -> {
                        API.reloadConfig(false);
                        context.getSource().sendFeedback(Text.of("Successfully updated the config!"), true);
                        return 1;
                    }))

                    .then(CommandManager.argument("do_restart_discord_bot", BoolArgumentType.bool()).executes((context -> {
                                if (BoolArgumentType.getBool(context, "do_restart_discord_bot"))
                                    context.getSource().sendFeedback(Text.of("Attempting to reload the config and discord bot..."), true);
                                API.reloadConfig(BoolArgumentType.getBool(context, "do_restart_discord_bot"));
                                context.getSource().sendFeedback(Text.of("Successfully updated the config!"), true);
                                return 1;
                            }))
                    ))));
        });

        ServerMessageEvents.GAME_MESSAGE.register((message, typeKey) -> {
            String messageStr = message.getString();
                if (!config.isOnlyWebhooks()) {
                    if (config.isChatEnabled()) {
                        API.sendMessageToDiscordChat(messageStr);
                    }
                    if (config.isWebhooksEnabled()) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle(messageStr);
                        eb.setColor(Color.TRANSLUCENT);
                        API.sendEmbedToDiscordChat(eb);
                    }
                } else {
                    if (config.isWebhooksEnabled()) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle(messageStr);
                        eb.setColor(Color.TRANSLUCENT);
                        API.sendEmbedToDiscordChat(eb);
                    } else if (config.isChatEnabled()) {
                        API.sendMessageToDiscordChat(messageStr);
                    }
                }
        });

        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, typeKey) -> {
            if (!config.isOnlyWebhooks()) {
                if (config.isChatEnabled()) {
                    API.sendMessageToDiscordChat(String.format("<%s> %s", sender.getName().getString(), message.raw().getContent().getString()));
                }
                if (config.isWebhooksEnabled()) {
                    API.sendMessageToDiscordWebhook(message.raw().getContent().getString().replaceFirst("<.+?> ", ""), sender.getName().getString(), "https://crafatar.com/avatars/" + sender.getUuid() + "?&overlay", config.getWebhookURL());
                }
            } else {
                if (config.isWebhooksEnabled()) {
                    API.sendMessageToDiscordWebhook(message.raw().getContent().getString().replaceFirst("<.+?> ", ""), sender.getName().getString(), "https://crafatar.com/avatars/" + sender.getUuid() + "?&overlay", config.getWebhookURL());
                } else if (config.isChatEnabled()) {
                    API.sendMessageToDiscordChat(String.format("<%s> %s", sender.getName().getString(), message.raw().getContent().getString()));
                }
            }
        });

        if (!config.getBotToken().equals("") && !config.getBotToken().equals("Add bot token here")) {

            //Register and make appender
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            MinecraftConsoleAppender minecraftConsoleAppender = new MinecraftConsoleAppender();
            minecraftConsoleAppender.start();
            ctx.getRootLogger().addAppender(minecraftConsoleAppender);
            ctx.updateLoggers();

            //Register prompt events
            ServerLifecycleEvents.SERVER_STARTING.register(new GetServerPromptEvents.GetServerStartingEvent());
            ServerLifecycleEvents.SERVER_STARTED.register(new GetServerPromptEvents.GetServerStartedEvent());
            ServerLifecycleEvents.SERVER_STOPPING.register(new GetServerPromptEvents.GetServerStoppingEvent());
            ServerLifecycleEvents.SERVER_STOPPED.register(new GetServerPromptEvents.GetServerStoppedEvent());
        } else {
            canUseBot = false;
            Logger.getLogger("Fabricord Logger").warning("Fabricord is currently NOT active, please check your config located in config/limeshulkerbox/fabricord.json and restart your server. If the issue persists please join the Discord server here: https://discord.com/invite/XhHuYG7aXT");
        }
    }
}