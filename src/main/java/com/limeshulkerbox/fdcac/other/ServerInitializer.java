package com.limeshulkerbox.fdcac.other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.limeshulkerbox.fdcac.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.MessageType;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Environment(EnvType.SERVER)
public class ServerInitializer implements DedicatedServerModInitializer {

    public static Config config;
    public static boolean jdaReady = false;
    static JDA api;
    static Text previousDiscordConsoleMessage;
    static Text previousDiscordChatMessage;
    static Text previousMinecraftChatMessage;
    static boolean sendNextDiscordMessage = true;
    static Path configPath = Paths.get(FabricLoader.getInstance().getConfigDir() + "/limeshulkerbox/fdcac.json");
    private static MinecraftDedicatedServer server;
    private static List<ServerPlayerEntity> players;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //Return the server variable
    public static MinecraftDedicatedServer getServerVariable() {
        return server;
    }

    //Sets the server variable to use for commands
    public static void setServerVariable(MinecraftDedicatedServer server) {
        ServerInitializer.server = server;
    }

    //Sets the server variable to use for commands
    public static void setPlayerVariable(List<ServerPlayerEntity> players) {
        ServerInitializer.players = players;
    }

    public static void stopDiscordBot() {
        if (api == null) return;
        api.shutdown();
    }

    //For sending messages to Discord and Minecraft
    public static void sendMessage(Text message, boolean sendToDiscordChat, boolean sendToDiscordConsole, boolean sendToMinecraft, boolean ignoreDuplicateMessages) {
        //if (previousDiscordConsoleMessage == null)
        //    previousDiscordConsoleMessage = Text.of("LimeShulkerBox is cool");
        //if (previousDiscordChatMessage == null)
        //    previousDiscordChatMessage = Text.of("LimeShulkerBox is cool");
        //if (previousMinecraftChatMessage == null)
        //    previousMinecraftChatMessage = Text.of("LimeShulkerBox is cool");
        //if (!ignoreDuplicateMessages) {
        //    if (previousDiscordConsoleMessage.equals(message) || previousDiscordChatMessage.equals(message)) {
        //        return;
        //    }
        //}
        //if (sendToDiscordConsole) {
        //    previousDiscordConsoleMessage = message;
        //}
        //if (sendToDiscordChat) {
        //    previousDiscordChatMessage = message;
        //}
        //if (sendToMinecraft) {
        //    if (!previousMinecraftChatMessage.equals(previousDiscordChatMessage)) {
        //        sendNextDiscordMessage = true;
        //    } else {
        //        sendNextDiscordMessage = false;
        //    }
        //    previousMinecraftChatMessage = message;
        //}
        if (jdaReady) {
            if (sendToMinecraft) {
                server.sendSystemMessage(message, null);

                for (ServerPlayerEntity serverPlayerEntity : players) {
                    serverPlayerEntity.sendMessage(message, MessageType.CHAT, UUID.randomUUID());
                }
            }

            if (sendToDiscordChat && sendNextDiscordMessage) {
                Objects.requireNonNull(api.getTextChannelById(config.getChatChannelID())).sendMessage(message.getString()).queue();
            }

            if (sendToDiscordConsole) {
                Objects.requireNonNull(api.getTextChannelById(config.getConsoleChannelID())).sendMessage(message.getString()).queue();
            }
        }
    }

    //Method to grab stuff from the config file
    public void updateConfigs() {
        try {
            String contents = Files.readString(configPath);
            config = gson.fromJson(contents, Config.class);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            config = new Config("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456",
                    "123456789012345678",
                    "123456789012345678",
                    "123456789012345678",
                    true,
                    false,
                    true,
                    true,
                    "Server starting",
                    "Server started",
                    "Server stopping",
                    "Server stopped",
                    false);
        }
    }

    @Override
    public void onInitializeServer() {

        //Create default config file
        try {
            if (!Files.exists(configPath)) {
                if (!Files.exists(configPath.getParent())) {
                    Files.createDirectory(configPath.getParent());
                }
                String contents =
                        """
                                {
                                    "botToken": "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456",
                                    "chatChannelID": "123456789012345678",
                                    "consoleChannelID": "123456789012345678",
                                    "commandsAccessRoleID": "123456789012345678",
                                    "chatEnabled": "true",
                                    "commandsInChatChannel": "false",
                                    "consoleEnabled": "true",
                                    "promptsEnabled": "true",
                                    "serverStartingPrompt": "Server starting",
                                    "serverStartedPrompt": "Server started",
                                    "serverStoppingPrompt": "Server stopping",
                                    "serverStoppedPrompt": "Server stopped",
                                    "showDebugLogsInConsole": "false"
                                }
                                        """;
                Files.writeString(configPath, contents);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the configs from the json
        updateConfigs();

        //Make the Discord bot come alive
        try {
            api = JDABuilder.createDefault(config.getBotToken()).addEventListeners(new ChatThroughDiscord()).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        //Register and make appender
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.start();
        ctx.getRootLogger().addAppender(consoleAppender);
        ctx.updateLoggers();

        //Register prompt events
        ServerLifecycleEvents.SERVER_STARTING.register(new GetServerStartingEvent());
        ServerLifecycleEvents.SERVER_STARTED.register(new GetServerStartedEvent());
        ServerLifecycleEvents.SERVER_STOPPING.register(new GetServerStoppingEvent());
        ServerLifecycleEvents.SERVER_STOPPED.register(new GetServerStoppedEvent());
    }
}