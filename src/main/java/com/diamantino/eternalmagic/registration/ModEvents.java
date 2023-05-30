package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.EternalMagic;
import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.multiblocks.Multiblock;
import com.diamantino.eternalmagic.multiblocks.MultiblockRegistry;
import com.diamantino.eternalmagic.multiblocks.MultiblockUtils;
import com.diamantino.eternalmagic.networking.s2c.MultiblockBlockSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ModReferences.modId)
public class ModEvents {
    @SubscribeEvent
    public static void addCreative(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == ModCreativeTabs.functionalBlocksTab) {
            ModBlocks.functionalBlocks.values().forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.decorativeBlocksTab) {
            ModBlocks.decorativeBlocks.values().forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.itemsTab) {
            ModItems.items.forEach((item) -> event.accept(item.get()));
        }

        if (event.getTab() == ModCreativeTabs.resourcesTab) {
            ModBlocks.resourcesBlocks.values().forEach((item) -> event.accept(item.get()));
            ModItems.resourcesItems.forEach((item) -> event.accept(item.get()));
        }
    }

    /*private static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        PostsInChunk cap = new PostsInChunk(event.getObject());
        event.addCapability(new ResourceLocation(ModReferences.modId, "mana_storage"), cap);
        event.addListener(cap::onCapabilityInvalidated);
    }*/

    public static void registerModEvents() {
        //bus.addGenericListener(ItemStack.class, ModEvents::onAttachChunkCapabilities);
    }
}
