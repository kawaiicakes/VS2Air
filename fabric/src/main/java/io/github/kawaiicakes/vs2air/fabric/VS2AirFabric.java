package io.github.kawaiicakes.vs2air.fabric;

import net.fabricmc.api.ModInitializer;

import io.github.kawaiicakes.vs2air.VS2Air;

public final class VS2AirFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        VS2Air.init();
    }
}
