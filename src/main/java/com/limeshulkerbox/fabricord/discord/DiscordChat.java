package com.limeshulkerbox.fabricord.discord;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import com.limeshulkerbox.fabricord.minecraft.events.GetServerPromptEvents;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.config;

public class DiscordChat extends ListenerAdapter {

    static String content;

    public DiscordChat() {
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isCorrectChannel(MessageReceivedEvent event, boolean checkForChatOnly) {
        if (checkForChatOnly && event.getChannel().equals(event.getGuild().getTextChannelById(config.getChatChannelID())))
            return true;
        if ((event.getChannel().equals(event.getGuild().getTextChannelById(config.getChatChannelID())) && config.isCommandsInChatChannel() || event.getChannel().equals(event.getGuild().getTextChannelById(config.getConsoleChannelID()))))
            return true;
        if (config.isSendWrongChannelMessage())
            event.getChannel().sendMessage("Sorry <@" + Objects.requireNonNull(event.getMember()).getId() + "> this is not the console channel or commands are not enabled here.").queue();
        return false;
    }

    public static void runMinecraftCommand(@NotNull MessageReceivedEvent event) {
        CommandManager command = new CommandManager(CommandManager.RegistrationEnvironment.DEDICATED, new CommandRegistryAccess(DynamicRegistryManager.BUILTIN.get()));
        try {
            //Attempt to send command
            ServerCommandSource cmdsrc = new ServerCommandSource(new CommandOutput() {

                @Override
                public void sendMessage(Text message) {
                    API.sendMessageToDiscord(message.getString(), event.getChannel());
                }

                @Override
                public boolean shouldReceiveFeedback() {
                    return true;
                }

                @Override
                public boolean shouldTrackOutput() {
                    return true;
                }

                @Override
                public boolean shouldBroadcastConsoleToOps() {
                    return true;
                }

                @Override
                public boolean cannotBeSilenced() {
                    return CommandOutput.super.cannotBeSilenced();
                }
            }, new Vec3d(0, 0, 0), new Vec2f(0, 0), API.getServerVariable().getOverworld(), 4, Objects.requireNonNull(event.getMember()).getNickname() + "on Discord", Text.of(Objects.requireNonNull(event.getMember()).getNickname() + "on Discord"), API.getServerVariable(), null);
            command.executeWithPrefix(cmdsrc, event.getMessage().getContentRaw());
        } catch (Exception e) {
            e.printStackTrace();
        }
  }

    //Prompts
    public static void serverStartingMethod() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(config.getServerStartingPrompt());
        eb.setColor(0x2e3136);
        if (config.isOnlyWebhooks()) {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            } else if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStartingPrompt(), false, true, false);
            }
        } else {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            }
            if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStartingPrompt(), false, true, false);
            }
        }
    }

    public static void serverStartedMethod() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(config.getServerStartedPrompt());
        eb.setColor(0x2e3136);
        if (config.isOnlyWebhooks()) {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            } else if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStartedPrompt(), false, true, false);
            }
        } else {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            }
            if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStartedPrompt(), false, true, false);
            }
        }
    }

    public static void serverStoppingMethod() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(config.getServerStoppingPrompt());
        eb.setColor(0x2e3136);
        if (config.isOnlyWebhooks()) {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            } else if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStoppingPrompt(), false, true, false);
            }
        } else {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            }
            if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStoppingPrompt(), false, true, false);
            }
        }
    }

    public static void serverStoppedMethod() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(config.getServerStoppedPrompt());
        eb.setColor(0x2e3136);
        if (config.isOnlyWebhooks()) {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            } else if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStoppedPrompt(), false, true, false);
            }
        } else {
            if (config.isWebhooksEnabled()) {
                API.sendEmbedToDiscordChat(eb);
            }
            if (config.isChatEnabled()) {
                API.sendMessage(config.getServerStoppedPrompt(), false, true, false);
            }
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        ServerInitializer.jdaReady = true;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        //Makes sure the sender is not a bot
        if (event.getAuthor().isBot()) return;

        //Gets the raw content of the message
        content = event.getMessage().getContentRaw();

        //See if the message is a command
        if (content.startsWith("/")) {
            if (!isCorrectChannel(event, false)) return;
            if (didFabricordCommandRun(event)) return;
            if (!hasAccess(event)) return;
            runMinecraftCommand(event);
            return;
        }
        if (!isCorrectChannel(event, true)) return;
        if (!config.isSendDiscriminatorToMinecraft()) {
            String name = Objects.requireNonNull(event.getMember()).getUser().getName();
            if (!(event.getMember().getNickname() == null)) name = event.getMember().getNickname();
            API.sendMessage(String.format("[%s] %s", name, content), false, false, true);
        } else {
            API.sendMessage(String.format("<%s> %s", Objects.requireNonNull(event.getMember()).getUser().getAsTag(), content), false, false, true);
        }
    }

    private boolean hasAccess(MessageReceivedEvent event) {
        if (config.getCommandsAccessRoleID() == null || config.getCommandsAccessRoleID().equals("")) return false;
        if (Objects.requireNonNull(event.getMember()).getRoles().contains(event.getGuild().getRoleById(config.getCommandsAccessRoleID()))) return true;
        if (config.isSendWrongChannelMessage()) event.getChannel().sendMessage("Sorry <@" + event.getMember().getId() + "> you don't have access to the console. If you believe you should have access, contact an Admin of this discord server.").queue();
        return false;
    }

    private boolean didFabricordCommandRun(MessageReceivedEvent event) {
        switch (content.toLowerCase(Locale.ROOT)) {
            case "/fabricord list" -> {
                API.sendMessageToDiscord(getList(), event.getChannel());
                return true;
            }
            case "/fabricord tps" -> {
                API.sendMessageToDiscord("The server TPS is: " + API.getTPS(), event.getChannel());
                return true;
            }
            case "/fabricord uptime" -> {
                API.sendMessageToDiscord("The server has been up for" + GetServerPromptEvents.GetServerStartedEvent.getUptime(), event.getChannel());
                return true;
            }
        }
        return false;
    }

    private String getList() {
        String[] playerNames = API.getServerVariable().getPlayerManager().getPlayerNames();
        StringBuilder formattedString = new StringBuilder();
        if (API.getServerVariable().getCurrentPlayerCount() != 0) {
            formattedString.append("Players online:\n");
            for (String playerName: playerNames) {
                ServerPlayerEntity player = Objects.requireNonNull(API.getServerVariable().getPlayerManager().getPlayer(playerName));
                String playerWorldName = player.getEntityWorld().getRegistryKey().getValue().toString();
                playerWorldName = switch (playerWorldName) {
                    case "minecraft:overworld" -> " in overworld";
                    case "minecraft:nether", "minecraft:the_nether" -> " in The Nether";
                    case "minecraft:end", "minecraft:the_end" -> " in The End";
                    default -> " somewhere else";
                };
                formattedString
                        .append(player.getName().getString())
                        .append(playerWorldName)
                        .append("\n");
            }
        }
        formattedString.append(String.format("\n%o/%o players online", API.getServerVariable().getCurrentPlayerCount(), API.getServerVariable().getMaxPlayerCount()));
        return formattedString.toString();
    }
}