package com.envyful.reforged.gts.forge.config.discord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DiscordEventManager {

    private static DiscordEvent publishHandler;
    private static DiscordEvent purchaseHandler;
    private static DiscordEvent expireHandler;
    private static DiscordEvent removeHandler;

    public static void init() {
        try {
            publishHandler = loadHandler("config/ReforgedGTS/webhooks/publisher");
            purchaseHandler = loadHandler("config/ReforgedGTS/webhooks/purchaser");
            expireHandler = loadHandler("config/ReforgedGTS/webhooks/expirer");
            removeHandler = loadHandler("config/ReforgedGTS/webhooks/remover");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiscordEvent loadHandler(String filePath) throws IOException {
        File pokemonFile = new File(filePath + "-pokemon.json");
        File itemFile = new File(filePath + "-item.json");
        String pokemonJSON = "";
        String itemJSON = "";

        if (!pokemonFile.exists() && !itemFile.exists()) {
            return new DiscordEvent();
        }

        if (pokemonFile.exists()) {
            pokemonJSON = String.join(System.lineSeparator(), Files.readAllLines(pokemonFile.toPath()));
        }

        if (itemFile.exists()) {
            itemJSON = String.join(System.lineSeparator(), Files.readAllLines(itemFile.toPath()));
        }

        return new DiscordEvent(true, pokemonJSON, itemJSON);
    }

    public static DiscordEvent getPublishHandler() {
        return publishHandler;
    }

    public static DiscordEvent getPurchaseHandler() {
        return purchaseHandler;
    }

    public static DiscordEvent getExpireHandler() {
        return expireHandler;
    }

    public static DiscordEvent getRemoveHandler() {
        return removeHandler;
    }
}
