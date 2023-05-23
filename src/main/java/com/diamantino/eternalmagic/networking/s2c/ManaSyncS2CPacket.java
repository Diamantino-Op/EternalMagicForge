package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.blockentities.ManaBlockEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ManaSyncS2CPacket {
    private final long mana;
    private final BlockPos pos;

    public ManaSyncS2CPacket(long mana, BlockPos pos) {
        this.mana = mana;
        this.pos = pos;
    }

    public ManaSyncS2CPacket(FriendlyByteBuf buf) {
        this.mana = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(mana);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof ManaBlockEntityBase blockEntity) {
                blockEntity.setManaLevel(mana);
            }
        });
    }
}
