package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.api.mana.ItemStackManaStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public abstract class ManaItemBase extends Item {
    public static long baseStoredMana;
    public static long maxReceive;
    public static long maxExtract;

    public ManaItemBase(Properties pProperties, long baseStoredMana, long maxTransfer) {
        this(pProperties, baseStoredMana, maxTransfer, maxTransfer);
    }

    public ManaItemBase(Properties pProperties, long baseStoredManaL, long maxReceiveL, long maxExtractL) {
        super(pProperties);

        baseStoredMana = baseStoredManaL;
        maxReceive = maxReceiveL;
        maxExtract = maxExtractL;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemStackManaStorage(baseStoredMana, maxReceive, maxExtract);
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        return stack.serializeNBT();
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt != null)
            stack.deserializeNBT(nbt);
    }

    public static void setCapacity(ItemStack stack, long capacity) {
        stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            itemStackManaStorage.setCapacity(capacity);
        });
    }

    public static long getCapacity(ItemStack stack) {
        AtomicLong returnVal = new AtomicLong(0);

        stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            returnVal.set(itemStackManaStorage.getCapacity());
        });

        return returnVal.get();
    }

    public static long getManaLevel(ItemStack stack) {
        AtomicLong returnVal = new AtomicLong(0);

        stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            returnVal.set(itemStackManaStorage.getManaLevel());
        });

        return returnVal.get();
    }

    public static void setManaLevel(ItemStack stack, long mana) {
        stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            itemStackManaStorage.setManaLevel(mana);
        });
    }

    private static long extractMana(ItemStack stack, long amount, boolean simulate) {
        AtomicLong returnVal = new AtomicLong(0);

        stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            returnVal.set(itemStackManaStorage.extractMana(amount, simulate));
        });

        return returnVal.get();
    }

    private static long receiveMana(ItemStack stack, long amount, boolean simulate) {
        AtomicLong returnVal = new AtomicLong(0);

        stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            returnVal.set(itemStackManaStorage.receiveMana(amount, simulate));
        });

        return returnVal.get();
    }

    private static boolean hasEnoughMana(ItemStack stack, long requiredMana) {
        AtomicBoolean returnVal = new AtomicBoolean(false);

        stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            returnVal.set(itemStackManaStorage.hasEnoughMana(requiredMana));
        });

        return returnVal.get();
    }
}
