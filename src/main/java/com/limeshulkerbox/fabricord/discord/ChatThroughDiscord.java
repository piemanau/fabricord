package com.limeshulkerbox.fabricord.discord;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.GetPlayersInterface;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import com.limeshulkerbox.fabricord.minecraft.mixins.PlayerManagerMixin;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public class ChatThroughDiscord extends ListenerAdapter implements UpdateConfigsInterface, GetPlayersInterface {

    static String content;

    public ChatThroughDiscord() {
    }

    //Prompts
    public static void serverStartingMethod() {
        API.sendMessage(ServerInitializer.config.getServerStartingPrompt(), false, true, false);
    }

    public static void serverStartedMethod() {
        API.sendMessage(ServerInitializer.config.getServerStartedPrompt(), false,true, false);
    }

    public static void serverStoppingMethod() {
        API.sendMessage(ServerInitializer.config.getServerStoppingPrompt(), false, true, false);
    }

    public static void serverStoppedMethod() {
        API.sendMessage(ServerInitializer.config.getServerStoppedPrompt(), false, true, false);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        ServerInitializer.jdaReady = true;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        //Makes sure the Author is not a bot
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        content = message.getContentRaw();

        if (content.startsWith("/")) {
            //Checks if the user has the appropriate role
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(event.getGuild().getRoleById(ServerInitializer.config.getCommandsAccessRoleID()))) {
                event.getChannel().sendMessage("Sorry <@" + event.getMember().getId() + "> you don't have access to the console. If you believe you should have access, contact an Admin of this discord server.").queue();
                return;
            }

            //Check correct channel
            if (!event.getChannel().equals(event.getGuild().getTextChannelById(ServerInitializer.config.getChatChannelID())) && ServerInitializer.config.getCommandsInChatChannel()) {
                event.getChannel().sendMessage("Sorry <@" + event.getMember().getId() + "> this is not the console channel or commands are not enabled here.").queue();
                return;
            }

            //Updates the config if /updateconfigs is run
            //TODO: change /updateconfigs to a discord / command
            if (content.toLowerCase(Locale.ROOT).equals("/updateconfigs")) {
                updateConfigs();
                API.sendMessageToDiscord("Configs updated!", event.getChannel());
                return;
            } else if (content.toLowerCase(Locale.ROOT).equals("/list")) {
                API.sendMessageToDiscord(getPlayers(), event.getChannel());
            }

            //Register a new CommandManager
            CommandManager command = new CommandManager(CommandManager.RegistrationEnvironment.DEDICATED);
            try {
                //Attempt to send command
                command.execute(API.getServerVariable().getCommandSource(), content);
            } catch (Exception e) {
                e.printStackTrace();
            }

            } else {
            if (!event.getChannel().equals(event.getGuild().getTextChannelById(ServerInitializer.config.getChatChannelID()))) {
                return;
            }
            //Send message to Minecraft chat
            API.sendMessage("[" + event.getMember().getUser().getName() + "] " + content, false, false, true);
        }
    }
}