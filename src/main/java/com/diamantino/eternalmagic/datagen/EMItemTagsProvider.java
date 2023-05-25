package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMItemTagsProvider extends ItemTagsProvider implements DataProvider {
    public EMItemTagsProvider(PackOutput packOutput, CompletableFuture<TagsProvider.TagLookup<Block>> tagLookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor()), tagLookupCompletableFuture, ModReferences.modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {

    }
}
