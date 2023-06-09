package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.blockentities.ManaPipeBlockEntity;
import com.diamantino.eternalmagic.blockentities.ShrineCoreBlockEntity;
import com.diamantino.eternalmagic.blockentities.ShrineOutputBlockEntity;
import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("DataFlowIssue")
public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> blockEntityTypes = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModConstants.modId);

    public static final RegistryObject<BlockEntityType<WandBenchBlockEntity>> wandBenchBlockEntity = blockEntityTypes.register("wand_bench", () -> BlockEntityType.Builder.of(WandBenchBlockEntity::new, ModBlocks.wandBenchBlock.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShrineCoreBlockEntity>> shrineCoreBlockEntity = blockEntityTypes.register("shrine_core", () -> BlockEntityType.Builder.of(ShrineCoreBlockEntity::new, ModBlocks.shrineCoreBlock.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShrineOutputBlockEntity>> shrineOutputBlockEntity = blockEntityTypes.register("shrine_output", () -> BlockEntityType.Builder.of(ShrineOutputBlockEntity::new, ModBlocks.shrineOutputBlock.get()).build(null));
    public static final RegistryObject<BlockEntityType<ManaPipeBlockEntity>> manaPipeBlockEntity = blockEntityTypes.register("mana_pipe", () -> BlockEntityType.Builder.of(ManaPipeBlockEntity::new, ModBlocks.manaPipeBlock.get()).build(null));

    public static void registerBlockEntityTypes(IEventBus bus) {
        blockEntityTypes.register(bus);
    }
}
