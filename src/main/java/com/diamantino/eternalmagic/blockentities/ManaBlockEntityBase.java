package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.api.mana.IManaStorage;
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

    private LazyOptional<IManaStorage> lazyManaHandler = LazyOptional.empty();

    public ManaBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, long capacity, long maxTransfer) {
        this(pType, pPos, pBlockState, capacity, maxTransfer, maxTransfer);
    }

    public ManaBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, long capacity, long maxReceive, long maxExtract) {
        super(pType, pPos, pBlockState);

        this.manaStorage = new ModManaStorage(capacity, maxReceive, maxExtract) {
            @Override
            public void onManaChanged() {
                setChanged();

                syncMana();
            }
        };
    }

    public void syncMana() {
        ModMessages.sendToClients(new ManaSyncS2CPacket(this.manaStorage.getManaStored(), getBlockPos()));
    }

    public IManaStorage getManaStorage() {
        return manaStorage;
    }

    public void setManaLevel(long mana) {
        this.manaStorage.setMana(mana);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("manaStorage", manaStorage.serializeNBT());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        manaStorage.deserializeNBT(nbt.get("manaStorage"));

        super.load(nbt);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.mana) {
            return ModCapabilities.mana.orEmpty(cap, lazyManaHandler);
        }

        return super.getCapability(cap, side);
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

    private static long extractMana(ManaBlockEntityBase blockEntity, long amount) {
        return blockEntity.manaStorage.extractMana(amount, false);
    }

    private static long receiveMana(ManaBlockEntityBase blockEntity, long amount) {
        return blockEntity.manaStorage.receiveMana(amount, false);
    }

    private static boolean hasEnoughMana(ManaBlockEntityBase blockEntity, long requiredMana) {
        return blockEntity.manaStorage.getManaStored() >= requiredMana;
    }
}
