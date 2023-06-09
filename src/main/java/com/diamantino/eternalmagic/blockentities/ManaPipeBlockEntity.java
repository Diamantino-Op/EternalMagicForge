package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.api.mana.ManaStorage;
import com.diamantino.eternalmagic.blocks.ManaPipeBlock;
import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import com.diamantino.eternalmagic.registration.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ManaPipeBlockEntity extends ManaBlockEntityBase {
    public ManaPipeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.manaPipeBlockEntity.get(), pPos, pBlockState, 10240, 1024);

        this.manaStorage.topFace = ManaStorage.SideInfo.both;
        this.manaStorage.bottomFace = ManaStorage.SideInfo.both;
        this.manaStorage.northFace = ManaStorage.SideInfo.both;
        this.manaStorage.southFace = ManaStorage.SideInfo.both;
        this.manaStorage.eastFace = ManaStorage.SideInfo.both;
        this.manaStorage.westFace = ManaStorage.SideInfo.both;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaPipeBlockEntity blockEntity) {
        if (state.getOptionalValue(ManaPipeBlock.connectedTop).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.UP));

            if (entity != null) {
                if (entity instanceof ManaPipeBlockEntity manaPipeBlockEntity) {
                    equalizeMana(blockEntity, manaPipeBlockEntity);
                } else {
                    entity.getCapability(ModCapabilities.mana, Direction.DOWN).ifPresent(manaStorage -> blockEntity.extractMana(null, manaStorage.receiveMana(Direction.DOWN, blockEntity.getManaStorage().getManaStored(), false), false));
                    entity.getCapability(ModCapabilities.mana, Direction.DOWN).ifPresent(manaStorage -> manaStorage.extractMana(Direction.DOWN, blockEntity.receiveMana(null, manaStorage.getManaStored(), false), false));
                }
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedBottom).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.DOWN));

            if (entity != null) {
                if (entity instanceof ManaPipeBlockEntity manaPipeBlockEntity) {
                    equalizeMana(blockEntity, manaPipeBlockEntity);
                } else {
                    entity.getCapability(ModCapabilities.mana, Direction.UP).ifPresent(manaStorage -> blockEntity.extractMana(null, manaStorage.receiveMana(Direction.UP, blockEntity.getManaStorage().getManaStored(), false), false));
                    entity.getCapability(ModCapabilities.mana, Direction.UP).ifPresent(manaStorage -> manaStorage.extractMana(Direction.UP, blockEntity.receiveMana(null, manaStorage.getManaStored(), false), false));
                }
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedNorth).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.NORTH));

            if (entity != null) {
                if (entity instanceof ManaPipeBlockEntity manaPipeBlockEntity) {
                    equalizeMana(blockEntity, manaPipeBlockEntity);
                } else {
                    entity.getCapability(ModCapabilities.mana, Direction.SOUTH).ifPresent(manaStorage -> blockEntity.extractMana(null, manaStorage.receiveMana(Direction.SOUTH, blockEntity.getManaStorage().getManaStored(), false), false));
                    entity.getCapability(ModCapabilities.mana, Direction.SOUTH).ifPresent(manaStorage -> manaStorage.extractMana(Direction.SOUTH, blockEntity.receiveMana(null, manaStorage.getManaStored(), false), false));
                }
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedSouth).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.SOUTH));

            if (entity != null) {
                if (entity instanceof ManaPipeBlockEntity manaPipeBlockEntity) {
                    equalizeMana(blockEntity, manaPipeBlockEntity);
                } else {
                    entity.getCapability(ModCapabilities.mana, Direction.NORTH).ifPresent(manaStorage -> blockEntity.extractMana(null, manaStorage.receiveMana(Direction.NORTH, blockEntity.getManaStorage().getManaStored(), false), false));
                    entity.getCapability(ModCapabilities.mana, Direction.NORTH).ifPresent(manaStorage -> manaStorage.extractMana(Direction.NORTH, blockEntity.receiveMana(null, manaStorage.getManaStored(), false), false));
                }
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedEast).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.EAST));

            if (entity != null) {
                if (entity instanceof ManaPipeBlockEntity manaPipeBlockEntity) {
                    equalizeMana(blockEntity, manaPipeBlockEntity);
                } else {
                    entity.getCapability(ModCapabilities.mana, Direction.WEST).ifPresent(manaStorage -> blockEntity.extractMana(null, manaStorage.receiveMana(Direction.WEST, blockEntity.getManaStorage().getManaStored(), false), false));
                    entity.getCapability(ModCapabilities.mana, Direction.WEST).ifPresent(manaStorage -> manaStorage.extractMana(Direction.WEST, blockEntity.receiveMana(null, manaStorage.getManaStored(), false), false));
                }
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedWest).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.WEST));

            if (entity != null) {
                if (entity instanceof ManaPipeBlockEntity manaPipeBlockEntity) {
                    equalizeMana(blockEntity, manaPipeBlockEntity);
                } else {
                    entity.getCapability(ModCapabilities.mana, Direction.EAST).ifPresent(manaStorage -> blockEntity.extractMana(null, manaStorage.receiveMana(Direction.EAST, blockEntity.getManaStorage().getManaStored(), false), false));
                    entity.getCapability(ModCapabilities.mana, Direction.EAST).ifPresent(manaStorage -> manaStorage.extractMana(Direction.EAST, blockEntity.receiveMana(null, manaStorage.getManaStored(), false), false));
                }
            }
        }
    }

    private static void equalizeMana(ManaPipeBlockEntity manaPipeBlockEntity1, ManaPipeBlockEntity manaPipeBlockEntity2) {
        manaPipeBlockEntity1.getCapability(ModCapabilities.mana).ifPresent(manaStorage -> {
            manaPipeBlockEntity2.getCapability(ModCapabilities.mana).ifPresent(manaStorage2 -> {
                long temp = (manaStorage.getManaStored() + manaStorage2.getManaStored()) / 2;

                manaPipeBlockEntity1.setManaLevel(temp);
                manaPipeBlockEntity2.setManaLevel(temp);
            });
        });
    }
}
