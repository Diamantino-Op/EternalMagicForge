package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.items.WandCoreItem;
import com.diamantino.eternalmagic.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
        for (RegistryObject<WandCoreItem> item : ModItems.wandCores.values()) {
            wandCore(item.get(), new ResourceLocation(ModReferences.modId, "item/wand_core_base"));
        }
    }

    public void wandCore(Item item, ResourceLocation parent)
    {
        ResourceLocation itemLoc = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        getBuilder(item.toString()).parent(new ModelFile.ExistingModelFile(parent, existingFileHelper))
                .texture("particle", new ResourceLocation(itemLoc.getNamespace(), "item/wand_cores/" + itemLoc.getPath() + "_center"))
                .texture("wand_core_internal", new ResourceLocation(itemLoc.getNamespace(), "item/wand_cores/" + itemLoc.getPath() + "_center"))
                .texture("wand_core_external", new ResourceLocation(itemLoc.getNamespace(), "item/wand_cores/" + itemLoc.getPath() + "_external"));
    }
}
