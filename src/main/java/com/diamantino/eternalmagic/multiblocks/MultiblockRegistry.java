package com.diamantino.eternalmagic.multiblocks;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MultiblockRegistry {
    public final Map<ResourceLocation, Multiblock> multiblockMap = new LinkedHashMap<>();
    public final List<ResourceLocation> registeredStructures = new ArrayList<>();

    public void addMultiblock(ResourceLocation resourceLocation, Multiblock multiblock) {
        multiblockMap.put(resourceLocation, multiblock);
    }

    public Multiblock getMultiblockByName(ResourceLocation resourceLocation) {
        return multiblockMap.get(resourceLocation);
    }
}
