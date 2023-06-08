package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.client.menu.ShrineCoreMenu;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> menuTypes = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModConstants.modId);

    public static final RegistryObject<MenuType<WandBenchMenu>> wandBenchMenu = registerMenuType(WandBenchMenu::new, "wand_bench");
    public static final RegistryObject<MenuType<ShrineCoreMenu>> shrineCoreMenu = registerMenuType(ShrineCoreMenu::new, "shrine_core");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return menuTypes.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void registerMenuTypes(IEventBus bus) {
        menuTypes.register(bus);
    }
}
