package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.multiblocks.MultiblockLoader;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    public static void addResourceListeners(AddReloadListenerEvent event) {
        event.addListener(new MultiblockLoader());
    }

    public static void registerForgeEvents() {

    }
}
