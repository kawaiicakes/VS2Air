package io.github.kawaiicakes.vs2air.forge;

import io.github.kawaiicakes.vs2air.VS2Air;
import io.github.kawaiicakes.vs2air.block.ImmortalAirBlock;
import io.github.kawaiicakes.vs2air.block.StandardAirBlock;
import io.github.kawaiicakes.vs2air.block.VS2AirBlock;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

@Mod(VS2Air.MOD_ID)
public final class VS2AirForge {
    public static final CreativeModeTab FORGE_AIR_TAB = new CreativeModeTab("vs2air") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return IMMORTAL_AIR_ITEM.get().getDefaultInstance();
        }
    };

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VS2Air.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VS2Air.MOD_ID);

    public static final RegistryObject<Block> IMMORTAL_AIR_BLOCK = BLOCKS.register(
            "immortal",
            () -> new ImmortalAirBlock(VS2AirBlock.defaultProperties())
    );

    public static final RegistryObject<Item> IMMORTAL_AIR_ITEM = ITEMS.register(
            "immortal",
            () -> new BlockItem(IMMORTAL_AIR_BLOCK.get(), new Item.Properties().tab(FORGE_AIR_TAB).rarity(Rarity.EPIC))
    );

    public static final RegistryObject<Block> STANDARD_AIR_BLOCK = BLOCKS.register(
            "standard",
            () -> new StandardAirBlock(VS2AirBlock.defaultProperties())
    );

    public static final RegistryObject<Item> STANDARD_AIR_ITEM = ITEMS.register(
            "standard",
            () -> new BlockItem(STANDARD_AIR_BLOCK.get(), new Item.Properties().tab(FORGE_AIR_TAB).rarity(Rarity.EPIC))
    );

    public VS2AirForge() {
        VS2Air.init();
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
