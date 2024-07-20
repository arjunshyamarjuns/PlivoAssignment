package com.plivo.Scenario2;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.load();
    private static String overrideApiKey;

    public static String getApiKey() {
        return overrideApiKey != null ? overrideApiKey : dotenv.get("OPENWEATHER_API_KEY");
    }

    public static void setOverrideApiKey(String apiKey) {
        overrideApiKey = apiKey;
    }

    public static void clearOverrideApiKey() {
        overrideApiKey = null;
    }
}
