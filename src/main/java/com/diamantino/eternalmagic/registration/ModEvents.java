package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.api.capabilities.player.PlayerMageInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;

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

    public static void registerModEvents() {

    }
}
