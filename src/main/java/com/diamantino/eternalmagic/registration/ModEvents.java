package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModConstants;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ModConstants.modId)
public class ModEvents {
    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeTabs.functionalBlocksTab.get()) {
            ModBlocks.functionalBlocks.values().forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.decorativeBlocksTab.get()) {
            ModBlocks.decorativeBlocks.values().forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.itemsTab.get()) {
            ModItems.items.forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.resourcesTab.get()) {
            ModBlocks.resourcesBlocks.values().forEach((item) -> event.accept(item.get()));
            ModItems.resourcesItems.forEach((item) -> event.accept(item.get()));
        }
    }

    /*private static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        PostsInChunk cap = new PostsInChunk(event.getObject());
        event.addCapability(new ResourceLocation(ModReferences.modId, "mana_storage"), cap);
        event.addListener(cap::onCapabilityInvalidated);
    }*/

    public static void registerModEvents() {
        //bus.addGenericListener(ItemStack.class, ModEvents::onAttachChunkCapabilities);
    }
}
