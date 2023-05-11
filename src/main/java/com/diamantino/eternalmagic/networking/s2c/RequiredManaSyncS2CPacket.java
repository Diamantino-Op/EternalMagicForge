package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequiredManaSyncS2CPacket {
    private final long requiredMana;
    private final BlockPos pos;

    public RequiredManaSyncS2CPacket(long requiredMana, BlockPos pos) {
        this.requiredMana = requiredMana;
        this.pos = pos;
    }

    public RequiredManaSyncS2CPacket(FriendlyByteBuf buf) {
        this.requiredMana = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(requiredMana);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof WandBenchBlockEntity blockEntity) {
                blockEntity.setRequiredMana(requiredMana);

                if(Minecraft.getInstance().player.containerMenu instanceof WandBenchMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setRequiredMana(requiredMana);
                }
            }
        });

        return true;
    }
}
