package com.diamantino.eternalmagic.registration;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.networking.c2s.WandBenchButtonC2SPacket;
import com.diamantino.eternalmagic.networking.s2c.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel instance;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ModReferences.modId, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        instance = net;

        // S2C
        net.messageBuilder(ManaSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaSyncS2CPacket::new)
                .encoder(ManaSyncS2CPacket::toBytes)
                .consumerMainThread(ManaSyncS2CPacket::handle)
                .add();

        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();

        net.messageBuilder(RequiredManaSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RequiredManaSyncS2CPacket::new)
                .encoder(RequiredManaSyncS2CPacket::toBytes)
                .consumerMainThread(RequiredManaSyncS2CPacket::handle)
                .add();

        net.messageBuilder(WandBenchWandSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(WandBenchWandSyncS2CPacket::new)
                .encoder(WandBenchWandSyncS2CPacket::toBytes)
                .consumerMainThread(WandBenchWandSyncS2CPacket::handle)
                .add();

        net.messageBuilder(GeneratingManaSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GeneratingManaSyncS2CPacket::new)
                .encoder(GeneratingManaSyncS2CPacket::toBytes)
                .consumerMainThread(GeneratingManaSyncS2CPacket::handle)
                .add();

        // C2S
        net.messageBuilder(WandBenchButtonC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(WandBenchButtonC2SPacket::new)
                .encoder(WandBenchButtonC2SPacket::toBytes)
                .consumerMainThread(WandBenchButtonC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        instance.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        instance.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        instance.send(PacketDistributor.ALL.noArg(), message);
    }
}
