package com.diamantino.eternalmagic.networking.s2c;

import com.diamantino.eternalmagic.EternalMagic;
import com.diamantino.eternalmagic.blockentities.ShrineCoreBlockEntity;
import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import com.diamantino.eternalmagic.multiblocks.Multiblock;
import com.diamantino.eternalmagic.multiblocks.MultiblockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MultiblockBlockSyncS2CPacket {
    private final List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks;
    private final ResourceLocation multiblockName;
    private final int size;

    public MultiblockBlockSyncS2CPacket(List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks, ResourceLocation multiblockName, int size) {
        this.multiblockTemplateBlocks = multiblockTemplateBlocks;
        this.multiblockName = multiblockName;
        this.size = size;
    }

    public MultiblockBlockSyncS2CPacket(FriendlyByteBuf buf) {
        this.multiblockTemplateBlocks = new ArrayList<>();

        this.size = buf.readInt();

        this.multiblockName = buf.readResourceLocation();

        int len = buf.readInt();

        for (int i = 0; i < len; i++) {
            BlockPos pos = buf.readBlockPos();
            BlockState state = buf.readById(Block.BLOCK_STATE_REGISTRY);

            if (state != null)
                multiblockTemplateBlocks.add(new StructureTemplate.StructureBlockInfo(pos, state, null));
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(size);

        buf.writeResourceLocation(multiblockName);

        buf.writeInt(multiblockTemplateBlocks.size());

        for (StructureTemplate.StructureBlockInfo block : multiblockTemplateBlocks) {
            BlockState state = block.state();
            BlockPos pos = block.pos();

            buf.writeBlockPos(pos);
            buf.writeId(Block.BLOCK_STATE_REGISTRY, state);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            MultiblockRegistry registry = EternalMagic.instance.multiblockRegistry;

            if (registry.multiblockMap.size() >= size)
                registry.multiblockMap.clear();

            registry.addMultiblock(multiblockName, new Multiblock(multiblockName, multiblockTemplateBlocks));
        });
    }
}
