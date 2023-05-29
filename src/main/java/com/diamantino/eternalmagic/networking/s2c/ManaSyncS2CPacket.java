package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.blockentities.ManaBlockEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ManaSyncS2CPacket {
    private final long manaStored;
    private final long maxReceive;
    private final long maxExtract;
    private final long capacity;
    private final BlockPos pos;

    public ManaSyncS2CPacket(long manaStored, long maxReceive, long maxExtract, long capacity, BlockPos pos) {
        this.manaStored = manaStored;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.capacity = capacity;
        this.pos = pos;
    }

    public ManaSyncS2CPacket(FriendlyByteBuf buf) {
        this.manaStored = buf.readLong();
        this.maxReceive = buf.readLong();
        this.maxExtract = buf.readLong();
        this.capacity = buf.readLong();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manaStored);
        buf.writeLong(maxReceive);
        buf.writeLong(maxExtract);
        buf.writeLong(capacity);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof ManaBlockEntityBase blockEntity) {
                blockEntity.setManaLevel(manaStored);
                blockEntity.setCapacity(capacity);

                if (maxExtract == maxReceive) {
                    blockEntity.setMaxTransfer(maxExtract);
                } else {
                    blockEntity.setMaxReceive(maxReceive);
                    blockEntity.setMaxExtract(maxExtract);
                }
            }
        });
    }
}
