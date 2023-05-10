package com.diamantino.eternalmagic.storage.mana;

import com.diamantino.eternalmagic.api.mana.ManaStorage;

public abstract class ModManaStorage extends ManaStorage {
    public ModManaStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer);
    }

    public ModManaStorage(int capacity, int maxTransfer, int mana) {
        super(capacity, maxTransfer, maxTransfer, mana);
    }

    @Override
    public int extractMana(int maxExtract, boolean simulate) {
        int extractedMana = super.extractMana(maxExtract, simulate);
        if(extractedMana != 0) {
            onManaChanged();
        }

        return extractedMana;
    }

    @Override
    public int receiveMana(int maxReceive, boolean simulate) {
        int receiveMana = super.receiveMana(maxReceive, simulate);
        if(receiveMana != 0) {
            onManaChanged();
        }

        return receiveMana;
    }

    public int setMana(int mana) {
        this.mana = mana;
        return mana;
    }

    public abstract void onManaChanged();
}
