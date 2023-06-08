package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> creativeTabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModConstants.modId);

    public static RegistryObject<CreativeModeTab> functionalBlocksTab = creativeTabs.register("functional_blocks_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> new ItemStack(ModBlocks.wandBenchBlock.get()))
                    .title(Component.translatable("itemGroup." + ModConstants.modId + ".functional_blocks"))
                    .build()
    );

    public static RegistryObject<CreativeModeTab> decorativeBlocksTab = creativeTabs.register("decorative_blocks_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> new ItemStack(ModBlocks.decorativeBlocks.get("dark_stone").get()))
                    .title(Component.translatable("itemGroup." + ModConstants.modId + ".decorative_blocks"))
                    .build()
    );

    public static RegistryObject<CreativeModeTab> itemsTab = creativeTabs.register("items_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> new ItemStack(ModItems.wandItem.get()))
                    .title(Component.translatable("itemGroup." + ModConstants.modId + ".items"))
                    .build()
    );

    public static RegistryObject<CreativeModeTab> resourcesTab = creativeTabs.register("resources_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> new ItemStack(Blocks.IRON_ORE))
                    .title(Component.translatable("itemGroup." + ModConstants.modId + ".resources"))
                    .build()
    );

    public static void registerCreativeTabs(IEventBus bus) {
        creativeTabs.register(bus);
    }
}
