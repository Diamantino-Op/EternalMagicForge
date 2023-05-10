package com.diamantino.eternalmagic.client;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.client.model.ModelLoader;
import com.diamantino.eternalmagic.client.screens.WandBenchScreen;
import com.diamantino.eternalmagic.registration.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModReferences.modId, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EternalMagicClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.wandBenchMenu.get(), WandBenchScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterLoaders(ModelEvent.RegisterGeometryLoaders event)
    {
        event.register("em_model_loader", new ModelLoader.ModelGeometryLoader());
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event)
    {
        new ModelLoader().registerModels(event);
    }
}
