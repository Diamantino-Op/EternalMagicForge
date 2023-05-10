package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.items.WandItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, ModReferences.modId);

    public static final RegistryObject<WandItem> wandItem = items.register("wand", () -> new WandItem(new Item.Properties().stacksTo(1)));

    public static void registerItems(IEventBus bus) {
        items.register(bus);
    }
}
