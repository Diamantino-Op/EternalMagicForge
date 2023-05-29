package com.diamantino.eternalmagic.multiblocks;

import com.diamantino.eternalmagic.ModReferences;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MultiblockUtils {
    @Nullable
    public static StructureTemplate loadMultiblockNbt(Level level, ResourceLocation multiblockRL) {
        if (level.getServer() != null) {
            Optional<StructureTemplate> template = level.getServer().getStructureManager().get(multiblockRL);

            if (template.isPresent()) {
                return template.get();
            }
        }

        return null;
    }

    public static void sendWrongBlockMsg(Player player, BlockPos pos, String placedBlock, String expectedBlock) {
        player.sendSystemMessage(Component.translatable("msg." + ModReferences.modId + ".wrong_block_at", pos.getX(), pos.getY(), pos.getZ(), placedBlock, expectedBlock));
    }
}
