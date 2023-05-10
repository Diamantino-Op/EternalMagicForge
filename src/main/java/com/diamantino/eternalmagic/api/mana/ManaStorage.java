package com.diamantino.eternalmagic.api.mana;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class ManaStorage implements IManaStorage, INBTSerializable<Tag> {
    protected int mana;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public ManaStorage(int capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public ManaStorage(int capacity, int maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public ManaStorage(int capacity, int maxReceive, int maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public ManaStorage(int capacity, int maxReceive, int maxExtract, int mana)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.mana = Math.max(0, Math.min(capacity, mana));
    }

    @Override
    public int receiveMana(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int manaReceived = Math.min(capacity - mana, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            mana += manaReceived;
        return manaReceived;
    }

    @Override
    public int extractMana(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int manaExtracted = Math.min(mana, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            mana -= manaExtracted;
        return manaExtracted;
    }

    @Override
    public int getManaStored()
    {
        return mana;
    }

    @Override
    public int getMaxManaStored()
    {
        return capacity;
    }

    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }

    @Override
    public Tag serializeNBT()
    {
        return IntTag.valueOf(this.getManaStored());
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.mana = intNbt.getAsInt();
    }
}
