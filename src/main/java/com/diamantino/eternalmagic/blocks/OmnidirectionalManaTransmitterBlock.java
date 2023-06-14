package com.diamantino.eternalmagic.blocks;

import com.diamantino.eternalmagic.blockentities.OmnidirectionalManaTransmitterBlockEntity;
import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OmnidirectionalManaTransmitterBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public OmnidirectionalManaTransmitterBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new OmnidirectionalManaTransmitterBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.omnidirectionalManaTransmitterBlockEntity.get(), OmnidirectionalManaTransmitterBlockEntity::tick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity instanceof OmnidirectionalManaTransmitterBlockEntity omnidirectionalManaTransmitterBlockEntity)
            omnidirectionalManaTransmitterBlockEntity.onClick(pPlayer, pHand);

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void onPlace(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pOldState, boolean pIsMoving) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity instanceof OmnidirectionalManaTransmitterBlockEntity omnidirectionalManaTransmitterBlockEntity) {
            omnidirectionalManaTransmitterBlockEntity.onPlace(pState);
        }

        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof OmnidirectionalManaTransmitterBlockEntity omnidirectionalManaTransmitterBlockEntity) {
                omnidirectionalManaTransmitterBlockEntity.onBreak();
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
