package com.example.tmmextension;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AreaConfigurationReloader implements SimpleResourceReloadListener<AreaConfiguration> {
    private static final Identifier CONFIGURATION_FILE_ID = Identifier.of("trainmurdermystery", "tmm_areas/custom_areas.json");

    @Override
    public CompletableFuture<AreaConfiguration> load(ResourceManager manager, Profiler profiler, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<net.minecraft.resource.Resource> resources = manager.getAllResources(CONFIGURATION_FILE_ID);
                Tmmdatapackextension.LOGGER.info("Found {} custom area configuration resources", resources.size());
                if (!resources.isEmpty()) {
                    net.minecraft.resource.Resource resource = resources.get(0); // 使用 get(0) 替代 getFirst()
                    Tmmdatapackextension.LOGGER.info("Loading custom area configuration from datapack");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                        JsonElement jsonElement = JsonParser.parseReader(reader);
                        Tmmdatapackextension.LOGGER.info("JSON content: {}", jsonElement.toString());
                        DataResult<AreaConfiguration> result = AreaConfiguration.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                        return result.resultOrPartial(errorMsg -> {
                            Tmmdatapackextension.LOGGER.error("Failed to parse area configuration: {}", errorMsg);
                        }).orElse(null);
                    }
                } else {
                    Tmmdatapackextension.LOGGER.info("No custom area configuration resources found");
                }
            } catch (IOException e) {
                Tmmdatapackextension.LOGGER.error("Failed to load area configuration: " + e.getMessage(), e);
            }
            return null;
        }, executor);
    }

    @Override
    public CompletableFuture<Void> apply(AreaConfiguration configuration, ResourceManager manager, Profiler profiler, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            if (configuration != null) {
                AreaConfigurationManager.setConfiguration(configuration);
                Tmmdatapackextension.LOGGER.info("Loaded custom area configuration from datapack: {}", configuration);
            } else {
                AreaConfigurationManager.setConfiguration(null);
                Tmmdatapackextension.LOGGER.info("No custom area configuration found in datapack");
            }
        }, executor);
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(Tmmdatapackextension.MOD_ID, "area_configuration_reloader");
    }
}