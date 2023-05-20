package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModReferences.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {
    public static CreativeModeTab functionalBlocksTab;
    public static CreativeModeTab decorativeBlocksTab;
    public static CreativeModeTab itemsTab;
    public static CreativeModeTab resourcesTab;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        functionalBlocksTab = event.registerCreativeModeTab(new ResourceLocation(ModReferences.modId, "functional_blocks_tab"), builder -> builder.icon(() -> new ItemStack(ModBlocks.wandBenchBlock.get())).title(Component.translatable("itemGroup." + ModReferences.modId + ".functional_blocks")).build());
        decorativeBlocksTab = event.registerCreativeModeTab(new ResourceLocation(ModReferences.modId, "decorative_blocks_tab"), builder -> builder.icon(() -> new ItemStack(Blocks.IRON_BLOCK)).title(Component.translatable("itemGroup." + ModReferences.modId + ".decorative_blocks")).build());
        itemsTab = event.registerCreativeModeTab(new ResourceLocation(ModReferences.modId, "items_tab"), builder -> builder.icon(() -> new ItemStack(ModItems.wandItem.get())).title(Component.translatable("itemGroup." + ModReferences.modId + ".items")).build());
        resourcesTab = event.registerCreativeModeTab(new ResourceLocation(ModReferences.modId, "resources_tab"), builder -> builder.icon(() -> new ItemStack(Blocks.IRON_ORE)).title(Component.translatable("itemGroup." + ModReferences.modId + ".resources")).build());
    }

    public static void registerCreativeTabs() {

    }
}
