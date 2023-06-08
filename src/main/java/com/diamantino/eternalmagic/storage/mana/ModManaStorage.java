package com.diamantino.eternalmagic.storage.mana;

import com.diamantino.eternalmagic.api.mana.ManaStorage;
import net.minecraft.core.Direction;

public abstract class ModManaStorage extends ManaStorage {
    public ModManaStorage(long capacity, long maxTransfer) {
        super(capacity, maxTransfer, maxTransfer);
    }

    public ModManaStorage(long capacity, long maxReceive, long maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public ModManaStorage(long capacity, long maxReceive, long maxExtract, long mana) {
        super(capacity, maxReceive, maxExtract, mana);
    }

    @Override
    public void setCapacity(long capacity) {
        super.setCapacity(capacity);

        onManaChanged();
    }

    @Override
    public void setMaxTransfer(long maxTransfer) {
        super.setMaxTransfer(maxTransfer);

        onManaChanged();
    }

    @Override
    public void setMaxExtract(long maxExtract) {
        super.setMaxExtract(maxExtract);

        onManaChanged();
    }

    @Override
    public void setMaxReceive(long maxReceive) {
        super.setMaxReceive(maxReceive);

        onManaChanged();
    }

    @Override
    public long extractMana(Direction direction, long maxExtract, boolean simulate) {
        long extractedMana = super.extractMana(direction, maxExtract, simulate);
        if(extractedMana != 0) {
            onManaChanged();
        }

        return extractedMana;
    }

    @Override
    public long receiveMana(Direction direction, long maxReceive, boolean simulate) {
        long receiveMana = super.receiveMana(direction, maxReceive, simulate);
        if(receiveMana != 0) {
            onManaChanged();
        }

        return receiveMana;
    }

    public void setMana(long mana) {
        this.mana = mana;
    }

    public abstract void onManaChanged();
}
