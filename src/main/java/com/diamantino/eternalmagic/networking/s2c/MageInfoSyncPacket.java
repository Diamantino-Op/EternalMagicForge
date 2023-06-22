package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.api.capabilities.player.PlayerMageInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MageInfoSyncPacket {
    private final CompoundTag mageInfoTag;

    public MageInfoSyncPacket(CompoundTag mageInfoTag) {
        this.mageInfoTag = mageInfoTag;
    }

    public MageInfoSyncPacket(FriendlyByteBuf buf) {
        this.mageInfoTag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(mageInfoTag);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;

            if (player != null)
                player.getCapability(PlayerMageInfo.mageInfoCapability).ifPresent(mageInfo -> mageInfo.deserializeNBT(mageInfoTag));
        });
    }
}
