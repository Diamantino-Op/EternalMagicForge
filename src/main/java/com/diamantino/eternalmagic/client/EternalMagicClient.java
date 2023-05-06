package com.diamantino.eternalmagic.client;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModReferences.modId, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EternalMagicClient {
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
