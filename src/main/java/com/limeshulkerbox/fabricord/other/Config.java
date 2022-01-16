package com.limeshulkerbox.fabricord.other;

import blue.endless.jankson.annotation.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
//@ToString
//@EqualsAndHashCode
public class Config {

    @SerializedName("bot_token")
    private String botToken;

    @SerializedName("commands_access_roles_id")
    private String commandsAccessRoleID;

    @SerializedName("update_bot_status_every")
    private int updateBotStatusEvery;


    @SerializedName("chat_enabled")
    private boolean chatEnabled;

    @SerializedName("chat_channel_id")
    private String chatChannelID;

    @SerializedName("commands_in_chat_channel")
    private boolean commandsInChatChannel;

    @SerializedName("prompt_enabled")
    private boolean promptsEnabled;


    @SerializedName("console_enabled")
    private boolean consoleEnabled;

    @SerializedName("console_channel_id")
    private String consoleChannelID;

    @SerializedName("show_debug_logs_in_console")
    private boolean showDebugLogsInConsole;

    @SerializedName("show_error_logs_in_console")
    private boolean showErrorLogsInConsole;

    @SerializedName("show_warn_logs_in_console")
    private boolean showWarnLogsInConsole;

    @SerializedName("show_info_logs_in_console")
    private boolean showInfoLogsInConsole;


    @SerializedName("webhooks_enabled")
    private boolean webhooksEnabled;

    @SerializedName("only_webhooks")
    private boolean onlyWebhooks;

    @SerializedName("webhook_url")
    private String webhookURL;


    @SerializedName("server_starting_prompt")
    private String serverStartingPrompt;

    @SerializedName("server_started_prompt")
    private String serverStartedPrompt;

    @SerializedName("server_stopping_prompt")
    private String serverStoppingPrompt;

    @SerializedName("server_stopped_prompt")
    private String serverStoppedPrompt;


    @SerializedName("keys_to_send_to_discord")
    private String[] keysToSendToDiscord;

    @SerializedName("commands_for_everyone")
    private String[] commandsForEveryone;

    public Config(String botToken, String commandsAccessRoleID, int updateBotStatusEvery, boolean chatEnabled, String chatChannelID, boolean commandsInChatChannel, boolean promptsEnabled, boolean consoleEnabled, String consoleChannelID, boolean showInfoLogsInConsole, boolean showWarnLogsInConsole, boolean showErrorLogsInConsole, boolean showDebugLogsInConsole, boolean webhooksEnabled, boolean onlyWebhooks, String webhookURL, String serverStartingPrompt, String serverStartedPrompt, String serverStoppingPrompt, String serverStoppedPrompt, String[] keysToSendToDiscord, String[] commandsForEveryone) {
        this.botToken = botToken;
        this.commandsAccessRoleID = commandsAccessRoleID;
        this.updateBotStatusEvery = updateBotStatusEvery;

        this.chatEnabled = chatEnabled;
        this.chatChannelID = chatChannelID;
        this.commandsInChatChannel = commandsInChatChannel;
        this.promptsEnabled = promptsEnabled;

        this.consoleEnabled = consoleEnabled;
        this.consoleChannelID = consoleChannelID;
        this.showDebugLogsInConsole = showDebugLogsInConsole;
        this.showErrorLogsInConsole = showErrorLogsInConsole;
        this.showWarnLogsInConsole = showWarnLogsInConsole;
        this.showInfoLogsInConsole = showInfoLogsInConsole;

        this.webhooksEnabled = webhooksEnabled;
        this.onlyWebhooks = onlyWebhooks;
        this.webhookURL = webhookURL;

        this.serverStartingPrompt = serverStartingPrompt;
        this.serverStartedPrompt = serverStartedPrompt;
        this.serverStoppingPrompt = serverStoppingPrompt;
        this.serverStoppedPrompt = serverStoppedPrompt;

        this.commandsForEveryone = commandsForEveryone;
        this.keysToSendToDiscord = keysToSendToDiscord;
    }

    public Config()
    {

    }
}