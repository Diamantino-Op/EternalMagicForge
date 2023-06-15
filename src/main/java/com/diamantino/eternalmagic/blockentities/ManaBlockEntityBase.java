package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.api.mana.IManaStorage;
import com.diamantino.eternalmagic.api.mana.ManaStorage;
import com.diamantino.eternalmagic.networking.s2c.ManaSyncS2CPacket;
import com.diamantino.eternalmagic.registration.ModCapabilities;
import com.diamantino.eternalmagic.registration.ModMessages;
import com.diamantino.eternalmagic.storage.mana.ModManaStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ManaBlockEntityBase extends BlockEntity {
    public final ModManaStorage manaStorage;

    public LazyOptional<IManaStorage> lazyManaHandler = LazyOptional.empty();

    public ManaBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, long capacity) {
        this(pType, pPos, pBlockState, capacity, capacity, capacity);
    }

    public ManaBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, long capacity, long maxTransfer) {
        this(pType, pPos, pBlockState, capacity, maxTransfer, maxTransfer);
    }

    public ManaBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, long capacity, long maxReceive, long maxExtract) {
        super(pType, pPos, pBlockState);

        this.manaStorage = new ModManaStorage(capacity, maxReceive, maxExtract) {
            @Override
            public void onManaChanged() {
                setChanged();

                if (level != null && !level.isClientSide()) {
                    syncMana();
                }
            }
        };
    }

    public void syncMana() {
        ModMessages.sendToClients(new ManaSyncS2CPacket(this.manaStorage.getManaStored(), this.manaStorage.getMaxReceive(), this.manaStorage.getMaxExtract(), this.manaStorage.getCapacity(), this.getBlockPos()));
    }

    public IManaStorage getManaStorage() {
        return manaStorage;
    }

    public void setManaLevel(long mana) {
        this.manaStorage.setMana(mana);
    }

    public void setCapacity(long capacity) {
        this.manaStorage.setCapacity(capacity);
    }

    public void setMaxTransfer(long maxTransfer) {
        this.manaStorage.setMaxTransfer(maxTransfer);
    }

    public void setMaxReceive(long maxReceive) {
        this.manaStorage.setMaxReceive(maxReceive);
    }

    public void setMaxExtract(long maxExtract) {
        this.manaStorage.setMaxExtract(maxExtract);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("manaStorage", manaStorage.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        manaStorage.deserializeNBT(nbt.get("manaStorage"));

        syncMana();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.mana && supportManaCap(side)) {
            return ModCapabilities.mana.orEmpty(cap, lazyManaHandler);
        }

        return super.getCapability(cap, side);
    }

    private boolean supportManaCap(@Nullable Direction side) {
        if (side != null) {
            return switch (side) {
                case DOWN -> manaStorage.bottomFace != ManaStorage.SideInfo.none;
                case UP -> manaStorage.topFace != ManaStorage.SideInfo.none;
                case NORTH -> manaStorage.northFace != ManaStorage.SideInfo.none;
                case SOUTH -> manaStorage.southFace != ManaStorage.SideInfo.none;
                case WEST -> manaStorage.westFace != ManaStorage.SideInfo.none;
                case EAST -> manaStorage.eastFace != ManaStorage.SideInfo.none;
            };
        }

        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        lazyManaHandler = LazyOptional.of(() -> manaStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        lazyManaHandler.invalidate();
    }

    public long extractMana(Direction direction, long amount, boolean simulate) {
        return this.manaStorage.extractMana(direction, amount, simulate);
    }

    public long receiveMana(Direction direction, long amount, boolean simulate) {
        return this.manaStorage.receiveMana(direction, amount, simulate);
    }

    public boolean hasEnoughMana(long requiredMana) {
        return this.manaStorage.getManaStored() >= requiredMana;
    }
}
