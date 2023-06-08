package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.registration.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class EMBlockStateProvider extends BlockStateProvider {

    public EMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModConstants.modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<? extends Block> block : ModBlocks.simpleBlocks) {
            if (block.getId().getPath().contains("_slab")) {
                this.slabBlock((SlabBlock) block.get(), new ResourceLocation(block.getId().getNamespace(), block.getId().getPath().replace("_slab", "")), blockTexture(block.get()));
            } else if (block.getId().getPath().contains("_pillar")) {
                this.axisBlock((RotatedPillarBlock) block.get(), blockTexture(block.get()));
            } else if (block.getId().getPath().contains("_stairs")) {
                this.stairsBlock((StairBlock) block.get(), blockTexture(block.get()));
            } else if (block.getId().getPath().contains("_wall")) {
                this.wallBlock((WallBlock) block.get(), blockTexture(block.get()));
            } else if (block.getId().getPath().contains("shrine_output")) {
                this.directionalBlock(block.get(),
                        blockTexture(block.get()),
                        blockTexture(block.get()),
                        new ResourceLocation(blockTexture(block.get()).getNamespace(), blockTexture(block.get()).getPath() + "_face"),
                        blockTexture(block.get()),
                        blockTexture(block.get()),
                        blockTexture(block.get())
                );
            } else {
                this.simpleBlock(block.get());
            }
        }
    }

    private void directionalBlock(Block block, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(models().cube(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), up, down, north, south, east, west).texture("particle", north)).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(models().cube(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), up, down, north, south, east, west).texture("particle", north)).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(models().cube(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), up, down, north, south, east, west).texture("particle", north)).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(models().cube(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), up, down, north, south, east, west).texture("particle", north)).rotationY(270).addModel();
    }
}
