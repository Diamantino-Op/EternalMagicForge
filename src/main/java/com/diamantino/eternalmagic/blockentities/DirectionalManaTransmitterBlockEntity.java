package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public class DirectionalManaTransmitterBlockEntity extends ManaTransceiverBlockEntityBase {
    public DirectionalManaTransmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.directionalManaTransmitterBlockEntity.get(), pPos, pBlockState, 20480, 2048, true, false);
    }

    @Override
    public boolean canLink(BlockPos targetPos, ManaTransceiverBlockEntityBase blockEntity) {
        Level beLevel = blockEntity.getLevel();

        Optional<Direction> facing1 = this.getBlockState().getOptionalValue(BlockStateProperties.FACING);
        Optional<Direction> facing2 = blockEntity.getBlockState().getOptionalValue(BlockStateProperties.FACING);

        if (this.level != null && beLevel != null && beLevel.dimension() == this.level.dimension() && facing1.isPresent() && facing2.isPresent() && isInRange(this.getBlockPos(), targetPos, facing1.get(), facing2.get())) {
            return blockEntity.targetPos.isEmpty() && blockEntity instanceof ManaReceiverBlockEntity;
        }

        return false;
    }

    private boolean isInRange(BlockPos pos1, BlockPos pos2, Direction facing1, Direction facing2) {
        if (facing1 == facing2.getOpposite()) {
            return switch (facing1) {
                case DOWN -> pos1.getY() > pos2.getY() && pos1.getX() == pos2.getX() && pos1.getZ() == pos2.getZ() && Math.abs(pos1.getY() - pos2.getY()) <= 128;
                case UP -> pos1.getY() < pos2.getY() && pos1.getX() == pos2.getX() && pos1.getZ() == pos2.getZ() && Math.abs(pos1.getY() - pos2.getY()) <= 128;
                case NORTH -> pos1.getZ() > pos2.getZ() && pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && Math.abs(pos1.getZ() - pos2.getZ()) <= 128;
                case SOUTH -> pos1.getZ() < pos2.getZ() && pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && Math.abs(pos1.getZ() - pos2.getZ()) <= 128;
                case WEST -> pos1.getX() > pos2.getX() && pos1.getZ() == pos2.getZ() && pos1.getY() == pos2.getY() && Math.abs(pos1.getX() - pos2.getX()) <= 128;
                case EAST -> pos1.getX() < pos2.getX() && pos1.getZ() == pos2.getZ() && pos1.getY() == pos2.getY() && Math.abs(pos1.getX() - pos2.getX()) <= 128;
            };
        }

        return false;
    }
}
