package com.diamantino.eternalmagic.api.mana;

import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import com.diamantino.eternalmagic.storage.mana.ModManaStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackManaStorage  implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<ItemStackManaStorage> itemStackManaStorageCapability = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<ItemStackManaStorage> lazyManaHandler;

    public final ModManaStorage manaStorage;

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

    public long getCapacity() {
        return this.manaStorage.getMaxManaStored();
    }

    public long getManaLevel() {
        return this.manaStorage.getManaStored();
    }

    public void setManaLevel(long mana) {
        this.manaStorage.setMana(mana);
    }

    public long extractMana(long amount, boolean simulate) {
        return this.manaStorage.extractMana(amount, simulate);
    }

    public long receiveMana(long amount, boolean simulate) {
        return this.manaStorage.receiveMana(amount, simulate);
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
        manaStorage.deserializeNBT(nbt.get("manaStorage"));
    }
}
