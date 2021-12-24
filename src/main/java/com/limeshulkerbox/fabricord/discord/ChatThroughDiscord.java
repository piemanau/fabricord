package com.limeshulkerbox.fabricord.discord;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.command.CommandManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public class ChatThroughDiscord extends ListenerAdapter implements UpdateConfigsInterface {

    static String content;

    public ChatThroughDiscord() {
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
            if (!checkPriveledges(event)) return;

            //Check correct channel
            if (!checkChannel(event)) return;

            //Updates the config if /updateconfigs is run
            //TODO: change /updateconfigs to a discord / command
            if (content.toLowerCase(Locale.ROOT).equals("/updateconfigs")) {
                updateConfigs();
                API.sendMessageToDiscord("Configs updated!", event.getChannel());
                return;
            } else if (content.toLowerCase(Locale.ROOT).equals("/list")) {
                String[] playerNames = API.getServerVariable().getPlayerManager().getPlayerNames();
                StringBuilder formattedString = new StringBuilder("");
                if (API.getServerVariable().getCurrentPlayerCount() != 0) {
                    formattedString.append("Players online are: ");
                    for (int i = 0; i < playerNames.length - 1; i++) {
                        formattedString.append(playerNames[i]).append(":").append(Objects.requireNonNull(API.getServerVariable().getPlayerManager().getPlayer(playerNames[i])).getEntityWorld().getDimension()).append(", ");
                    }
                    formattedString.append(playerNames[playerNames.length - 1]).append(Objects.requireNonNull(API.getServerVariable().getPlayerManager().getPlayer(playerNames[playerNames.length - 1])).getEntityWorld().getDimension());
                }
                formattedString.append("\nThere are " );
                formattedString.append(API.getServerVariable().getCurrentPlayerCount());
                formattedString.append(" players out of ");
                formattedString.append(API.getServerVariable().getMaxPlayerCount());
                formattedString.append(" players.");
                API.sendMessageToDiscord(formattedString.toString(), event.getChannel());
                return;
            } else if (content.toLowerCase(Locale.ROOT).equals("/tps")) {
                Spark spark = SparkProvider.get();
                if (spark == null) return;
                DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
                assert tps != null;
                double tpsLast10Secs = tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
                double tpsLast5Mins = tps.poll(StatisticWindow.TicksPerSecond.MINUTES_5);
                API.sendMessageToDiscord("The tps from the last 10 seconds: " + ((int) tpsLast10Secs) + "\nThe tps from the last 5 minutes: " + ((int) tpsLast5Mins), event.getChannel());
                return;
            } else if (content.toLowerCase(Locale.ROOT).equals("/uptime")) {
                API.sendMessageToDiscord(API.getUpTime().toString(), event.getChannel());
                return;
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
            if (!event.getChannel().equals(event.getGuild().getTextChannelById(ServerInitializer.config.getChatChannelID())))
                return;
            //Send message to Minecraft chat
            API.sendMessage("[" + Objects.requireNonNull(event.getMember()).getUser().getName() + "] " + content, false, false, true);
        }
    }

    private static boolean checkPriveledges(MessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).getRoles().contains(event.getGuild().getRoleById(ServerInitializer.config.getCommandsAccessRoleID()))) {
            event.getChannel().sendMessage("Sorry <@" + event.getMember().getId() + "> you don't have access to the console. If you believe you should have access, contact an Admin of this discord server.").queue();
            return false;
        }
        return true;
    }

    private static boolean checkChannel(MessageReceivedEvent event) {
        if (!event.getChannel().equals(event.getGuild().getTextChannelById(ServerInitializer.config.getChatChannelID())) && ServerInitializer.config.getCommandsInChatChannel()) {
            event.getChannel().sendMessage("Sorry <@" + event.getMember().getId() + "> this is not the console channel or commands are not enabled here.").queue();
            return false;
        }
        return true;
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
}