package com.diamantino.eternalmagic.api.mana;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IManaStorage {
    /**
     * Adds mana to the storage. Returns quantity of mana that was accepted.
     *
     * @param maxReceive
     *            Maximum amount of mana to be inserted.
     * @param simulate
     *            If TRUE, the insertion will only be simulated.
     * @return Amount of mana that was (or would have been, if simulated) accepted by the storage.
     */
    long receiveMana(long maxReceive, boolean simulate);

    /**
     * Removes mana from the storage. Returns quantity of mana that was removed.
     *
     * @param maxExtract
     *            Maximum amount of mana to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of mana that was (or would have been, if simulated) extracted from the storage.
     */
    long extractMana(long maxExtract, boolean simulate);

    /**
     * Returns the amount of mana currently stored.
     */
    long getManaStored();

    /**
     * Returns the maximum amount of mana that can be stored.
     */
    long getMaxManaStored();

    /**
     * Returns if this storage can have mana extracted.
     * If this is false, then any calls to extractMana will return 0.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive mana.
     * If this is false, then any calls to receiveMana will return 0.
     */
    boolean canReceive();
}
