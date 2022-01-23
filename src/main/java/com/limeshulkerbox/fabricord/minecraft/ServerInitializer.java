package com.limeshulkerbox.fabricord.minecraft;

import blue.endless.jankson.Jankson;
import com.limeshulkerbox.fabricord.discord.ChatThroughDiscord;
import com.limeshulkerbox.fabricord.minecraft.events.*;
import com.limeshulkerbox.fabricord.other.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import javax.security.auth.login.LoginException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static net.minecraft.server.command.CommandManager.literal;

@Environment(EnvType.SERVER)
public class ServerInitializer implements DedicatedServerModInitializer {

    public static Config config;
    public static boolean jdaReady = false;
    static JDA api;
    static String sConfigPath = FabricLoader.getInstance().getConfigDir() + "/limeshulkerbox/fabricord.json";
    static Path configPath = Paths.get(sConfigPath);
    static BufferedWriter fileWriter;
    static Config defaultConfig = new Config(1.0f,
            "Add bot token here",
            "",
            5000,
            true,
            "",
            true,
            true,
            true,
            "",
            true,
            true,
            true,
            false,
            true,
            true,
            "Add webhook URL here",
            "Server starting",
            "Server started",
            "Server stopping",
            "Server stopped",
            new String[]{"multiplayer.player.joined", "multiplayer.player.joined.renamed", "multiplayer.player.left", "chat.type.text"},
            new String[]{""},
            false);

    public static final Jankson jankson = Jankson.builder().build();
    public static final UUID modUUID = UUID.fromString("0c4fc385-8b46-4ef2-8375-fcd19d71f45e");
    public static final int messageSplitterAmount = 2000;

    public static void stopDiscordBot() {
        if (api == null) return;
        jdaReady = false;
        api.shutdown();
    }

    public static JDA getDiscordApi() {
        return api;
    }

    //Method to grab stuff from the config file
    public static void updateConfigs() {
        try {
            String contents = Files.readString(configPath);
            var json = jankson.load(contents);
            config = jankson.fromJson(json, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            config = new Config(1.0f,
                    "",
                    "",
                    5000,
                    false,
                    "",
                    true,
                    true,
                    false,
                    "",
                    false,
                    true,
                    true,
                    true,
                    false,
                    true,
                    "",
                    "Server starting",
                    "Server started",
                    "Server stopping",
                    "Server stopped",
                    new String[0],
                    new String[0],
                    false);
        }
    }

    @Override
    public void onInitializeServer() {

        try {
            //Create default config file
            if (!Files.exists(configPath)) {
                if (!Files.exists(configPath.getParent())) {
                    Files.createDirectory(configPath.getParent());
                }

                String str = jankson.toJson(defaultConfig).toJson(true, true);
                Files.writeString(configPath, str);


                //Get the configs from the json
                updateConfigs();

                //Update configs command in MC
                CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                    dispatcher.register(literal("updateconfigs").executes(context -> {
                        updateConfigs();
                        context.getSource().sendFeedback(Text.of("Successfully updated the configs!"), true);
                        return 1;
                    }));
                });

                ChatThroughDiscord.registerDiscordCommands();

                if (!config.getBotToken().equals("")) {
                    //Make the Discord bot come alive
                    api = JDABuilder.createDefault(config.getBotToken()).addEventListeners(new ChatThroughDiscord()).build();

                    //Register and make appender
                    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
                    ConsoleAppender consoleAppender = new ConsoleAppender();
                    consoleAppender.start();
                    ctx.getRootLogger().addAppender(consoleAppender);
                    ctx.updateLoggers();

                    //Register prompt events
                    ServerLifecycleEvents.SERVER_STARTING.register(new GetServerPromptEvents.GetServerStartingEvent());
                    ServerLifecycleEvents.SERVER_STARTED.register(new GetServerPromptEvents.GetServerStartedEvent());
                    ServerLifecycleEvents.SERVER_STOPPING.register(new GetServerPromptEvents.GetServerStoppingEvent());
                    ServerLifecycleEvents.SERVER_STOPPED.register(new GetServerPromptEvents.GetServerStoppedEvent());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}