package com.diamantino.eternalmagic.multiblocks;

import com.diamantino.eternalmagic.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
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

    public static void sendWrongBlockMsg(Player player, BlockPos pos, String placedBlock, String expectedBlock) {
        player.sendSystemMessage(Component.translatable("msg." + ModConstants.modId + ".wrong_block_at", pos.getX(), pos.getY(), pos.getZ(), placedBlock, expectedBlock));
    }
}
