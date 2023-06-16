package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.api.capabilities.mana.IManaStorage;
import com.diamantino.eternalmagic.api.capabilities.mana.ManaStorage;
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
        super(ModBlockEntityTypes.manaPipeBlockEntity.get(), pPos, pBlockState, 10240, 10240);

        this.manaStorage.topFace = ManaStorage.SideInfo.both;
        this.manaStorage.bottomFace = ManaStorage.SideInfo.both;
        this.manaStorage.northFace = ManaStorage.SideInfo.both;
        this.manaStorage.southFace = ManaStorage.SideInfo.both;
        this.manaStorage.eastFace = ManaStorage.SideInfo.both;
        this.manaStorage.westFace = ManaStorage.SideInfo.both;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaPipeBlockEntity blockEntity) {
        IManaStorage pipeManaStorage = blockEntity.getManaStorage();

        if (state.getOptionalValue(ManaPipeBlock.connectedTop).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.UP));

            if (entity != null) {
                entity.getCapability(ModCapabilities.mana, Direction.DOWN).ifPresent(manaStorage -> {
                    if (manaStorage.canIO(Direction.DOWN)) {
                        equalizeMana(pipeManaStorage, manaStorage);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canExtract(Direction.DOWN)) {
                        long extractedMana = manaStorage.extractMana(Direction.DOWN, manaStorage.getManaStored(), true);

                        long receivedMana = pipeManaStorage.receiveMana(Direction.UP, extractedMana, false);

                        manaStorage.extractMana(Direction.DOWN, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canReceive(Direction.DOWN)) {
                        long extractedMana = pipeManaStorage.extractMana(Direction.UP, pipeManaStorage.getManaStored(), true);

                        long receivedMana = manaStorage.receiveMana(Direction.DOWN, extractedMana, false);

                        pipeManaStorage.extractMana(Direction.UP, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    }
                });
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedBottom).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.DOWN));

            if (entity != null) {
                entity.getCapability(ModCapabilities.mana, Direction.UP).ifPresent(manaStorage -> {
                    if (manaStorage.canIO(Direction.UP)) {
                        equalizeMana(pipeManaStorage, manaStorage);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canExtract(Direction.UP)) {
                        long extractedMana = manaStorage.extractMana(Direction.UP, manaStorage.getManaStored(), true);

                        long receivedMana = pipeManaStorage.receiveMana(Direction.DOWN, extractedMana, false);

                        manaStorage.extractMana(Direction.UP, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canReceive(Direction.UP)) {
                        long extractedMana = pipeManaStorage.extractMana(Direction.DOWN, pipeManaStorage.getManaStored(), true);

                        long receivedMana = manaStorage.receiveMana(Direction.UP, extractedMana, false);

                        pipeManaStorage.extractMana(Direction.DOWN, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    }
                });
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedNorth).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.NORTH));

            if (entity != null) {
                entity.getCapability(ModCapabilities.mana, Direction.SOUTH).ifPresent(manaStorage -> {
                    if (manaStorage.canIO(Direction.SOUTH)) {
                        equalizeMana(pipeManaStorage, manaStorage);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canExtract(Direction.SOUTH)) {
                        long extractedMana = manaStorage.extractMana(Direction.SOUTH, manaStorage.getManaStored(), true);

                        long receivedMana = pipeManaStorage.receiveMana(Direction.NORTH, extractedMana, false);

                        manaStorage.extractMana(Direction.SOUTH, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canReceive(Direction.SOUTH)) {
                        long extractedMana = pipeManaStorage.extractMana(Direction.NORTH, pipeManaStorage.getManaStored(), true);

                        long receivedMana = manaStorage.receiveMana(Direction.SOUTH, extractedMana, false);

                        pipeManaStorage.extractMana(Direction.NORTH, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    }
                });
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedSouth).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.SOUTH));

            if (entity != null) {
                entity.getCapability(ModCapabilities.mana, Direction.NORTH).ifPresent(manaStorage -> {
                    if (manaStorage.canIO(Direction.NORTH)) {
                        equalizeMana(pipeManaStorage, manaStorage);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canExtract(Direction.NORTH)) {
                        long extractedMana = manaStorage.extractMana(Direction.NORTH, manaStorage.getManaStored(), true);

                        long receivedMana = pipeManaStorage.receiveMana(Direction.SOUTH, extractedMana, false);

                        manaStorage.extractMana(Direction.NORTH, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canReceive(Direction.NORTH)) {
                        long extractedMana = pipeManaStorage.extractMana(Direction.SOUTH, pipeManaStorage.getManaStored(), true);

                        long receivedMana = manaStorage.receiveMana(Direction.NORTH, extractedMana, false);

                        pipeManaStorage.extractMana(Direction.SOUTH, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    }
                });
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedEast).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.EAST));

            if (entity != null) {
                entity.getCapability(ModCapabilities.mana, Direction.WEST).ifPresent(manaStorage -> {
                    if (manaStorage.canIO(Direction.WEST)) {
                        equalizeMana(pipeManaStorage, manaStorage);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canExtract(Direction.WEST)) {
                        long extractedMana = manaStorage.extractMana(Direction.WEST, manaStorage.getManaStored(), true);

                        long receivedMana = pipeManaStorage.receiveMana(Direction.EAST, extractedMana, false);

                        manaStorage.extractMana(Direction.WEST, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canReceive(Direction.WEST)) {
                        long extractedMana = pipeManaStorage.extractMana(Direction.EAST, pipeManaStorage.getManaStored(), true);

                        long receivedMana = manaStorage.receiveMana(Direction.WEST, extractedMana, false);

                        pipeManaStorage.extractMana(Direction.EAST, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    }
                });
            }
        }

        if (state.getOptionalValue(ManaPipeBlock.connectedWest).orElse(false)) {
            BlockEntity entity = level.getBlockEntity(pos.relative(Direction.WEST));

            if (entity != null) {
                entity.getCapability(ModCapabilities.mana, Direction.WEST).ifPresent(manaStorage -> {
                    if (manaStorage.canIO(Direction.WEST)) {
                        equalizeMana(pipeManaStorage, manaStorage);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canExtract(Direction.WEST)) {
                        long extractedMana = manaStorage.extractMana(Direction.WEST, manaStorage.getManaStored(), true);

                        long receivedMana = pipeManaStorage.receiveMana(Direction.EAST, extractedMana, false);

                        manaStorage.extractMana(Direction.WEST, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    } else if (manaStorage.canReceive(Direction.WEST)) {
                        long extractedMana = pipeManaStorage.extractMana(Direction.EAST, pipeManaStorage.getManaStored(), true);

                        long receivedMana = manaStorage.receiveMana(Direction.WEST, extractedMana, false);

                        pipeManaStorage.extractMana(Direction.EAST, receivedMana, false);

                        entity.setChanged();
                        blockEntity.setChanged();
                    }
                });
            }
        }
    }

    private static void equalizeMana(IManaStorage manaStorage1, IManaStorage manaStorage2) {
        long temp = (manaStorage1.getManaStored() + manaStorage2.getManaStored()) / 2;

        manaStorage1.setManaStored(temp);
        manaStorage2.setManaStored(temp);
    }
}
