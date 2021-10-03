package com.limeshulkerbox.fdcac.other;

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

    public Config(String botToken, String chatChannelID, String consoleChannelID, String commandsAccessRoleID, boolean chatEnabled, boolean commandsInChatChannel, boolean consoleEnabled, boolean promptsEnabled, String serverStartingPrompt, String serverStartedPrompt, String serverStoppingPrompt, String serverStoppedPrompt, boolean showDebugLogsInConsole) {
        this.botToken = botToken;
        this.commandsAccessRoleID = commandsAccessRoleID;
        this.chatEnabled = chatEnabled;
        this.commandsInChatChannel = commandsInChatChannel;
        this.chatChannelID = chatChannelID;
        this.consoleEnabled = consoleEnabled;
        this.consoleChannelID = consoleChannelID;
        this.promptsEnabled = promptsEnabled;
        this.serverStartingPrompt = serverStartingPrompt;
        this.serverStartedPrompt = serverStartedPrompt;
        this.serverStoppingPrompt = serverStoppingPrompt;
        this.serverStoppedPrompt = serverStoppedPrompt;
        this.showDebugLogsInConsole = showDebugLogsInConsole;
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
}
