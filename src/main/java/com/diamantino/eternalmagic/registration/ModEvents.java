package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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

    /*private static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        PostsInChunk cap = new PostsInChunk(event.getObject());
        event.addCapability(new ResourceLocation(ModReferences.modId, "mana_storage"), cap);
        event.addListener(cap::onCapabilityInvalidated);
    }*/

    public static void registerEvents(IEventBus bus) {
        bus.addListener(ModEvents::addCreative);
        //bus.addGenericListener(ItemStack.class, ModEvents::onAttachChunkCapabilities);
    }
}
