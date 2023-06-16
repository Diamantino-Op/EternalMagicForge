package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.api.capabilities.mana.ManaStorage;
import com.diamantino.eternalmagic.registration.ModCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ManaTransceiverBlockEntityBase extends ManaBlockEntityBase {
    public ResourceKey<Level> targetLevel;
    public List<BlockPos> targetPos;

    public final boolean isTransmitter;
    public final boolean isTunnel;

    public ManaTransceiverBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, long capacity, long maxTransfer, boolean isTransmitter, boolean isTunnel) {
        super(pType, pPos, pBlockState, capacity, maxTransfer);

        this.targetLevel = null;
        this.targetPos = new ArrayList<>();

        this.isTransmitter = isTransmitter;
        this.isTunnel = isTunnel;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaTransceiverBlockEntityBase blockEntity) {
        MinecraftServer server = level.getServer();

        if (server != null) {
            Level targetLevel = server.getLevel(blockEntity.targetLevel);

            if (targetLevel != null) {
                for (BlockPos targetPos : blockEntity.targetPos) {
                    BlockEntity entityTemp = targetLevel.getBlockEntity(targetPos);

                    if (entityTemp instanceof ManaTransceiverBlockEntityBase entity) {
                        if (blockEntity.isTunnel) {
                            equalizeMana(blockEntity, entity);
                        } else {
                            entity.getCapability(ModCapabilities.mana, null).ifPresent(manaStorage -> {
                                if (blockEntity.isTransmitter) {
                                    long extractedMana = blockEntity.manaStorage.extractMana(null, blockEntity.manaStorage.getManaStored(), true);

                                    long receivedMana = manaStorage.receiveMana(null, extractedMana, false);

                                    blockEntity.manaStorage.extractMana(null, receivedMana, false);

                                    entity.setChanged();
                                    blockEntity.setChanged();
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private static void equalizeMana(ManaTransceiverBlockEntityBase manaPipeBlockEntity1, ManaTransceiverBlockEntityBase manaPipeBlockEntity2) {
        manaPipeBlockEntity1.getCapability(ModCapabilities.mana).ifPresent(manaStorage -> {
            manaPipeBlockEntity2.getCapability(ModCapabilities.mana).ifPresent(manaStorage2 -> {
                long temp = (manaStorage.getManaStored() + manaStorage2.getManaStored()) / 2;

                manaPipeBlockEntity1.setManaLevel(temp);
                manaPipeBlockEntity2.setManaLevel(temp);
            });
        });
    }

    public void onPlace(BlockState state) {
        Optional<Direction> facing = state.getOptionalValue(BlockStateProperties.FACING);

        if (facing.isPresent()) {
            ManaStorage.SideInfo outputInfo = isTunnel ? ManaStorage.SideInfo.both : (isTransmitter ? ManaStorage.SideInfo.insert : ManaStorage.SideInfo.extract);

            switch (facing.get()) {
                case NORTH -> {
                    this.manaStorage.northFace = ManaStorage.SideInfo.none;
                    this.manaStorage.southFace = outputInfo;
                    this.manaStorage.eastFace = ManaStorage.SideInfo.none;
                    this.manaStorage.westFace = ManaStorage.SideInfo.none;
                    this.manaStorage.topFace = ManaStorage.SideInfo.none;
                    this.manaStorage.bottomFace = ManaStorage.SideInfo.none;
                }
                case SOUTH -> {
                    this.manaStorage.northFace = outputInfo;
                    this.manaStorage.southFace = ManaStorage.SideInfo.none;
                    this.manaStorage.eastFace = ManaStorage.SideInfo.none;
                    this.manaStorage.westFace = ManaStorage.SideInfo.none;
                    this.manaStorage.topFace = ManaStorage.SideInfo.none;
                    this.manaStorage.bottomFace = ManaStorage.SideInfo.none;
                }
                case EAST -> {
                    this.manaStorage.northFace = ManaStorage.SideInfo.none;
                    this.manaStorage.southFace = ManaStorage.SideInfo.none;
                    this.manaStorage.eastFace = ManaStorage.SideInfo.none;
                    this.manaStorage.westFace = outputInfo;
                    this.manaStorage.topFace = ManaStorage.SideInfo.none;
                    this.manaStorage.bottomFace = ManaStorage.SideInfo.none;
                }
                case WEST -> {
                    this.manaStorage.northFace = ManaStorage.SideInfo.none;
                    this.manaStorage.southFace = ManaStorage.SideInfo.none;
                    this.manaStorage.eastFace = outputInfo;
                    this.manaStorage.westFace = ManaStorage.SideInfo.none;
                    this.manaStorage.topFace = ManaStorage.SideInfo.none;
                    this.manaStorage.bottomFace = ManaStorage.SideInfo.none;
                }
                case UP -> {
                    this.manaStorage.northFace = ManaStorage.SideInfo.none;
                    this.manaStorage.southFace = ManaStorage.SideInfo.none;
                    this.manaStorage.eastFace = ManaStorage.SideInfo.none;
                    this.manaStorage.westFace = ManaStorage.SideInfo.none;
                    this.manaStorage.topFace = ManaStorage.SideInfo.none;
                    this.manaStorage.bottomFace = outputInfo;
                }
                case DOWN -> {
                    this.manaStorage.northFace = ManaStorage.SideInfo.none;
                    this.manaStorage.southFace = ManaStorage.SideInfo.none;
                    this.manaStorage.eastFace = ManaStorage.SideInfo.none;
                    this.manaStorage.westFace = ManaStorage.SideInfo.none;
                    this.manaStorage.topFace = outputInfo;
                    this.manaStorage.bottomFace = ManaStorage.SideInfo.none;
                }
            }
        }
    }

    public void onBreak() {
        if (level != null) {
            MinecraftServer server = level.getServer();

            if (server != null) {
                Level targetLevel = server.getLevel(this.targetLevel);

                if (targetLevel != null) {
                    for (BlockPos targetPos : this.targetPos) {
                        BlockEntity entityTemp = targetLevel.getBlockEntity(targetPos);

                        if (entityTemp instanceof ManaTransceiverBlockEntityBase entity) {
                            entity.targetPos.remove(this.getBlockPos());
                        }
                    }
                }
            }
        }
    }

    public void onClick(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        CompoundTag tag = stack.getOrCreateTag();

        if (!player.isShiftKeyDown() && tag.contains("savedTarget")) {
            CompoundTag targetTag = tag.getCompound("savedTarget");

            BlockPos savedPos = new BlockPos(targetTag.getInt("x"), targetTag.getInt("y"), targetTag.getInt("z"));
            ResourceKey<Level> targetLvlKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(targetTag.getString("level")));

            if (this.level != null) {
                MinecraftServer server = level.getServer();

                if (server != null) {
                    Level targetLvl = server.getLevel(targetLvlKey);

                    if (targetLvl != null && targetLvl.getBlockEntity(savedPos) instanceof ManaTransceiverBlockEntityBase blockEntityBase && blockEntityBase.level != null && canLink(savedPos, blockEntityBase) && blockEntityBase.canLink(this.getBlockPos(), this)) {
                        blockEntityBase.targetPos.add(this.getBlockPos());
                        blockEntityBase.targetLevel = this.level.dimension();

                        this.targetPos.add(savedPos);
                        this.targetLevel = blockEntityBase.level.dimension();

                        tag.remove("savedTarget");

                        player.sendSystemMessage(Component.translatable("message." + ModConstants.modId + ".link_successful").withStyle(ChatFormatting.GREEN));
                    } else {
                        player.sendSystemMessage(Component.translatable("message." + ModConstants.modId + ".link_error").withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }

    public abstract boolean canLink(BlockPos targetPos, ManaTransceiverBlockEntityBase blockEntity);

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);

        if (targetLevel != null)
            nbt.putString("targetLevel", targetLevel.location().toString());

        List<Tag> posTags = new ArrayList<>();

        for (BlockPos pos : targetPos) {
            CompoundTag tag = new CompoundTag();

            tag.putInt("targetX", pos.getX());
            tag.putInt("targetY", pos.getY());
            tag.putInt("targetZ", pos.getZ());

            posTags.add(tag);
        }

        nbt.put("targets", new ListTag(posTags, (byte) 10));
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        if (nbt.contains("targetLevel"))
            targetLevel = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("targetLevel")));

        for (Tag tag : nbt.getList("targets", 10)) {
            CompoundTag tagCmp = (CompoundTag) tag;

            targetPos.add(new BlockPos(tagCmp.getInt("targetX"), tagCmp.getInt("targetY"), tagCmp.getInt("targetZ")));
        }
    }
}
