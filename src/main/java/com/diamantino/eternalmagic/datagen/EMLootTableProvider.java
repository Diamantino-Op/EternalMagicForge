package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.registration.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EMLootTableProvider extends LootTableProvider {
    public EMLootTableProvider(PackOutput pOutput) {
        super(pOutput, Collections.emptySet(), List.of(BlockTagsSubProvider.getEntry()));
    }

    public static class BlockTagsSubProvider extends BlockLootSubProvider {
        public BlockTagsSubProvider() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        public static SubProviderEntry getEntry() {
            return new SubProviderEntry(BlockTagsSubProvider::new, LootContextParamSets.BLOCK);
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getEntries().stream()
                    .filter(e -> e.getKey().location().getNamespace().equals(ModReferences.modId))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }

        @Override
        public void generate() {
            for (RegistryObject<? extends Block> block : ModBlocks.functionalBlocks.values()) {
                dropSelf(block.get());
            }

            for (RegistryObject<? extends Block> block : ModBlocks.decorativeBlocks.values()) {
                dropSelf(block.get());
            }
        }
    }
}
