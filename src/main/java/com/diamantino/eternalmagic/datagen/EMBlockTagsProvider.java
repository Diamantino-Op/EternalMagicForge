package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.registration.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMBlockTagsProvider extends BlockTagsProvider implements DataProvider {
    public EMBlockTagsProvider(PackOutput output, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor()), ModConstants.modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        for (RegistryObject<? extends Block> block : ModBlocks.decorativeBlocks.values()) {
            if (block.getId().getPath().contains("_wall")) {
                tag(BlockTags.WALLS).add(block.get());
            }

            addBlockToMineToolTag(block.get(), ModBlocks.mineToolMap.get(block.getId()));
            addBlockToMineLevelTag(block.get(), ModBlocks.mineLevelMap.get(block.getId()));
        }

        for (RegistryObject<? extends Block> block : ModBlocks.functionalBlocks.values()) {
            addBlockToMineToolTag(block.get(), ModBlocks.mineToolMap.get(block.getId()));
            addBlockToMineLevelTag(block.get(), ModBlocks.mineLevelMap.get(block.getId()));
        }

        for (RegistryObject<? extends Block> block : ModBlocks.resourcesBlocks.values()) {
            addBlockToMineToolTag(block.get(), ModBlocks.mineToolMap.get(block.getId()));
            addBlockToMineLevelTag(block.get(), ModBlocks.mineLevelMap.get(block.getId()));
        }
    }

    private void addBlockToMineToolTag(Block block, ModBlocks.MineTool mineTool) {
        switch (mineTool) {
            case axe -> tag(BlockTags.MINEABLE_WITH_AXE).add(block);
            case shovel -> tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block);
            case pickaxe -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
            case hoe -> tag(BlockTags.MINEABLE_WITH_HOE).add(block);
        }
    }

    private void addBlockToMineLevelTag(Block block, ModBlocks.MineLevel mineLevel) {
        switch (mineLevel) {
            case wood -> tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(block);
            case gold -> tag(Tags.Blocks.NEEDS_GOLD_TOOL).add(block);
            case stone -> tag(BlockTags.NEEDS_STONE_TOOL).add(block);
            case iron -> tag(BlockTags.NEEDS_IRON_TOOL).add(block);
            case diamond -> tag(BlockTags.NEEDS_DIAMOND_TOOL).add(block);
            case netherite -> tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(block);
        }
    }
}
