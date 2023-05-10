package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ManaSyncS2CPacket {
    private final int mana;
    private final BlockPos pos;

    public ManaSyncS2CPacket(int mana, BlockPos pos) {
        this.mana = mana;
        this.pos = pos;
    }

    public ManaSyncS2CPacket(FriendlyByteBuf buf) {
        this.mana = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(mana);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof WandBenchBlockEntity blockEntity) {
                blockEntity.setManaLevel(mana);

                if(Minecraft.getInstance().player.containerMenu instanceof WandBenchMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setManaLevel(mana);
                }
            }
        });

        return true;
    }
}
