package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.registration.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class EMBlockStateProvider extends BlockStateProvider {

    public EMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModReferences.modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<? extends Block> block : ModBlocks.simpleBlocks) {
            if (block.getId().getPath().contains("_block") || block.getId().getPath().contains("_bricks")) {
                this.simpleBlock(block.get());
            } else if (block.getId().getPath().contains("_slab")) {
                this.slabBlock((SlabBlock) block.get(), blockTexture(block.get()), blockTexture(block.get()));
            } else if (block.getId().getPath().contains("_pillar")) {
                this.axisBlock((RotatedPillarBlock) block.get(), blockTexture(block.get()));
            } else if (block.getId().getPath().contains("_stairs")) {
                this.stairsBlock((StairBlock) block.get(), blockTexture(block.get()));
            } else if (block.getId().getPath().contains("_wall")) {
                this.wallBlock((WallBlock) block.get(), blockTexture(block.get()));
            }
        }
    }
}
