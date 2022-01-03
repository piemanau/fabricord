package com.limeshulkerbox.fabricord.minecraft.mixins;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import com.limeshulkerbox.fabricord.other.UUIDConverter;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.UUID;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.config;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At(value = "HEAD"))
    public void getMessage(Text message, MessageType type, UUID sender, CallbackInfo ci) throws IOException {
        if (!sender.equals(ServerInitializer.modUUID)) {
            String key = "this.cannot.be.blank";
            if (message instanceof TranslatableText translatableText) {
                key = translatableText.getKey();
            }
            if (API.checkIfSomethingIsPresent(config.getKeysToSendToDiscord(), key)) {
                if (config.getChatEnabled()) {
                    API.sendMessageToDiscordChat(message.getString());
                } else if (config.getWebhooksEnabled()) {
                    String message1 = message.getString();
                    API.sendMessageToDiscordWebhook(message1.replaceFirst("<.+?> ", ""), UUIDConverter.getName(sender), "https://crafatar.com/avatars/" + sender + "?&overlay", config.getWebhookURL());
                }
            }
        }
    }
}