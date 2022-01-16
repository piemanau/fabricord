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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.config;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    Thread thread;

    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At(value = "TAIL"))
    public void getMessage(Text message, MessageType type, UUID sender, CallbackInfo ci) throws IOException {
        thread = new Thread(() ->
        {
            try {
                if (!sender.equals(ServerInitializer.modUUID)) {
                    String key = "this.cannot.be.blank";
                    if (message instanceof TranslatableText translatableText) {
                        key = translatableText.getKey();
                    }
                    if (API.checkIfSomethingIsPresent(config.getKeysToSendToDiscord(), key)) {
                        if (!config.isOnlyWebhooks()) {
                            if (config.isChatEnabled()) {
                                API.sendMessageToDiscordChat(message.getString());
                                thread.interrupt();
                            }
                            if (config.isWebhooksEnabled()) {
                                String message1 = message.getString();
                                String[] message2 = message1.split(" ");
                                String message4 = "";
                                for (int i = 0; i < message2.length; i++) {
                                    message4 += message2[i] + " ";
                                }
                                String message3 = message4.replaceFirst(message2[0], "");
                                API.sendMessageToDiscordWebhook(message3, message2[0], "https://crafatar.com/avatars/" + UUIDConverter.getUUID(message2[0]) + "?&overlay", config.getWebhookURL());
                                thread.interrupt();
                            }
                        } else {
                            if (config.isWebhooksEnabled()) {
                                String message1 = message.getString();
                                String[] message2 = message1.split(" ");
                                String message4 = "";
                                for (int i = 0; i < message2.length; i++) {
                                    message4 += message2[i] + " ";
                                }
                                String message3 = message4.replaceFirst(message2[0], "");
                                API.sendMessageToDiscordWebhook(message3, message2[0], "https://crafatar.com/avatars/" + UUIDConverter.getUUID(message2[0]) + "?&overlay", config.getWebhookURL());
                                thread.interrupt();
                            } else if (config.isChatEnabled()) {
                                API.sendMessageToDiscordChat(message.getString());
                                thread.interrupt();
                            }
                        }
                    } else if (!key.equals("this.cannot.be.blank")) {
                        System.out.println("The key: " + key + " is not present in the config. This message is from PlayerManagerMixin from Fabricord.");
                        }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Fabricord get message");
        thread.start();
    }
}