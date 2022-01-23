package com.limeshulkerbox.fabricord.minecraft.events;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import net.dv8tion.jda.api.entities.Activity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.util.Date;

import static com.limeshulkerbox.fabricord.discord.ChatThroughDiscord.*;
import static com.limeshulkerbox.fabricord.minecraft.events.GetServerPromptEvents.GetServerStartedEvent.thread;

public class GetServerPromptEvents {

    public static class GetServerStartingEvent implements ServerLifecycleEvents.ServerStarting {
        @Override
        public void onServerStarting(MinecraftServer server) {
            if (ServerInitializer.config.isPromptsEnabled()) {
                serverStartingMethod();
            }
        }
    }

    public static class GetServerStartedEvent implements ServerLifecycleEvents.ServerStarted {
        public static Date startTime;
        public static Thread thread;

        @Override
        public void onServerStarted(MinecraftServer server) {
            API.getUpTime().start();
            if (ServerInitializer.config.isPromptsEnabled()) {
                serverStartedMethod();
            }
            startTime = new Date();
            thread = new Thread(() ->
            {
                try {
                    for (long done = 0; ; done++) {
                        var expected = done * ServerInitializer.config.getUpdateBotStatusEvery();
                        var wait = expected - (new Date().getTime() - startTime.getTime());
                        if (wait > 0) Thread.sleep(wait); //should blocking-wait
                        //do stuff
                        {
                            if (SparkProvider.get() != null) {
                                Spark spark = SparkProvider.get();
                                    DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
                                    assert tps != null;
                                    double tpsLast10Secs = tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
                                    ServerInitializer.getDiscordApi().getPresence().setActivity(Activity.playing(String.format("Minecraft with %s other players! With a TPS of %.01f and uptime of %s.", API.getServerVariable().getCurrentPlayerCount() + "/" + API.getServerVariable().getMaxPlayerCount(), tpsLast10Secs, getUptime())));
                            }
                        }
                    }
                } catch (InterruptedException e) {
                }
            }, "Fabricord update status thread");
            thread.start();
        }

        public static String getUptime() {
            Date time2 = new Date();
            long ms = (time2.getTime() - startTime.getTime());
            long secs = ms / 1000;
            //ms -= secs * 1000;
            long mins = secs / 60;
            secs -= mins * 60;
            long hours = mins / 60;
            mins -= hours * 60;
            return String.format("%2d:%02d:%02d", hours, mins, secs);
        }
    }

    public static class GetServerStoppingEvent implements ServerLifecycleEvents.ServerStopping {
        @Override
        public void onServerStopping(MinecraftServer server) {
            if (ServerInitializer.config.isPromptsEnabled()) {
                serverStoppingMethod();
            }
        }
    }

    public static class GetServerStoppedEvent implements ServerLifecycleEvents.ServerStopped {
        @Override
        public void onServerStopped(MinecraftServer server) {
            thread.interrupt();
            if (ServerInitializer.config.isPromptsEnabled()) {
                serverStoppedMethod();
            }
            ServerInitializer.stopDiscordBot();
        }
    }
}
