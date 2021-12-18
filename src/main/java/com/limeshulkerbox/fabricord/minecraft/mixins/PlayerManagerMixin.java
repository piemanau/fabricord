package com.limeshulkerbox.fabricord.minecraft.mixins;

import com.limeshulkerbox.fabricord.api.v1.API;
import com.limeshulkerbox.fabricord.minecraft.GetPlayersInterface;
import com.limeshulkerbox.fabricord.minecraft.ServerInitializer;
import com.limeshulkerbox.fabricord.other.UUIDConverter;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.limeshulkerbox.fabricord.minecraft.ServerInitializer.config;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements GetPlayersInterface {

    @Shadow public abstract List<ServerPlayerEntity> getPlayerList();

    private static Text previousMessage = null;
    private static String previousKey = null;

    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At(value = "HEAD"))
    public void getMessage(Text message, MessageType type, UUID sender, CallbackInfo ci) throws IOException {
        String key = "this.cannot.be.blank";
        boolean sendToDiscord = true;
        if (message instanceof TranslatableText translatable) {
            key = translatable.getKey();
            if (previousMessage == null) {
                previousMessage = Text.of("LimeShulkerBox is cool");
            }

            if (previousMessage.equals(message) && key.equals(previousKey)) {
                sendToDiscord = false;
            }

            previousKey = key;
            previousMessage = message;
        }

        if (!sender.equals(ServerInitializer.modUUID) && sendToDiscord || API.checkIfSomethingIsPresent(config.getKeysToSendToDiscord(), key)) {
            if (config.getChatEnabled()) {
                API.sendMessage(message.getString(), false, true, false);
            }
            if (!sender.equals(Util.NIL_UUID) && !sender.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                if (config.getWebhooksEnabled()) {
                    String message1 = message.getString();
                    API.sendMessageToDiscordWebhook(message1.replaceFirst("<.+?> ", ""), UUIDConverter.getName(sender), "https://crafatar.com/avatars/" + sender + "?&overlay", config.getWebhookURL());
                }
            }
        }
    }

    @Override
    public String getPlayers() {
        ArrayList<String> playerNames = new ArrayList<String>();
        int playerAmount = 0;
        for (int i = 0; i < getPlayerList().size(); i++) {
            playerNames.add(getPlayerList().get(i).getName().getString());
            playerAmount = i + 1;
        }
        String formattedString = "Player count: " + playerAmount + " Players online: ";
        for (int i = 0; i < playerNames.size(); i++) {
            formattedString += getPlayerList().get(i).getName().getString();
            if (i < playerNames.size() - 1) {
                formattedString += ", ";
            }
        }
        return formattedString;
    }
}