package com.limeshulkerbox.fdcac.events;

import com.limeshulkerbox.fdcac.other.ServerInitializer;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import static com.limeshulkerbox.fdcac.other.ServerInitializer.config;

public class ConsoleAppender extends AbstractAppender {

    public ConsoleAppender() {
        super("CustomConsoleAppender", null, null, false, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(LogEvent event) {
        //Checking if it is at the right level
        if (event.getLevel() == Level.ERROR || event.getLevel() == Level.INFO || event.getLevel() == Level.WARN) {
            ServerInitializer.sendMessage(Text.of("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage()), false, true, false, true);
        } else if (event.getLevel() == Level.DEBUG && config.getShowDebugLogsInConsole()) {
            ServerInitializer.sendMessage(Text.of("[" + event.getLevel() + "] " + event.getMessage().getFormattedMessage()), false, true, false, true);
        }
    }
}