package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class OmnidirectionalManaTransmitterBlockEntity extends ManaTransceiverBlockEntityBase {
    public OmnidirectionalManaTransmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.omnidirectionalManaTransmitterBlockEntity.get(), pPos, pBlockState, 20480, 2048, true, false);
    }

    @Override
    public boolean canLink(BlockPos targetPos, ManaTransceiverBlockEntityBase blockEntity) {
        Level beLevel = blockEntity.getLevel();

        return this.level != null && beLevel != null && beLevel.dimension() == this.level.dimension() && blockEntity.getBlockPos().closerThan(this.getBlockPos(), 96) && blockEntity.targetPos.isEmpty() && blockEntity instanceof ManaReceiverBlockEntity;
    }
}
