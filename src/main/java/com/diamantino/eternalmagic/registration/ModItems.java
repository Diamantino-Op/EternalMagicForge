package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.items.WandCoreItem;
import com.diamantino.eternalmagic.items.WandItem;
import com.diamantino.eternalmagic.items.WandUpgradeItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> modItems = DeferredRegister.create(ForgeRegistries.ITEMS, ModReferences.modId);

    public static final List<RegistryObject<? extends Item>> items = new ArrayList<>();
    public static final List<RegistryObject<? extends Item>> resourcesItems = new ArrayList<>();

    public static final Map<WandCoreItem.WandCoreElement, RegistryObject<WandCoreItem>> wandCores = new LinkedHashMap<>();
    public static final Map<WandUpgradeItem.WandUpgradeType, RegistryObject<WandUpgradeItem>> wandUpgrades = new LinkedHashMap<>();

    public static final RegistryObject<WandItem> wandItem = registerItem("wand", () -> new WandItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> emptyUpgrade = registerItem("blank_upgrade", () -> new Item(new Item.Properties().stacksTo(16)));

    private static void registerWandCores() {
        for (WandCoreItem.WandCoreElement element : WandCoreItem.WandCoreElement.values()) {
            if (element == WandCoreItem.WandCoreElement.none)
                continue;

            RegistryObject<WandCoreItem> item = modItems.register("wand_" + element.toString() + "_core", () -> new WandCoreItem(new Item.Properties().stacksTo(1), element));
            wandCores.put(element, item);
            items.add(item);
        }
    }

    private static void registerWandUpgrades() {
        for (WandUpgradeItem.WandUpgradeType upgrade : WandUpgradeItem.WandUpgradeType.values()) {
            if (upgrade == WandUpgradeItem.WandUpgradeType.none)
                continue;

            RegistryObject<WandUpgradeItem> item = modItems.register("wand_" + upgrade.regName + "_upgrade", () -> new WandUpgradeItem(new Item.Properties().stacksTo(1), upgrade, 5));
            wandUpgrades.put(upgrade, item);
            items.add(item);
        }
    }

    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = modItems.register(name, block);
        items.add(toReturn);
        return toReturn;
    }

    private static <T extends Item> RegistryObject<T> registerResource(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = modItems.register(name, block);
        resourcesItems.add(toReturn);
        return toReturn;
    }

    public static void registerItems(IEventBus bus) {
        modItems.register(bus);

        registerWandCores();
        registerWandUpgrades();
    }
}
