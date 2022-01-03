package com.limeshulkerbox.fabricord.other;

/*
 *   Code copied from https://github.com/ProjectET/UUIDLookup/blob/master/src/main/java/io/github/projectet/uuidlookup/util/HTTPGet.java
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class UUIDConverter {
    private static JsonObject jsonObject;

    private static void sendGET(String urlinput) throws IOException {
        URL url = new URL("https://playerdb.co/api/player/minecraft/" + urlinput);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        JsonStreamParser streamParser = new JsonStreamParser(new InputStreamReader(connection.getInputStream()));
        jsonObject = (JsonObject) streamParser.next();
    }

    public static String getName(UUID uuid) throws IOException {
        sendGET(uuid.toString());
        return jsonObject.get("data").getAsJsonObject().get("player").getAsJsonObject().get("username").getAsString();
    }

    public static UUID getUUID(String name) throws IOException {
        sendGET(name);
        return UUID.fromString(jsonObject.get("data").getAsJsonObject().get("player").getAsJsonObject().get("id").getAsString());
    }
}
