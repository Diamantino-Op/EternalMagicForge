package com.diamantino.eternalmagic.datagen;

import com.diamantino.eternalmagic.ModConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModConstants.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EternalMagicDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        EMBlockTagsProvider blockTagsProvider = new EMBlockTagsProvider(packOutput, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new EMItemTagsProvider(packOutput, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeClient(), new EMBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new EMItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new EMLootTableProvider(packOutput));
    }
}
