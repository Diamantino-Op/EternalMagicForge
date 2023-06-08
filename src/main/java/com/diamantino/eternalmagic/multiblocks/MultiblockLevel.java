package com.diamantino.eternalmagic.multiblocks;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MultiblockLevel implements BlockAndTintGetter {
    private final Level level;
    private final Long2ObjectMap<BlockState> states = new Long2ObjectArrayMap<>();

    public MultiblockLevel(Level level, List<StructureTemplate.StructureBlockInfo> blockInfos) {
        this.level = level;

        for (StructureTemplate.StructureBlockInfo blockInfo : blockInfos) {
            states.put(blockInfo.pos().asLong(), blockInfo.state());
        }
    }

    @Override
    public float getShade(@NotNull Direction pDirection, boolean pShade)
    {
        return level.getShade(pDirection, pShade);
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine()
    {
        return level.getLightEngine();
    }

    @Override
    public int getBrightness(@NotNull LightLayer pLightType, @NotNull BlockPos pBlockPos) {
        return 15;
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pPos, @NotNull ColorResolver pColorResolver)
    {
        long packedPos = pPos.asLong();
        if (states.containsKey(packedPos))
        {
            return IClientFluidTypeExtensions.of(states.get(packedPos).getFluidState()).getTintColor();
        }
        return -1;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pPos)
    {
        return null;
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pPos)
    {
        return states.getOrDefault(pPos.asLong(), Blocks.AIR.defaultBlockState());
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pPos)
    {
        return states.getOrDefault(pPos.asLong(), Blocks.AIR.defaultBlockState()).getFluidState();
    }

    @Override
    public int getHeight()
    {
        return level.getHeight();
    }

    @Override
    public int getMinBuildHeight()
    {
        return level.getMinBuildHeight();
    }
}
