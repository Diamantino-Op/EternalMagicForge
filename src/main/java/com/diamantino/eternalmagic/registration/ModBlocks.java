package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.blocks.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("UnusedReturnValue")
public class ModBlocks {
    public static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, ModConstants.modId);

    public static final Map<String, RegistryObject<? extends Block>> functionalBlocks = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<? extends Block>> decorativeBlocks = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<? extends Block>> resourcesBlocks = new LinkedHashMap<>();

    public static final List<RegistryObject<? extends Block>> simpleBlocks = new ArrayList<>();

    public static final Map<ResourceLocation, MineTool> mineToolMap = new LinkedHashMap<>();
    public static final Map<ResourceLocation, MineLevel> mineLevelMap = new LinkedHashMap<>();

    public static final RegistryObject<WandBenchBlock> wandBenchBlock = registerFunctionalBlock("wand_bench", false, MineTool.pickaxe, MineLevel.wood, () -> new WandBenchBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).lightLevel(state -> 15).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<ShrineCoreBlock> shrineCoreBlock = registerFunctionalBlock("shrine_core", false, MineTool.pickaxe, MineLevel.iron, () -> new ShrineCoreBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).lightLevel(state -> 15).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<ShrineOutputBlock> shrineOutputBlock = registerFunctionalBlock("shrine_output", true, MineTool.pickaxe, MineLevel.iron, () -> new ShrineOutputBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).requiresCorrectToolForDrops()));
    public static final RegistryObject<ManaPipeBlock> manaPipeBlock = registerFunctionalBlock("mana_pipe", false, MineTool.pickaxe, MineLevel.iron, () -> new ManaPipeBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<ManaTunnelBlock> manaTunnelBlock = registerFunctionalBlock("mana_tunnel", false, MineTool.pickaxe, MineLevel.iron, () -> new ManaTunnelBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<ManaReceiverBlock> manaReceiverBlock = registerFunctionalBlock("mana_receiver", false, MineTool.pickaxe, MineLevel.iron, () -> new ManaReceiverBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<DirectionalManaTransmitterBlock> directionalManaTransmitterBlock = registerFunctionalBlock("directional_mana_transmitter", false, MineTool.pickaxe, MineLevel.iron, () -> new DirectionalManaTransmitterBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));
    public static final RegistryObject<OmnidirectionalManaTransmitterBlock> omnidirectionalManaTransmitterBlock = registerFunctionalBlock("omnidirectional_mana_transmitter", false, MineTool.pickaxe, MineLevel.iron, () -> new OmnidirectionalManaTransmitterBlock(BlockBehaviour.Properties.of().strength(1.5F, 6F).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(ModBlocks::never).isRedstoneConductor(ModBlocks::never).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never)));

    private static void registerSimpleDecorativeBlocksSet(String regName, MineTool tool, MineLevel mineLevel, float destroyTime, float explosionResistance) {
        RegistryObject<Block> block = registerDecorativeBlock(regName, true, tool, mineLevel, () -> new Block(BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance).requiresCorrectToolForDrops()));
        registerDecorativeBlock(regName + "_bricks", true, tool, mineLevel, () -> new Block(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_bricks_slab", true, tool, mineLevel, () -> new SlabBlock(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_slab", true, tool, mineLevel, () -> new SlabBlock(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_pillar", true, tool, mineLevel, () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_bricks_stairs", true, tool, mineLevel, () -> new StairBlock(() -> block.get().defaultBlockState(), BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_stairs", true, tool, mineLevel, () -> new StairBlock(() -> block.get().defaultBlockState(), BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_bricks_wall", true, tool, mineLevel, () -> new WallBlock(BlockBehaviour.Properties.copy(block.get())));
        registerDecorativeBlock(regName + "_wall", true, tool, mineLevel, () -> new WallBlock(BlockBehaviour.Properties.copy(block.get())));
    }

    private static <T extends Block> RegistryObject<T> registerFunctionalBlock(String name, boolean isSimple, MineTool tool, MineLevel mineLevel, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        functionalBlocks.put(name, toReturn);
        registerBlockItem(name, toReturn);

        mineToolMap.put(toReturn.getId(), tool);
        mineLevelMap.put(toReturn.getId(), mineLevel);

        if (isSimple) simpleBlocks.add(toReturn);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerDecorativeBlock(String name, boolean isSimple, MineTool tool, MineLevel mineLevel, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        decorativeBlocks.put(name, toReturn);
        registerBlockItem(name, toReturn);

        mineToolMap.put(toReturn.getId(), tool);
        mineLevelMap.put(toReturn.getId(), mineLevel);

        if (isSimple) simpleBlocks.add(toReturn);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerResourceBlock(String name, boolean isSimple, MineTool tool, MineLevel mineLevel, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);
        resourcesBlocks.put(name, toReturn);
        registerBlockItem(name, toReturn);

        mineToolMap.put(toReturn.getId(), tool);
        mineLevelMap.put(toReturn.getId(), mineLevel);

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

        registerSimpleDecorativeBlocksSet("mana_stone", MineTool.pickaxe, MineLevel.stone, 1.5F, 6F);
        registerSimpleDecorativeBlocksSet("dark_stone", MineTool.pickaxe, MineLevel.iron, 1.5F, 6F);
    }

    public enum MineTool {
        none,
        pickaxe,
        axe,
        shovel,
        hoe
    }

    public enum MineLevel {
        none,
        wood,
        gold,
        stone,
        iron,
        diamond,
        netherite
    }
}
