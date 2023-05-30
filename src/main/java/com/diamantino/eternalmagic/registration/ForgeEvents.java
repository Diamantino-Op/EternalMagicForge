package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.EternalMagic;
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
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        MultiblockRegistry registry = EternalMagic.instance.multiblockRegistry;

        RandomSource randomSource = RandomSource.create();

        for (ResourceLocation resourceLocation : registry.registeredStructures) {
            StructureTemplate multiblock = MultiblockUtils.loadMultiblockNbt(event.getServer(), resourceLocation);

            if (multiblock != null) {
                StructurePlaceSettings settings = new StructurePlaceSettings().setRandom(randomSource);

                registry.addMultiblock(resourceLocation, new Multiblock(resourceLocation, settings.getRandomPalette(multiblock.palettes, new BlockPos(0, 0, 0)).blocks()));
            }
        }
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        MultiblockRegistry registry = EternalMagic.instance.multiblockRegistry;

        if (event.getPlayer() != null) {
            registry.multiblockMap.forEach((id, multiblock) -> {
                ModMessages.sendToPlayer(new MultiblockBlockSyncS2CPacket(multiblock.multiblockTemplateBlocks(), id, registry.multiblockMap.size()), event.getPlayer());
            });
        } else {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                registry.multiblockMap.forEach((id, multiblock) -> {
                    ModMessages.sendToPlayer(new MultiblockBlockSyncS2CPacket(multiblock.multiblockTemplateBlocks(), id, registry.multiblockMap.size()), player);
                });
            }
        }
    }

    public static void registerForgeEvents() {

    }
}
