package com.diamantino.eternalmagic.api.mana;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.core.Direction;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class ManaStorage implements IManaStorage, INBTSerializable<Tag> {
    protected long mana;
    protected long capacity;
    protected long maxReceive;
    protected long maxExtract;

    protected SideInfo topFace;
    protected SideInfo bottomFace;
    protected SideInfo northFace;
    protected SideInfo southFace;
    protected SideInfo eastFace;
    protected SideInfo westFace;

    public ManaStorage(long capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public ManaStorage(long capacity, long maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public ManaStorage(long capacity, long maxReceive, long maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public ManaStorage(long capacity, long maxReceive, long maxExtract, long mana)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.mana = Math.max(0, Math.min(capacity, mana));
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setMaxTransfer(long maxTransfer) {
        this.maxExtract = maxTransfer;
        this.maxReceive = maxTransfer;
    }

    public void setMaxReceive(long maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(long maxExtract) {
        this.maxExtract = maxExtract;
    }

    @Override
    public long receiveMana(Direction side, long maxReceive, boolean simulate)
    {
        if (!canReceive(side))
            return 0;

        long manaReceived = Math.min(capacity - mana, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            mana += manaReceived;
        return manaReceived;
    }

    @Override
    public long extractMana(Direction side, long maxExtract, boolean simulate)
    {
        if (!canExtract(side))
            return 0;

        long manaExtracted = Math.min(mana, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            mana -= manaExtracted;
        return manaExtracted;
    }

    @Override
    public long getManaStored()
    {
        return mana;
    }

    @Override
    public long getMaxManaStored()
    {
        return capacity;
    }

    @Override
    public long getMaxExtract() {
        return maxExtract;
    }

    @Override
    public long getMaxReceive() {
        return maxReceive;
    }

    @Override
    public boolean canExtract(Direction side)
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive(Direction side)
    {
        return this.maxReceive > 0;
    }

    @Override
    public boolean isSideEnabled(Direction side) {
        return false;
    }

    @Override
    public Tag serializeNBT()
    {
        return LongTag.valueOf(this.getManaStored());
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        if (!(nbt instanceof LongTag longNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.mana = longNbt.getAsLong();
    }

    public enum SideInfo {
        extract,
        insert,
        none
    }
}
