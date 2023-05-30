package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.items.CoreItem;
import com.diamantino.eternalmagic.items.WandUpgradeItem;
import com.diamantino.eternalmagic.registration.ModBlocks;
import com.diamantino.eternalmagic.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class EMItemModelProvider extends ItemModelProvider {
    public EMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModReferences.modId, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<CoreItem> item : ModItems.wandCores.values()) {
            wandCore(item.get(), new ResourceLocation(ModReferences.modId, "item/core_base"));
        }

        for (RegistryObject<WandUpgradeItem> item : ModItems.wandUpgrades.values()) {
            wandUpgrade(item.get());
        }

        for (RegistryObject<? extends Block> block : ModBlocks.functionalBlocks.values()) {
            this.withExistingParent(block.getId().getPath(), new ResourceLocation(block.getId().getNamespace(), "block/" + block.getId().getPath()));
        }

        for (RegistryObject<? extends Block> block : ModBlocks.decorativeBlocks.values()) {
            if (block.getId().getPath().contains("_wall")) {
                this.withExistingParent(block.getId().getPath(), new ResourceLocation(block.getId().getNamespace(), "block/" + block.getId().getPath() + "_post"));
            } else {
                this.withExistingParent(block.getId().getPath(), new ResourceLocation(block.getId().getNamespace(), "block/" + block.getId().getPath()));
            }
        }

        for (RegistryObject<? extends Block> block : ModBlocks.resourcesBlocks.values()) {
            this.withExistingParent(block.getId().getPath(), new ResourceLocation(block.getId().getNamespace(), "block/" + block.getId().getPath()));
        }
    }

    public void wandCore(Item item, ResourceLocation parent)
    {
        ResourceLocation itemLoc = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        getBuilder(item.toString()).parent(new ModelFile.ExistingModelFile(parent, existingFileHelper))
                .texture("particle", new ResourceLocation(itemLoc.getNamespace(), "item/cores/" + itemLoc.getPath() + "_center"))
                .texture("core_internal", new ResourceLocation(itemLoc.getNamespace(), "item/cores/" + itemLoc.getPath() + "_center"))
                .texture("core_external", new ResourceLocation(itemLoc.getNamespace(), "item/cores/" + itemLoc.getPath() + "_external"));
    }

    public void wandUpgrade(Item item)
    {
        ResourceLocation itemLoc = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(itemLoc.getNamespace(), "item/upgrades/" + itemLoc.getPath()));
    }
}
