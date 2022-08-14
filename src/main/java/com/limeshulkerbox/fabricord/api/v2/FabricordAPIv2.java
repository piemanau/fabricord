package com.limeshulkerbox.fabricord.api.v2;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;

import java.util.Objects;

import static com.limeshulkerbox.fabricord.api.v1.API.splitToNChar;
import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.*;

public class FabricordAPIv2 {

    /**
     * Here you can send a message to multiple places.
     *
     * @param message              This is the message you want to send.
     * @param sendToDiscordConsole This asks if you want to send to the Discord console.
     * @param sendToDiscordChat    This asks if you want to send to the Discord chat.
     * @param sendToMinecraft      This asks if you want to send to the Minecraft in-game chat.
     */
    public static void sendMessage(String message, String sender, boolean sendToDiscordConsole, boolean sendToDiscordChat, boolean sendToMinecraft) {
        if (!canUseBot) return;
        if (jdaReady) {
            if (sendToDiscordConsole) {
                sendMessageToDiscordConsole(message, sender);
            }

            if (sendToDiscordChat) {
                sendMessageToDiscordChat(message, sender);
            }
        }

        if (sendToMinecraft) {
            sendMessageToMinecraft(message, sender);
        }
    }

    /**
     * This method sends whatever message to the Minecraft chat.
     *
     * @param message This is the message you want to send to the Minecraft chat.
     */
    public static void sendMessageToMinecraft(String message, String sender) {
        API.getServerVariable().getPlayerManager().broadcast(SignedMessage.of(Text.of(message)), new MessageSender(modUUID, Text.of(sender)), MessageType.CHAT);
    }

    /**
     * This is where you send a message to the console from Minecraft in Discord.
     *
     * @param message This is the message you want to send.
     * @param sender This is the sender of the message.
     */
    public static void sendMessageToDiscordConsole(String message, String sender) {
        if (!canUseBot) return;
        if (!(Objects.equals(config.getConsoleChannelID(), "") || config.getConsoleChannelID() == null)) {
            if (message.length() <= messageSplitterAmount) {
                Objects.requireNonNull(ServerInitializer.getDiscordApi().getTextChannelById(config.getConsoleChannelID())).sendMessage("<" + sender + "> " + message).queue();
            } else {
                splitToNChar(message, messageSplitterAmount, config.getConsoleChannelID());
            }
        }
    }

    /**
     * This is the message you want to send to the Discord version of the Minecraft chat, where messages go that are sent in-game.
     *
     * @param message This is the message you want to send.
     * @param sender This is the sender of the message.
     */
    public static void sendMessageToDiscordChat(String message, String sender) {
        if (!canUseBot) return;
        if (!(Objects.equals(config.getChatChannelID(), "") || config.getChatChannelID() == null)) {
            if (message.length() <= messageSplitterAmount) {
                Objects.requireNonNull(ServerInitializer.getDiscordApi().getTextChannelById(config.getChatChannelID())).sendMessage("<" + sender + "> " + message).queue();
            } else {
                splitToNChar(message, messageSplitterAmount, config.getChatChannelID());
            }
        }
    }
}
