package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.blocks.WandBenchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
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

    public static final List<RegistryObject<? extends Block>> simpleBlocks = new ArrayList<>();

    public static final RegistryObject<WandBenchBlock> wandBenchBlock = registerFunctionalBlock("wand_bench", false, () -> new WandBenchBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6F).lightLevel(state -> 15).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));

    private static void registerSimpleDecorativeBlocksSet(String regName, Material material, float destroyTime, float explosionResistance) {
        RegistryObject<Block> block = registerDecorativeBlock(regName + "_block", true, () -> new Block(BlockBehaviour.Properties.of(material).strength(destroyTime, explosionResistance).requiresCorrectToolForDrops()));
        registerDecorativeBlock(regName + "_bricks", true, () -> new Block(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_slab", true, () -> new SlabBlock(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_pillar", true, () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_stairs", true, () -> new StairBlock(() -> block.get().defaultBlockState(), BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_wall", true, () -> new WallBlock(BlockBehaviour.Properties.copy(block.get())));
    }

    private static <T extends Block> RegistryObject<T> registerFunctionalBlock(String name, boolean isSimple, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        functionalBlocks.add(toReturn);
        registerBlockItem(name, toReturn);

        if (isSimple) simpleBlocks.add(toReturn);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerDecorativeBlock(String name, boolean isSimple, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        decorativeBlocks.add(toReturn);
        registerBlockItem(name, toReturn);

        if (isSimple) simpleBlocks.add(toReturn);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerResourceBlock(String name, boolean isSimple, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        resourcesBlocks.add(toReturn);
        registerBlockItem(name, toReturn);

        if (isSimple) simpleBlocks.add(toReturn);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<BlockItem> registerBlockItem(String name, RegistryObject<T> block) {
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

        registerSimpleDecorativeBlocksSet("mana_stone", Material.STONE, 1.5F, 6F);
    }
}
