package com.diamantino.eternalmagic.multiblocks;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MultiblockUtils {
    @Nullable
    public static StructureTemplate loadMultiblockNbt(MinecraftServer server, ResourceLocation multiblockRL) {
        Optional<StructureTemplate> template = server.getStructureManager().get(multiblockRL);

        return template.orElse(null);

    }

    public static ListTag encodeMultiblockBlock(List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks) {
        List<Tag> blockTags = new ArrayList<>();

        for (StructureTemplate.StructureBlockInfo block : multiblockTemplateBlocks) {
            CompoundTag blockTag = new CompoundTag();

            BlockState state = block.state;
            BlockPos pos = block.pos;

            CompoundTag posTag = new CompoundTag();

            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());

            blockTag.put("pos", posTag);

            blockTag.putInt("stateId", Block.BLOCK_STATE_REGISTRY.getId(state));

            blockTags.add(blockTag);
        }

        return new ListTag(blockTags, (byte) 10);
    }

    public static List<StructureTemplate.StructureBlockInfo> decodeMultiblockBlocks(ListTag blockTags) {
        List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks = new ArrayList<>();

        for (Tag tempTag : blockTags) {
            CompoundTag blockTag = (CompoundTag) tempTag;

            CompoundTag posTag = blockTag.getCompound("pos");

            BlockPos pos = new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z"));
            BlockState state = Block.BLOCK_STATE_REGISTRY.byId(blockTag.getInt("stateId"));

            if (state != null)
                multiblockTemplateBlocks.add(new StructureTemplate.StructureBlockInfo(pos, state, null));
        }

        return multiblockTemplateBlocks;
    }

    public static void sendWrongBlockMsg(Player player, BlockPos pos, String placedBlock, String expectedBlock) {
        player.sendSystemMessage(Component.translatable("msg." + ModReferences.modId + ".wrong_block_at", pos.getX(), pos.getY(), pos.getZ(), placedBlock, expectedBlock));
    }
}
