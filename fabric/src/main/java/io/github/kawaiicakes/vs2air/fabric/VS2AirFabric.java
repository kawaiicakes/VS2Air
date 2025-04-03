package io.github.kawaiicakes.vs2air.fabric;

import net.fabricmc.api.ModInitializer;

import io.github.kawaiicakes.vs2air.VS2Air;

public final class VS2AirFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VS2Air.init();
    }
}
