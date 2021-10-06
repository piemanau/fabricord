package com.limeshulkerbox.fdcac.other;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public class ChatThroughDiscord extends ListenerAdapter {

    static String content;
    static String previousDiscordMessage;

    public ChatThroughDiscord() {
    }

    //Prompts
    public static void serverStartingMethod() {
        ServerInitializer.sendMessage(Text.of(ServerInitializer.config.getServerStartingPrompt()), true, false, false, true);
    }

    public static void serverStartedMethod() {
        ServerInitializer.sendMessage(Text.of(ServerInitializer.config.getServerStartedPrompt()), true, false, false, true);
    }

    public static void serverStoppingMethod() {
        ServerInitializer.sendMessage(Text.of(ServerInitializer.config.getServerStoppingPrompt()), true, false, false, true);
    }

    public static void serverStoppedMethod() {
        ServerInitializer.sendMessage(Text.of(ServerInitializer.config.getServerStoppedPrompt()), true, false, false, true);
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

            //Updates the config if /updateconfigs is run
            //TODO: change /updateconfigs to a discord / command
            if (content.toLowerCase(Locale.ROOT).equals("/updateconfigs")) {
                new ServerInitializer().updateConfigs();
                ServerInitializer.sendMessage(Text.of("Configs updated!"), true, false, false, true);
                return;
            }
            //Check correct channel
            if (!event.getChannel().equals(event.getGuild().getTextChannelById(ServerInitializer.config.getChatChannelID())) && ServerInitializer.config.getCommandsInChatChannel()) {
                event.getChannel().sendMessage("Sorry <@" + event.getMember().getId() + "> this is not the console channel or commands are not enabled here.").queue();
                return;
            }
            //Register a new CommandManager
            CommandManager command = new CommandManager(CommandManager.RegistrationEnvironment.DEDICATED);
            try {
                //Attempt to send command
                command.execute(ServerInitializer.getServerVariable().getCommandSource(), content);
            } catch (Exception e) {
                e.printStackTrace();
            }

            } else {
            if (!event.getChannel().equals(event.getGuild().getTextChannelById(ServerInitializer.config.getChatChannelID()))) {
                return;
            }
            //Send message to Minecraft chat
            ServerInitializer.sendMessage(Text.of("[" + event.getMember().getUser().getName() + "] " + content), false, false, true, false);
        }
    }
}