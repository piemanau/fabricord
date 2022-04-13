package com.limeshulkerbox.fabricord.minecraft.mixins;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.other.UUIDConverter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.config;
import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.modUUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At(value = "TAIL"))
    public void getMessages(Text message, MessageType type, UUID sender, CallbackInfo ci) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(message.getString());
        eb.setColor(Color.TRANSLUCENT);
        if (!(sender == modUUID)) {
            if (!config.isOnlyWebhooks()) {
                if (config.isChatEnabled()) {
                    API.sendMessageToDiscordChat(message.getString());
                }
                if (config.isWebhooksEnabled()) {
                    API.sendEmbedToDiscordChat(eb);
                }
            } else {
                if (config.isWebhooksEnabled()) {
                    API.sendEmbedToDiscordChat(eb);
                } else if (config.isChatEnabled()) {
                    API.sendMessageToDiscordChat(message.getString());
                }
            }
        }
    }

    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At(value = "TAIL"))
    void getServerMessages(Text serverMessage, Function<ServerPlayerEntity, Text> playerMessageFactory, MessageType type, UUID sender, CallbackInfo ci) throws IOException {
        if (!config.isOnlyWebhooks()) {
            if (config.isChatEnabled()) {
                API.sendMessageToDiscordChat(serverMessage.getString());
            }
            if (config.isWebhooksEnabled()) {
                API.sendMessageToDiscordWebhook(serverMessage.getString().replaceFirst("<.+?> ", ""), UUIDConverter.getName(sender), "https://crafatar.com/avatars/" + sender + "?&overlay", config.getWebhookURL());
            }
        } else {
            if (config.isWebhooksEnabled()) {
                API.sendMessageToDiscordWebhook(serverMessage.getString().replaceFirst("<.+?> ", ""), UUIDConverter.getName(sender), "https://crafatar.com/avatars/" + sender + "?&overlay", config.getWebhookURL());
            } else if (config.isChatEnabled()) {
                API.sendMessageToDiscordChat(serverMessage.getString());
            }
        }
    }
}