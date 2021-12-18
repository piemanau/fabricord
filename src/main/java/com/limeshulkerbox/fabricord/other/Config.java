package com.limeshulkerbox.fabricord.other;

public class Config {

    private String botToken;
    private String commandsAccessRoleID;
    private boolean chatEnabled;
    private boolean commandsInChatChannel;
    private String chatChannelID;
    private boolean consoleEnabled;
    private String consoleChannelID;
    private boolean promptsEnabled;
    private String serverStartingPrompt;
    private String serverStartedPrompt;
    private String serverStoppingPrompt;
    private String serverStoppedPrompt;
    private boolean showDebugLogsInConsole;
    private boolean showErrorLogsInConsole;
    private boolean showWarnLogsInConsole;
    private boolean showInfoLogsInConsole;
    private String webhookURL;
    private boolean webhooksEnabled;
    private String[] keysToSendToDiscord;
    private String[] commandsForEveryone;

    public Config(String botToken, String commandsAccessRoleID, boolean chatEnabled, String chatChannelID, boolean commandsInChatChannel, boolean promptsEnabled, boolean consoleEnabled, String consoleChannelID, boolean showInfoLogsInConsole, boolean showWarnLogsInConsole, boolean showErrorLogsInConsole, boolean showDebugLogsInConsole, boolean webhooksEnabled, String webhookURL, String serverStartingPrompt, String serverStartedPrompt, String serverStoppingPrompt, String serverStoppedPrompt, String[] keysToSendToDiscord, String[] commandsForEveryone) {
        this.botToken = botToken;
        this.commandsAccessRoleID = commandsAccessRoleID;

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
        this.webhookURL = webhookURL;

        this.serverStartingPrompt = serverStartingPrompt;
        this.serverStartedPrompt = serverStartedPrompt;
        this.serverStoppingPrompt = serverStoppingPrompt;
        this.serverStoppedPrompt = serverStoppedPrompt;

        this.commandsForEveryone = commandsForEveryone;
        this.keysToSendToDiscord = keysToSendToDiscord;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getChatChannelID() {
        return chatChannelID;
    }

    public void setChatChannelID(String chatChannelID) {
        this.chatChannelID = chatChannelID;
    }

    public String getConsoleChannelID() {
        return consoleChannelID;
    }

    public void setConsoleChannelID(String consoleChannelID) {
        this.consoleChannelID = consoleChannelID;
    }

    public String getCommandsAccessRoleID() {
        return commandsAccessRoleID;
    }

    public void setCommandsAccessRoleID(String commandsAccessRoleID) {
        this.commandsAccessRoleID = commandsAccessRoleID;
    }

    public boolean getChatEnabled() {
        return chatEnabled;
    }

    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    public boolean getCommandsInChatChannel() {
        return commandsInChatChannel;
    }

    public void setCommandsInChatChannel(boolean commandsInChatChannel) {
        this.commandsInChatChannel = commandsInChatChannel;
    }

    public boolean getConsoleEnables() {
        return consoleEnabled;
    }

    public void setConsoleEnabled(boolean consoleEnabled) {
        this.consoleEnabled = consoleEnabled;
    }

    public boolean getPromptsEnabled() {
        return promptsEnabled;
    }

    public void setPromptsEnabled(boolean promptsEnabled) {
        this.promptsEnabled = promptsEnabled;
    }

    public String getServerStartingPrompt() {
        return serverStartingPrompt;
    }

    public void setServerStartingPrompt(String serverStartingPrompt) {
        this.serverStartingPrompt = serverStartingPrompt;
    }

    public String getServerStartedPrompt() {
        return serverStartedPrompt;
    }

    public void setServerStartedPrompt(String serverStartedPrompt) {
        this.serverStartedPrompt = serverStartedPrompt;
    }

    public String getServerStoppingPrompt() {
        return serverStoppingPrompt;
    }

    public void setServerStoppingPrompt(String serverStoppingPrompt) {
        this.serverStoppingPrompt = serverStoppingPrompt;
    }

    public String getServerStoppedPrompt() {
        return serverStoppedPrompt;
    }

    public void setServerStoppedPrompt(String serverStoppedPrompt) {
        this.serverStoppedPrompt = serverStoppedPrompt;
    }

    public boolean getShowDebugLogsInConsole() {
        return showDebugLogsInConsole;
    }

    public void setShowDebugLogsInConsole(boolean showDebugLogsInConsole) {
        this.showDebugLogsInConsole = showDebugLogsInConsole;
    }

    public boolean getShowErrorLogsInConsole() {
        return showErrorLogsInConsole;
    }

    public void setShowErrorLogsInConsole(boolean showErrorLogsInConsole) {
        this.showErrorLogsInConsole = showErrorLogsInConsole;
    }

    public boolean getShowWarnLogsInConsole() {
        return showWarnLogsInConsole;
    }

    public void setShowWarnLogsInConsole(boolean showWarnLogsInConsole) {
        this.showWarnLogsInConsole = showWarnLogsInConsole;
    }

    public boolean getShowInfoLogsInConsole() {
        return showInfoLogsInConsole;
    }

    public void setShowInfoLogsInConsole(boolean showInfoLogsInConsole) {
        this.showInfoLogsInConsole = showInfoLogsInConsole;
    }

    public String getWebhookURL() {
        return webhookURL;
    }

    public void setWebhookURL(String webhookURL) {
        this.webhookURL = webhookURL;
    }

    public boolean getWebhooksEnabled() {
        return webhooksEnabled;
    }

    public void setWebhooksEnabled(boolean webhooksEnabled) {
        this.webhooksEnabled = webhooksEnabled;
    }

    public String[] getCommandsForEveryone() {
        return commandsForEveryone;
    }

    public void setCommandsForEveryone(String[] commandsForEveryone) {
        this.commandsForEveryone = commandsForEveryone;
    }

    public String[] getKeysToSendToDiscord() {
        return keysToSendToDiscord;
    }

    public void setKeysToSendToDiscord(String[] keysToSendToDiscord) {
        this.keysToSendToDiscord = keysToSendToDiscord;
    }
}