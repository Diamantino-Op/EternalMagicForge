package com.diamantino.eternalmagic;

import com.diamantino.eternalmagic.multiblocks.MultiblockRegistry;
import com.diamantino.eternalmagic.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModReferences.modId)
public class EternalMagic {
    public static EternalMagic instance;
    public MultiblockRegistry multiblockRegistry;

    public EternalMagic() {
        instance = this;

        multiblockRegistry = new MultiblockRegistry();

        multiblockRegistry.registeredStructures.add(new ResourceLocation(ModReferences.modId, "shrine"));

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEvents.registerModEvents();
        ForgeEvents.registerForgeEvents();
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
