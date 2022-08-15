package com.limeshulkerbox.fabricord.minecraft.events;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.config;

public class MinecraftConsoleAppender extends AbstractAppender {

    public MinecraftConsoleAppender() {
        super("FabricordConsoleAppender", null, null, false, Property.EMPTY_ARRAY);
    }

    private static String conjoinedString = "";

    @Override
    public void append(LogEvent event) {
        if (event != null) {
            if (config.isConsoleBufferEnabled()) {
                //Checking if it is at the right level
                boolean print = false;
                if (event.getLevel() == Level.ERROR && config.isShowErrorLogsInConsole()) {
                    print = true;
                } else if (event.getLevel() == Level.INFO && config.isShowInfoLogsInConsole()) {
                    print = true;
                } else if (event.getLevel() == Level.WARN && config.isShowWarnLogsInConsole()) {
                    print = true;
                } else if (event.getLevel() == Level.DEBUG && config.isShowDebugLogsInConsole()) {
                    print = true;
                }
                if (print) {
                    conjoinedString += "[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage() + "\n";
                }
                if (conjoinedString.length() + event.getMessage().getFormattedMessage().length() > config.getConsoleBufferAmount() && conjoinedString.length() != 0) {
                    if (ServerInitializer.jdaReady) {
                        final int maxToSend = (2000 - 1) / 3; //java strings are utf-16, utf-16 to utf-8 can be up to 3x+1 characters in length, discord's max is 2000
                        while (conjoinedString.length() > 0) {
                            if (conjoinedString.length() > maxToSend) {
                                API.sendMessageToDiscordConsole(conjoinedString.substring(0, maxToSend));
                                conjoinedString = conjoinedString.substring(maxToSend);
                            } else {
                                API.sendMessageToDiscordConsole(conjoinedString);
                                conjoinedString = "";
                            }
                        }
                    }
                }
            } else {
                boolean print = false;
                if (event.getLevel() == Level.ERROR && config.isShowErrorLogsInConsole()) {
                    print = true;
                } else if (event.getLevel() == Level.INFO && config.isShowInfoLogsInConsole()) {
                    print = true;
                } else if (event.getLevel() == Level.WARN && config.isShowWarnLogsInConsole()) {
                    print = true;
                } else if (event.getLevel() == Level.DEBUG && config.isShowDebugLogsInConsole()) {
                    print = true;
                }
                if (print && event.getMessage().getFormattedMessage().length() > 0) {
                    API.sendMessageToDiscordConsole("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage());
                }
            }
        }
    }
}