package com.diamantino.eternalmagic.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ShrineOutputBlockEntity extends ManaBlockEntityBase {
    public BlockPos corePos;

    public ShrineOutputBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, long capacity, long maxTransfer) {
        super(pType, pPos, pBlockState, capacity, maxTransfer);

        this.corePos = new BlockPos(0, 0, 0);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("coreX", corePos.getX());
        nbt.putInt("coreY", corePos.getY());
        nbt.putInt("coreZ", corePos.getZ());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        corePos = new BlockPos(nbt.getInt("coreX"), nbt.getInt("coreY"), nbt.getInt("coreZ"));

        super.load(nbt);
    }
}
