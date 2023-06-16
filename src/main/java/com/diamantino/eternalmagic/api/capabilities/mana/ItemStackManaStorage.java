package com.diamantino.eternalmagic.api.capabilities.mana;

import com.diamantino.eternalmagic.storage.mana.ModManaStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class ItemStackManaStorage implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<ItemStackManaStorage> itemStackManaStorageCapability = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<ItemStackManaStorage> lazyManaHandler;

    private final ModManaStorage manaStorage;

    public ItemStackManaStorage(long baseCapacity, long maxReceive, long maxExtract) {
        lazyManaHandler = LazyOptional.of(() -> this);

        manaStorage = new ModManaStorage(baseCapacity, maxReceive, maxExtract) {
            @Override
            public void onManaChanged() {

            }
        };
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

    public long getCapacity() {
        return this.manaStorage.getMaxManaStored();
    }

    public long getManaLevel() {
        return this.manaStorage.getManaStored();
    }

    public void setManaLevel(long mana) {
        this.manaStorage.setMana(mana);
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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ItemStackManaStorage.itemStackManaStorageCapability) {
            return ItemStackManaStorage.itemStackManaStorageCapability.orEmpty(cap, lazyManaHandler);
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("manaStorage", manaStorage.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        manaStorage.deserializeNBT(nbt.getCompound("manaStorage"));
    }
}
