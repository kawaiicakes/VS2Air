package io.github.kawaiicakes.vs2air.forge;

import io.github.kawaiicakes.vs2air.VS2Air;
import net.minecraftforge.fml.common.Mod;

@Mod(VS2Air.MOD_ID)
public final class VS2AirForge {
    public VS2AirForge() {
        VS2Air.init();
    }
}
