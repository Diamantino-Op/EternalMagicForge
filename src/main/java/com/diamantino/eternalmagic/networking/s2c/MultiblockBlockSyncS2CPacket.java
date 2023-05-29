package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.blockentities.ShrineCoreBlockEntity;
import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MultiblockBlockSyncS2CPacket {
    private final List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks;
    private final BlockPos pos;

    public MultiblockBlockSyncS2CPacket(List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks, BlockPos pos) {
        this.multiblockTemplateBlocks = multiblockTemplateBlocks;
        this.pos = pos;
    }

    public MultiblockBlockSyncS2CPacket(FriendlyByteBuf buf) {
        this.multiblockTemplateBlocks = new ArrayList<>();

        this.pos = buf.readBlockPos();

        int len = buf.readInt();

        for (int i = 0; i < len; i++) {
            BlockPos pos = buf.readBlockPos();
            BlockState state = buf.readById(Block.BLOCK_STATE_REGISTRY);

            multiblockTemplateBlocks.add(new StructureTemplate.StructureBlockInfo(pos, state, null));
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);

        buf.writeInt(multiblockTemplateBlocks.size());

        for (StructureTemplate.StructureBlockInfo block : multiblockTemplateBlocks) {
            BlockState state = block.state;
            BlockPos pos = block.pos;

            buf.writeBlockPos(pos);
            buf.writeId(Block.BLOCK_STATE_REGISTRY, state);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof ShrineCoreBlockEntity blockEntity) {
                blockEntity.multiblockTemplateBlocks.clear();

                blockEntity.multiblockTemplateBlocks.addAll(multiblockTemplateBlocks);
            }
        });
    }
}
