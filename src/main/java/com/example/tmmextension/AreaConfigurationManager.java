package com.example.tmmextension;

import org.jetbrains.annotations.Nullable;

public class AreaConfigurationManager {
    private static AreaConfiguration currentConfiguration = null;

    public static void setConfiguration(AreaConfiguration configuration) {
        currentConfiguration = configuration;
    }

    @Nullable
    public static AreaConfiguration getCurrentConfiguration() {
        return currentConfiguration;
    }

    public static boolean hasConfiguration() {
        return currentConfiguration != null;
    }
}