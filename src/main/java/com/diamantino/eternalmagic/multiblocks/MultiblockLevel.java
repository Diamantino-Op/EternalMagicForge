package com.diamantino.eternalmagic.multiblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiblockLevel implements BlockAndTintGetter {
    private final Level level;
    private final BlockState state;
    private final BlockPos pos;

    public MultiblockLevel(Level level, BlockState state, BlockPos pos) {
        this.level = level;
        this.state = state;
        this.pos = pos;
    }

    @Override
    public float getShade(@NotNull Direction pDirection, boolean pShade) {
        return 0;
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return level.getLightEngine();
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pBlockPos, @NotNull ColorResolver pColorResolver) {
        return 0;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        return null;
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pPos) {
        return pos.compareTo(new Vec3i(pPos.getX(), pos.getY(), pos.getZ())) == 0 ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pPos) {
        return pos.compareTo(new Vec3i(pPos.getX(), pos.getY(), pos.getZ())) == 0 ? state.getFluidState() : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight() {
        return 128;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }
}
