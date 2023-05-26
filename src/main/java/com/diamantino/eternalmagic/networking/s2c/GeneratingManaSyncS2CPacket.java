package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.blockentities.ShrineCoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratingManaSyncS2CPacket {
    private final long generatingMana;
    private final BlockPos pos;

    public GeneratingManaSyncS2CPacket(long generatingMana, BlockPos pos) {
        this.generatingMana = generatingMana;
        this.pos = pos;
    }

    public GeneratingManaSyncS2CPacket(FriendlyByteBuf buf) {
        this.generatingMana = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(generatingMana);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof ShrineCoreBlockEntity blockEntity) {
                blockEntity.setGeneratingMana(generatingMana);
            }
        });
    }
}
