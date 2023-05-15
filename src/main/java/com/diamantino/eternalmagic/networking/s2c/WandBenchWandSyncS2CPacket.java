package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.client.screens.WandBenchScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WandBenchWandSyncS2CPacket {
    private final ItemStack wandStack;

    public WandBenchWandSyncS2CPacket(ItemStack wandStack) {
        this.wandStack = wandStack;
    }

    public WandBenchWandSyncS2CPacket(FriendlyByteBuf buf) {
        this.wandStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(wandStack);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        Minecraft mc = Minecraft.getInstance();

        context.enqueueWork(() -> {
            if (mc.screen instanceof WandBenchScreen wandBenchScreen) {
                wandBenchScreen.updateAddedModels();

                ModReferences.logger.warn("Updating added models list!");
            }
        });
    }
}
