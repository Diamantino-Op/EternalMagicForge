package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.blocks.WandBenchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("UnusedReturnValue")
public class ModBlocks {
    public static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, ModReferences.modId);

    public static final List<RegistryObject<? extends Block>> functionalBlocks = new ArrayList<>();
    public static final List<RegistryObject<? extends Block>> decorativeBlocks = new ArrayList<>();
    public static final List<RegistryObject<? extends Block>> resourcesBlocks = new ArrayList<>();

    public static final RegistryObject<WandBenchBlock> wandBenchBlock = registerFunctionalBlock("wand_bench", () -> new WandBenchBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6, 6).lightLevel(state -> 15).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));

    private static <T extends Block> RegistryObject<T> registerFunctionalBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        functionalBlocks.add(toReturn);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerDecorativeBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        decorativeBlocks.add(toReturn);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerResourceBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        resourcesBlocks.add(toReturn);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.modItems.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static Boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> type) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    public static void registerBlocks(IEventBus bus) {
        blocks.register(bus);
    }
}
