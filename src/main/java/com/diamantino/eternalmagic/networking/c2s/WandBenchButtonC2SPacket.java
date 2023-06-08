package com.diamantino.eternalmagic.networking.c2s;

import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WandBenchButtonC2SPacket {
    private final BlockPos pos;
    private final int btnId;
    private final int modelId;
    private final String btnText;
    private final boolean isShifted;

    public WandBenchButtonC2SPacket(BlockPos pos, int btnId, int modelId, String btnText, boolean isShifted) {
        this.pos = pos;
        this.btnId = btnId;
        this.modelId = modelId;
        this.btnText = btnText;
        this.isShifted = isShifted;
    }

    public WandBenchButtonC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.btnId = buf.readInt();
        this.modelId = buf.readInt();
        this.btnText = buf.readUtf();
        this.isShifted = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(btnId);
        buf.writeInt(modelId);
        buf.writeUtf(btnText);
        buf.writeBoolean(isShifted);
    }

    private float getMov() {
        return isShifted ? 0.1f : 1;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ServerPlayer player = context.getSender();

        context.enqueueWork(() -> {
            if (player != null && player.level().getBlockEntity(pos) instanceof WandBenchBlockEntity blockEntity && player.containerMenu instanceof WandBenchMenu menu && blockEntity.getRenderStack() != ItemStack.EMPTY) {
                switch (btnId) {
                    case 0 -> menu.setSelectedModelToAddId(btnText);
                    case 1 -> menu.setSelectedModelId(modelId);

                    case 2 -> menu.editSelectedModel(getMov(), 0, 0, 0, 0, 0, 0, 0, 0);
                    case 3 -> menu.editSelectedModel(0, getMov(), 0, 0, 0, 0, 0, 0, 0);
                    case 4 -> menu.editSelectedModel(0, 0, getMov(), 0, 0, 0, 0, 0, 0);
                    case 5 -> menu.editSelectedModel(-getMov(), 0, 0, 0, 0, 0, 0, 0, 0);
                    case 6 -> menu.editSelectedModel(0, -getMov(), 0, 0, 0, 0, 0, 0, 0);
                    case 7 -> menu.editSelectedModel(0, 0, -getMov(), 0, 0, 0, 0, 0, 0);

                    case 8 -> menu.editSelectedModel(0, 0, 0, getMov(), 0, 0, 0, 0, 0);
                    case 9 -> menu.editSelectedModel(0, 0, 0, 0, getMov(), 0, 0, 0, 0);
                    case 10 -> menu.editSelectedModel(0, 0, 0, 0, 0, getMov(), 0, 0, 0);
                    case 11 -> menu.editSelectedModel(0, 0, 0, -getMov(), 0, 0, 0, 0, 0);
                    case 12 -> menu.editSelectedModel(0, 0, 0, 0, -getMov(), 0, 0, 0, 0);
                    case 13 -> menu.editSelectedModel(0, 0, 0, 0, 0, -getMov(), 0, 0, 0);

                    case 14 -> menu.editSelectedModel(0, 0, 0, 0, 0, 0, getMov(), 0, 0);
                    case 15 -> menu.editSelectedModel(0, 0, 0, 0, 0, 0, 0, getMov(), 0);
                    case 16 -> menu.editSelectedModel(0, 0, 0, 0, 0, 0, 0, 0, getMov());
                    case 17 -> menu.editSelectedModel(0, 0, 0, 0, 0, 0, -getMov(), 0, 0);
                    case 18 -> menu.editSelectedModel(0, 0, 0, 0, 0, 0, 0, -getMov(), 0);
                    case 19 -> menu.editSelectedModel(0, 0, 0, 0, 0, 0, 0, 0, -getMov());

                    case 20 -> menu.addSelectedModel();
                    case 21 -> menu.removeSelectedModel();
                }
            }
        });
    }
}
