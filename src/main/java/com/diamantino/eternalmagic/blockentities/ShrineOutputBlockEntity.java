package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ShrineOutputBlockEntity extends BlockEntity {
    public BlockPos corePos;
    public boolean isLinked;

    public ShrineOutputBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.shrineOutputBlockEntity.get(), pPos, pBlockState);

        this.corePos = new BlockPos(0, 0, 0);
        this.isLinked = false;
    }

    public long extractMana(long amount, boolean simulate) {
        if (isLinked && level != null && !level.isClientSide() && level.getBlockEntity(corePos) instanceof ShrineCoreBlockEntity shrineCoreBlockEntity) {
            return shrineCoreBlockEntity.extractMana(amount, simulate);
        }

        return 0;
    }

    public long receiveMana(long amount, boolean simulate) {
        if (isLinked && level != null && !level.isClientSide() && level.getBlockEntity(corePos) instanceof ShrineCoreBlockEntity shrineCoreBlockEntity) {
            return shrineCoreBlockEntity.receiveMana(amount, simulate);
        }

        return 0;
    }

    public boolean hasEnoughMana(long requiredMana) {
        if (isLinked && level != null && !level.isClientSide() && level.getBlockEntity(corePos) instanceof ShrineCoreBlockEntity shrineCoreBlockEntity) {
            return shrineCoreBlockEntity.hasEnoughMana(requiredMana);
        }

        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("coreX", corePos.getX());
        nbt.putInt("coreY", corePos.getY());
        nbt.putInt("coreZ", corePos.getZ());
        nbt.putBoolean("linked", isLinked);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        corePos = new BlockPos(nbt.getInt("coreX"), nbt.getInt("coreY"), nbt.getInt("coreZ"));
        isLinked = nbt.getBoolean("linked");

        super.load(nbt);
    }
}
