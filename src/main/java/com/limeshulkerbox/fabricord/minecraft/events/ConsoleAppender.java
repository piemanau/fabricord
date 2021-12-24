package com.limeshulkerbox.fabricord.minecraft.events;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.config;

public class ConsoleAppender extends AbstractAppender {

    public ConsoleAppender() {
        super("FabricordConsoleAppender", null, null, false, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(LogEvent event) {
        if (ServerInitializer.jdaReady) {
            //Checking if it is at the right level
            if (event.getLevel() == Level.ERROR && config.getShowErrorLogsInConsole()) {
                API.sendMessage("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage(), true, false, false);
            } else if (event.getLevel() == Level.INFO && config.getShowInfoLogsInConsole()) {
                API.sendMessage("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage(), true, false, false);
            } else if (event.getLevel() == Level.WARN && config.getShowWarnLogsInConsole()) {
                API.sendMessage("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage(), true, false, false);
            } else if (event.getLevel() == Level.DEBUG && config.getShowDebugLogsInConsole()) {
                API.sendMessage("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage(), true, false, false);
            }
        }
    }
}