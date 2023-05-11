package com.diamantino.eternalmagic.storage.mana;

import com.diamantino.eternalmagic.api.mana.ManaStorage;

public abstract class ModManaStorage extends ManaStorage {
    public ModManaStorage(long capacity, long maxTransfer) {
        super(capacity, maxTransfer, maxTransfer);
    }

    public ModManaStorage(long capacity, long maxTransfer, long mana) {
        super(capacity, maxTransfer, maxTransfer, mana);
    }

    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        long extractedMana = super.extractMana(maxExtract, simulate);
        if(extractedMana != 0) {
            onManaChanged();
        }

        return extractedMana;
    }

    @Override
    public long receiveMana(long maxReceive, boolean simulate) {
        long receiveMana = super.receiveMana(maxReceive, simulate);
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
