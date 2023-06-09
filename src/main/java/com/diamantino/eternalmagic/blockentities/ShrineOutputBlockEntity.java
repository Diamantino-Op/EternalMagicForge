package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.blocks.ShrineOutputBlock;
import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import com.diamantino.eternalmagic.registration.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ShrineOutputBlockEntity extends BlockEntity {
    public BlockPos corePos;
    public boolean isLinked;

    public ShrineOutputBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.shrineOutputBlockEntity.get(), pPos, pBlockState);

        this.corePos = null;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        if (corePos != null) {
            nbt.putInt("coreX", corePos.getX());
            nbt.putInt("coreY", corePos.getY());
            nbt.putInt("coreZ", corePos.getZ());
        }

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        if (nbt.contains("coreX"))
            corePos = new BlockPos(nbt.getInt("coreX"), nbt.getInt("coreY"), nbt.getInt("coreZ"));

        super.load(nbt);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (this.getLevel() != null) {
            Optional<Direction> facing = this.getLevel().getBlockState(this.getBlockPos()).getOptionalValue(ShrineOutputBlock.facing);

            if (facing.isPresent()) {
                if (side == facing.get() && corePos != null && this.getLevel().getBlockEntity(this.getBlockPos()) instanceof ShrineCoreBlockEntity shrineCoreBlockEntity) {
                    return ModCapabilities.mana.orEmpty(cap, shrineCoreBlockEntity.lazyManaHandler);
                }
            }
        }

        return super.getCapability(cap, side);
    }
}
