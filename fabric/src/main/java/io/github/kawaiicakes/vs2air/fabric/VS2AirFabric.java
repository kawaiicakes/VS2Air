package io.github.kawaiicakes.vs2air.fabric;

import io.github.kawaiicakes.vs2air.block.StandardAirBlock;
import io.github.kawaiicakes.vs2air.block.VS2AirBlock;
import net.fabricmc.api.ModInitializer;

import io.github.kawaiicakes.vs2air.VS2Air;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class VS2AirFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VS2Air.init();

        Block block = new StandardAirBlock(VS2AirBlock.defaultProperties());
        Registry.register(Registry.BLOCK, new ResourceLocation("vs2air", "standard"), block);
        Registry.register(Registry.ITEM, new ResourceLocation("vs2air", "standard"), new BlockItem(block, new Item.Properties()));

    }
}
