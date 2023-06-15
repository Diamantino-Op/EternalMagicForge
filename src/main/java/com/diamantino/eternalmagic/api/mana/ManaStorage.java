package com.diamantino.eternalmagic.api.mana;

import net.minecraft.core.Direction;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class ManaStorage implements IManaStorage, INBTSerializable<Tag> {
    protected long mana;
    protected long capacity;
    protected long maxReceive;
    protected long maxExtract;

    public SideInfo topFace = SideInfo.none;
    public SideInfo bottomFace = SideInfo.none;
    public SideInfo northFace = SideInfo.none;
    public SideInfo southFace = SideInfo.none;
    public SideInfo eastFace = SideInfo.none;
    public SideInfo westFace = SideInfo.none;

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
    public void setManaStored(long mana) {
        this.mana = mana;
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
        return side == null ? this.maxExtract > 0 : switch (side) {
            case DOWN -> bottomFace == SideInfo.extract || bottomFace == SideInfo.both;
            case UP -> topFace == SideInfo.extract || topFace == SideInfo.both;
            case NORTH -> northFace == SideInfo.extract || northFace == SideInfo.both;
            case SOUTH -> southFace == SideInfo.extract || southFace == SideInfo.both;
            case WEST -> westFace == SideInfo.extract || westFace == SideInfo.both;
            case EAST -> eastFace == SideInfo.extract || eastFace == SideInfo.both;
        };
    }

    @Override
    public boolean canReceive(Direction side)
    {
        return side == null ? this.maxReceive > 0 : switch (side) {
            case DOWN -> bottomFace == SideInfo.insert || bottomFace == SideInfo.both;
            case UP -> topFace == SideInfo.insert || topFace == SideInfo.both;
            case NORTH -> northFace == SideInfo.insert || northFace == SideInfo.both;
            case SOUTH -> southFace == SideInfo.insert || southFace == SideInfo.both;
            case WEST -> westFace == SideInfo.insert || westFace == SideInfo.both;
            case EAST -> eastFace == SideInfo.insert || eastFace == SideInfo.both;
        };
    }

    @Override
    public boolean canIO(Direction side)
    {
        return side == null ? (this.maxExtract > 0 && this.maxReceive > 0) : switch (side) {
            case DOWN -> bottomFace == SideInfo.both;
            case UP -> topFace == SideInfo.both;
            case NORTH -> northFace == SideInfo.both;
            case SOUTH -> southFace == SideInfo.both;
            case WEST -> westFace == SideInfo.both;
            case EAST -> eastFace == SideInfo.both;
        };
    }

    @Override
    public boolean isSideEnabled(Direction side) {
        return bottomFace != SideInfo.none && topFace != SideInfo.none && northFace != SideInfo.none && southFace != SideInfo.none && westFace != SideInfo.none && eastFace != SideInfo.none;
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
        both,
        none
    }
}
