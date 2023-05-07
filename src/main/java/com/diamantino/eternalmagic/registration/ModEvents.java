package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ModReferences.modId)
public class ModEvents {
    private static void addCreative(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS) {

        }

    }

    public static void registerEvents(IEventBus bus) {
        bus.addListener(ModEvents::addCreative);
    }
}
