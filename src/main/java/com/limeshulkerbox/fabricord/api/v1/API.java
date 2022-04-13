package com.limeshulkerbox.fabricord.api.v1;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import com.limeshulkerbox.fabricord.minecraft.events.GetServerPromptEvents;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.minecraft.network.MessageType;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.text.Text;

import javax.management.timer.Timer;
import java.util.Objects;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.*;

public class API {

    private static MinecraftDedicatedServer server;

    private static Timer upTime = new Timer();

    /**
     * Send any message to any channel in a Discord server.
     * @param message
     * This is the message you want to send.
     * @param channelID
     * This is the channel you want to send the message in.
     */

    public static void sendMessageToDiscord(String message, MessageChannel channelID) {
        if (!canUseBot) return;
        if (message.length() <= messageSplitterAmount) {
            Objects.requireNonNull(ServerInitializer.getDiscordApi().getTextChannelById(channelID.getId())).sendMessage(message).queue();
        } else {
            splitToNChar(message, messageSplitterAmount, channelID.toString());
        }
    }

    /**
     * This method sends whatever message to the Minecraft chat.
     * @param message
     * This is the message you want to send to the Minecraft chat.
     */
    public static void sendMessageToMinecraft(String message) {
        server.getPlayerManager().broadcast(Text.of(message), MessageType.CHAT, modUUID);
    }

    /**
     * This is the message you want to send to the Discord version of the Minecraft chat, where messages go that are sent in-game.
     * @param message
     * This is the message you want to send.
     */
    public static void sendMessageToDiscordChat(String message) {
        if (!canUseBot) return;
        if (message.length() <= messageSplitterAmount) {
            Objects.requireNonNull(ServerInitializer.getDiscordApi().getTextChannelById(config.getChatChannelID())).sendMessage(message).queue();
        } else {
            splitToNChar(message, messageSplitterAmount, config.getChatChannelID());
        }
    }

    /**
     * This is the embed you want to send to the Discord version of the Minecraft chat, where messages go that are sent in-game.
     * @param embed
     * This is the embed you want to send.
     */
    public static void sendEmbedToDiscordChat(EmbedBuilder embed) {
        if (!canUseBot) return;
        if (embed.length() <= messageSplitterAmount) {
            Objects.requireNonNull(Objects.requireNonNull(getDiscordApi().getTextChannelById(config.getChatChannelID())).sendMessageEmbeds(embed.build())).queue();
        }
    }

    /**
     * This is where you send a message to the console from Minecraft in Discord.
     * @param message
     * This is the message you want to send.
     */
    public static void sendMessageToDiscordConsole(String message) {
        if (!canUseBot) return;
        if (message.length() <= messageSplitterAmount) {
            Objects.requireNonNull(ServerInitializer.getDiscordApi().getTextChannelById(config.getConsoleChannelID())).sendMessage(message).queue();
        } else {
            splitToNChar(message, messageSplitterAmount, config.getConsoleChannelID());
        }
    }

    /**
     * This is where you can send a message through a Discord webhook.
     * @param message
     * This is the message you want to send.
     * @param botName
     * This is the name the webhook 'bot' will have.
     * @param botAvatarLink
     * This is the avatar the webhook 'bot' will have.
     * @param webHookUrl
     * This is the URL of the webhook.
     */
    public static void sendMessageToDiscordWebhook(String message, String botName, String botAvatarLink, String webHookUrl) {
        if (!canUseBot) return;
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
        WebhookClient client = WebhookClient.withUrl(webHookUrl);
        messageBuilder.setUsername(botName).setAvatarUrl(botAvatarLink).setContent(message);
        client.send(messageBuilder.build());
    }

    /**
     * Here you can send a message to multiple places.
     * @param message
     * This is the message you want to send.
     * @param sendToDiscordConsole
     * This asks if you want to send to the Discord console.
     * @param sendToDiscordChat
     * This asks if you want to send to the Discord chat.
     * @param sendToMinecraft
     * This asks if you want to send to the Minecraft in-game chat.
     */
    public static void sendMessage(String message, boolean sendToDiscordConsole, boolean sendToDiscordChat, boolean sendToMinecraft) {
        if (!canUseBot) return;
        if (jdaReady) {
            if (sendToDiscordConsole) {
                sendMessageToDiscordConsole(message);
            }

            if (sendToDiscordChat) {
                sendMessageToDiscordChat(message);
            }
        }

        if (sendToMinecraft) {
            sendMessageToMinecraft(message);
        }
    }

    /**
     * This returns the dedicated server variable if you need it.
     * @return
     * Returns the server variable.
     */
    public static MinecraftDedicatedServer getServerVariable() {
        return server;
    }

    /**
     * Sets the server variable to use for commands.
     * @param server
     * The server variable.
     */
    public static void setServerVariable(MinecraftDedicatedServer server) {
        API.server = server;
    }

    /**
     * This checks if the target is anywhere to be found in the array.
     * @param array
     * The array you want to check in.
     * @param target
     * The target you want to check for.
     * @return
     * Returns true/false if it is there or not.
     */
    public static boolean checkIfSomethingIsPresent(String[] array, String target) {
        if (array != null && target != null) {
            for (String s : array) {
                if (target.equals(s) || (target.equals(s.endsWith("*")) && target.equals(target.startsWith(s.replace("*", ""))))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void splitToNChar(String text, int size, String channelID) {
        int length = text.length();
        for (int i = 0; i < length; i += size) {
            Objects.requireNonNull(getDiscordApi().getTextChannelById(channelID)).sendMessage(text.substring(i, Math.min(length, i + size))).queue();
        }
    }

    /**
     * @return
     * This returns the server uptime.
     */
    public static String getUpTime() {
        return GetServerPromptEvents.GetServerStartedEvent.getUptime();
    }

    /**
     * @return
     * This returns the server TPS.
     */
    public static double getTPS() {
        return ServerInitializer.getTPS();
    }

    /**
     * @return
     * This returns the server uptime variable, please don't mess with this unless you know what you are doing.
     */
    public static Timer getUpTimeVariable() {
        return upTime;
    }
}