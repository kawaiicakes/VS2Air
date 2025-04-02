package io.github.kawaiicakes.vs2air.forge;

import net.minecraftforge.fml.common.Mod;

import io.github.kawaiicakes.vs2air.VS2Air;

@Mod(VS2Air.MOD_ID)
public final class VS2AirForge {
    public VS2AirForge() {
        // Run our common setup.
        VS2Air.init();
    }
}
