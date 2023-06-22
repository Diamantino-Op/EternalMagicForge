package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.EternalMagic;
import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.api.capabilities.player.MageInfo;
import com.diamantino.eternalmagic.api.capabilities.player.PlayerMageInfo;
import com.diamantino.eternalmagic.multiblocks.Multiblock;
import com.diamantino.eternalmagic.multiblocks.MultiblockRegistry;
import com.diamantino.eternalmagic.multiblocks.MultiblockUtils;
import com.diamantino.eternalmagic.networking.s2c.MultiblockBlockSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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

    @SubscribeEvent
    public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(PlayerMageInfo.mageInfoCapability).isPresent()) {
                event.addCapability(new ResourceLocation(ModConstants.modId, "mage_info"), new PlayerMageInfo(player));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();

            event.getOriginal().getCapability(PlayerMageInfo.mageInfoCapability).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerMageInfo.mageInfoCapability).ifPresent(newStore -> {
                    newStore.deserializeNBT(oldStore.serializeNBT());
                });
            });

            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerMageInfo.mageInfoCapability).ifPresent(MageInfo::sendUpdatePacket);
            }
        }
    }

    public static void registerForgeEvents() {

    }
}
