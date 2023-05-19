package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ModReferences.modId)
public class ModEvents {
    private static void addCreative(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == ModCreativeTabs.functionalBlocksTab) {
            ModBlocks.functionalBlocks.forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.decorativeBlocksTab) {
            ModBlocks.decorativeBlocks.forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.itemsTab) {
            ModItems.items.forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.resourcesTab) {
            ModBlocks.resourcesBlocks.forEach((item) -> event.accept(item.get()));
            ModItems.resourcesItems.forEach((item) -> event.accept(item.get()));
        }
    }

    public static void registerEvents(IEventBus bus) {
        bus.addListener(ModEvents::addCreative);
    }
}
