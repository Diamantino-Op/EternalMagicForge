package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ManaTunnelBlockEntity extends ManaTransceiverBlockEntityBase {
    public ManaTunnelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.manaTunnelBlockEntity.get(), pPos, pBlockState, 102400, 10240, false, true);
    }

    @Override
    public boolean canLink(BlockPos targetPos, ManaTransceiverBlockEntityBase blockEntity) {
        return blockEntity.targetPos.isEmpty() && blockEntity instanceof ManaTunnelBlockEntity;
    }
}
