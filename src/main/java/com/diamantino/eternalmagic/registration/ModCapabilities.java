package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.api.mana.IManaStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;

import static net.minecraftforge.common.capabilities.CapabilityManager.get;

public class ModCapabilities {
    public static final Capability<IManaStorage> mana = get(new CapabilityToken<>(){});

    public static void registerCapabilities() {}
}
