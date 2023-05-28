package com.diamantino.eternalmagic.multiblocks;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiblockLoader extends SimplePreparableReloadListener<List<ResourceLocation>> {
    private final String directory;
    public static final List<ResourceLocation> multiblocks = new ArrayList<>();

    public MultiblockLoader() {
        this.directory = "multiblocks";
    }

    @Override
    protected @NotNull List<ResourceLocation> prepare(@NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        List<ResourceLocation> list = new ArrayList<>();
        FileToIdConverter filetoidconverter = new FileToIdConverter(this.directory, ".nbt");

        for(Map.Entry<ResourceLocation, Resource> entry : filetoidconverter.listMatchingResources(pResourceManager).entrySet()) {
            ResourceLocation resourcelocation = filetoidconverter.fileToId(entry.getKey());

            list.add(resourcelocation);
        }

        return list;
    }

    @Override
    protected void apply(@NotNull List<ResourceLocation> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        multiblocks.clear();

        multiblocks.addAll(pObject);
    }

    protected ResourceLocation getPreparedPath(ResourceLocation rl) {
        return new ResourceLocation(rl.getNamespace(), this.directory + "/" + rl.getPath() + ".nbt");
    }
}
