package com.diamantino.eternalmagic.multiblocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

public record Multiblock(ResourceLocation resourceLocation, List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks) {
}
