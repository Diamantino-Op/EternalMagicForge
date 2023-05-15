package com.diamantino.eternalmagic.networking.c2s;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import com.diamantino.eternalmagic.client.model.Model;
import com.diamantino.eternalmagic.client.model.ModelLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class WandBenchButtonC2SPacket {
    private final BlockPos pos;
    private final int btnId;
    private final int modelId;
    private final String btnText;

    public WandBenchButtonC2SPacket(BlockPos pos, int btnId, int modelId, String btnText) {
        this.pos = pos;
        this.btnId = btnId;
        this.modelId = modelId;
        this.btnText = btnText;
    }

    public WandBenchButtonC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.btnId = buf.readInt();
        this.modelId = buf.readInt();
        this.btnText = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(btnId);
        buf.writeInt(modelId);
        buf.writeUtf(btnText);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ServerPlayer player = context.getSender();

        context.enqueueWork(() -> {
            if (player != null && player.level.getBlockEntity(pos) instanceof WandBenchBlockEntity blockEntity && player.containerMenu instanceof WandBenchMenu menu && blockEntity.getRenderStack() != ItemStack.EMPTY) {
                switch (btnId) {
                    case 0 -> {
                        menu.setSelectedModelToAddId(btnText);
                    }
                    case 1 -> {
                        menu.setSelectedModelId(modelId);
                    }
                }
            }
        });
    }
}
