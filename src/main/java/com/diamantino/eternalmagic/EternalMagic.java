package com.diamantino.eternalmagic;

import com.diamantino.eternalmagic.registration.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModReferences.modId)
public class EternalMagic {
    public EternalMagic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEvents.registerEvents(modEventBus);
        ModItems.registerItems(modEventBus);
        ModBlocks.registerBlocks(modEventBus);
        ModBlockEntityTypes.registerBlockEntityTypes(modEventBus);
        ModMenuTypes.registerMenuTypes(modEventBus);
        ModCapabilities.registerCapabilities();
        ModMessages.register();
        ModCreativeTabs.registerCreativeTabs();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
