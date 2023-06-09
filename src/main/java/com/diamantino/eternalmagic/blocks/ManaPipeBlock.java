package com.diamantino.eternalmagic.blocks;

import com.diamantino.eternalmagic.blockentities.ManaPipeBlockEntity;
import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import com.diamantino.eternalmagic.registration.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaPipeBlock extends BaseEntityBlock {
    public static final BooleanProperty waterlogged = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty connectedTop = BooleanProperty.create("connected_top");
    public static final BooleanProperty connectedBottom = BooleanProperty.create("connected_bottom");
    public static final BooleanProperty connectedNorth = BooleanProperty.create("connected_north");
    public static final BooleanProperty connectedSouth = BooleanProperty.create("connected_south");
    public static final BooleanProperty connectedEast = BooleanProperty.create("connected_east");
    public static final BooleanProperty connectedWest = BooleanProperty.create("connected_west");

    public ManaPipeBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any().setValue(waterlogged, false).setValue(connectedTop, false).setValue(connectedBottom, false).setValue(connectedNorth, false).setValue(connectedSouth, false).setValue(connectedEast, false).setValue(connectedWest, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new ManaPipeBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.manaPipeBlockEntity.get(), ManaPipeBlockEntity::tick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(waterlogged, connectedBottom, connectedTop, connectedNorth, connectedSouth, connectedEast, connectedWest);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        FluidState fluidstate = pContext.getLevel().getFluidState(blockpos);
        return updateState(blockpos, this.defaultBlockState().setValue(waterlogged, fluidstate.getType() == Fluids.WATER), pContext.getLevel());
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState pState) {
        return pState.getValue(waterlogged) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        VoxelShape shape = Block.box(4, 4, 4, 12, 12, 12);

        if (pState.getValue(connectedTop))
            shape = Shapes.or(shape, Block.box(5, 11, 5, 11, 16, 11));

        if (pState.getValue(connectedBottom))
            shape = Shapes.or(shape, Block.box(5, 0, 5, 11, 5, 11));

        if (pState.getValue(connectedNorth))
            shape = Shapes.or(shape, Block.box(5, 5, 0, 11, 11, 5));

        if (pState.getValue(connectedSouth))
            shape = Shapes.or(shape, Block.box(5, 5, 11, 11, 11, 16));

        if (pState.getValue(connectedEast))
            shape = Shapes.or(shape, Block.box(11, 5, 5, 16, 11, 11));

        if (pState.getValue(connectedWest))
            shape = Shapes.or(shape, Block.box(0, 5, 5, 5, 11, 11));

        return shape;
    }

    private BlockState updateState(BlockPos pos, BlockState initialState, LevelAccessor level) {
        for (Direction dir : Direction.values()) {
            BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));

            switch (dir) {
                case DOWN -> {
                    if (blockEntity != null && blockEntity.getCapability(ModCapabilities.mana, dir.getOpposite()).isPresent())
                        initialState = initialState.setValue(connectedBottom, true);
                    else
                        initialState = initialState.setValue(connectedBottom, false);
                }
                case UP -> {
                    if (blockEntity != null && blockEntity.getCapability(ModCapabilities.mana, dir.getOpposite()).isPresent())
                        initialState = initialState.setValue(connectedTop, true);
                    else
                        initialState = initialState.setValue(connectedTop, false);
                }
                case NORTH -> {
                    if (blockEntity != null && blockEntity.getCapability(ModCapabilities.mana, dir.getOpposite()).isPresent())
                        initialState = initialState.setValue(connectedNorth, true);
                    else
                        initialState = initialState.setValue(connectedNorth, false);
                }
                case SOUTH -> {
                    if (blockEntity != null && blockEntity.getCapability(ModCapabilities.mana, dir.getOpposite()).isPresent())
                        initialState = initialState.setValue(connectedSouth, true);
                    else
                        initialState = initialState.setValue(connectedSouth, false);
                }
                case WEST -> {
                    if (blockEntity != null && blockEntity.getCapability(ModCapabilities.mana, dir.getOpposite()).isPresent())
                        initialState = initialState.setValue(connectedWest, true);
                    else
                        initialState = initialState.setValue(connectedWest, false);
                }
                case EAST -> {
                    if (blockEntity != null && blockEntity.getCapability(ModCapabilities.mana, dir.getOpposite()).isPresent())
                        initialState = initialState.setValue(connectedEast, true);
                    else
                        initialState = initialState.setValue(connectedEast, false);
                }
            }
        }

        return initialState;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        if (pState.getValue(waterlogged)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return updateState(pCurrentPos, super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos), pLevel);
    }
}
