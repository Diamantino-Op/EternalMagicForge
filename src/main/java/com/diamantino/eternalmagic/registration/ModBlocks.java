package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.blocks.WandBenchBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("UnusedReturnValue")
public class ModBlocks {
    public static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, ModReferences.modId);

    public static RegistryObject<WandBenchBlock> wandBenchBlock = registerBlock("wand_bench", () -> new WandBenchBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6, 6).lightLevel(state -> 15).requiresCorrectToolForDrops()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBI(String name, Supplier<T> block) {
        return blocks.register(name, block);
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void registerBlocks(IEventBus bus) {
        blocks.register(bus);
    }
}
