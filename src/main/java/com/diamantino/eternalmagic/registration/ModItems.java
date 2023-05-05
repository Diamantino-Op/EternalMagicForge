package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, ModReferences.modId);

    public static void registerItems(IEventBus bus) {
        items.register(bus);
    }
}
