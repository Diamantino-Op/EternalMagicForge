package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ManaReceiverBlockEntity extends ManaTransceiverBlockEntityBase {
    public ManaReceiverBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.manaReceiverBlockEntity.get(), pPos, pBlockState, 20480, 2048, false, false);
    }

    @Override
    public boolean canLink(BlockPos targetPos, ManaTransceiverBlockEntityBase blockEntity) {
        Level beLevel = blockEntity.getLevel();

        if (this.level != null && beLevel != null && beLevel.dimension() == this.level.dimension()) {
            if (blockEntity instanceof DirectionalManaTransmitterBlockEntity && blockEntity.targetPos.isEmpty())
                return true;
            else return blockEntity instanceof OmnidirectionalManaTransmitterBlockEntity;
        }

        return false;
    }
}
