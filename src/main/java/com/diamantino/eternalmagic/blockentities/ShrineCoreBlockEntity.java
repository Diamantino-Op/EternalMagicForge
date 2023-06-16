package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.EternalMagic;
import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.api.capabilities.mana.ManaStorage;
import com.diamantino.eternalmagic.client.menu.ShrineCoreMenu;
import com.diamantino.eternalmagic.items.CoreItem;
import com.diamantino.eternalmagic.multiblocks.MultiblockLevel;
import com.diamantino.eternalmagic.multiblocks.MultiblockUtils;
import com.diamantino.eternalmagic.networking.s2c.GeneratingManaSyncS2CPacket;
import com.diamantino.eternalmagic.networking.s2c.ItemStackSyncS2CPacket;
import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import com.diamantino.eternalmagic.registration.ModBlocks;
import com.diamantino.eternalmagic.registration.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShrineCoreBlockEntity extends ManaBlockEntityBase implements MenuProvider {
    private final ItemStackHandler itemHandler;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public List<StructureTemplate.StructureBlockInfo> multiblockTemplateBlocks = new ArrayList<>();
    public boolean isAssembled = false;

    public MultiblockLevel multiblockLevel;

    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress = 20;

    public static long baseCapacity = 10240;
    public static long baseGeneratedMana = 10;

    public long generatingMana = 0;
    public float generatingManaMultiplier = 1F;
    public int coreLevel = 0;

    public int showingBlockId = 0;
    private int showingChangeCountdown = 80;

    public ShrineCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.shrineCoreBlockEntity.get(), pPos, pBlockState, baseCapacity);

        this.manaStorage.topFace = ManaStorage.SideInfo.extract;
        this.manaStorage.bottomFace = ManaStorage.SideInfo.extract;
        this.manaStorage.northFace = ManaStorage.SideInfo.extract;
        this.manaStorage.southFace = ManaStorage.SideInfo.extract;
        this.manaStorage.eastFace = ManaStorage.SideInfo.extract;
        this.manaStorage.westFace = ManaStorage.SideInfo.extract;

        this.itemHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();

                if(level != null) {
                    if (!level.isClientSide())
                        ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));

                    updateCapacity();
                }
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot == 0 ? (stack.getItem() instanceof CoreItem) : super.isItemValid(slot, stack);
            }
        };

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ShrineCoreBlockEntity.this.progress;
                    case 1 -> ShrineCoreBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ShrineCoreBlockEntity.this.progress = value;
                    case 1 -> ShrineCoreBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void updateCapacity() {
        coreLevel = CoreItem.getLevel(itemHandler.getStackInSlot(0).getOrCreateTag());

        long newCapacity = baseCapacity * Math.max(1, ((long) coreLevel * coreLevel));

        manaStorage.setCapacity(newCapacity);

        changeGeneratingMana(getTotalGeneratingMana(itemHandler.getStackInSlot(0).isEmpty() ? 0 : Math.max(1, coreLevel)));
    }

    public long getTotalGeneratingMana(int coreLevel) {
        return Math.round((baseGeneratedMana * ((long) coreLevel * coreLevel)) * generatingManaMultiplier);
    }

    @Override
    public AABB getRenderBoundingBox() {
        BlockPos pos = getBlockPos();

        return new AABB(pos.getX() - 6, pos.getY() - 6, pos.getZ() - 6, pos.getX() + 6, pos.getY() + 6, pos.getZ() + 6);
    }

    public ItemStack getRenderStack() {
        return itemHandler.getStackInSlot(0);
    }

    public long getGeneratingMana() {
        return generatingMana;
    }

    public void changeGeneratingMana(long generatingMana) {
        setGeneratingMana(generatingMana);

        if (level != null && !level.isClientSide())
            ModMessages.sendToClients(new GeneratingManaSyncS2CPacket(generatingMana, worldPosition));
    }

    public void setGeneratingMana(long generatingMana) {
        this.generatingMana = generatingMana;
    }

    public boolean onClick(Player player) {
        boolean ret = true;

        this.generatingManaMultiplier = 1;

        if (this.level != null && multiblockTemplateBlocks.size() > 0) {
            for (StructureTemplate.StructureBlockInfo structureBlockInfo : multiblockTemplateBlocks) {
                BlockPos pos = structureBlockInfo.pos().offset(-5, -4, -5);
                BlockPos worldPos = this.getBlockPos().offset(pos.getX(), pos.getY(), pos.getZ());
                BlockState state = structureBlockInfo.state();
                BlockState worldState = this.level.getBlockState(worldPos);

                if (state.is(Blocks.IRON_BLOCK)) {
                    if (worldState.is(ModBlocks.decorativeBlocks.get("dark_stone").get())) {
                        this.generatingManaMultiplier += 0f;
                    } else if (worldState.is(Blocks.IRON_BLOCK)) {
                        this.generatingManaMultiplier += 0.15f;
                    } else if (worldState.is(Blocks.GOLD_BLOCK)) {
                        this.generatingManaMultiplier += 0.25f;
                    } else if (worldState.is(Blocks.DIAMOND_BLOCK)) {
                        this.generatingManaMultiplier += 0.5f;
                    } else if (worldState.is(Blocks.EMERALD_BLOCK)) {
                        this.generatingManaMultiplier += 0.75f;
                    } else if (worldState.is(Blocks.NETHERITE_BLOCK)) {
                        this.generatingManaMultiplier += 1f;
                    } else {
                        ret = false;

                        MultiblockUtils.sendWrongBlockMsg(player, worldPos, worldState.getBlock().getName().getString(), state.getBlock().getName().getString());

                        break;
                    }
                } else {
                    if (worldState != state) {
                        ret = false;

                        MultiblockUtils.sendWrongBlockMsg(player, worldPos, worldState.getBlock().getName().getString(), state.getBlock().getName().getString());

                        break;
                    }
                }
            }
        }

        if (ret) {
            assemble();
        } else {
            disassemble();
        }

        return ret;
    }

    private void assemble() {
        this.isAssembled = true;

        BlockPos basePos = this.getBlockPos().below(3);

        if (this.level != null) {
            if (this.level.getBlockEntity(basePos.north(5)) instanceof ShrineOutputBlockEntity shrineOutputBlockEntity) {
                shrineOutputBlockEntity.corePos = this.getBlockPos();
                shrineOutputBlockEntity.isLinked = true;
            }

            if (this.level.getBlockEntity(basePos.east(5)) instanceof ShrineOutputBlockEntity shrineOutputBlockEntity) {
                shrineOutputBlockEntity.corePos = this.getBlockPos();
                shrineOutputBlockEntity.isLinked = true;
            }

            if (this.level.getBlockEntity(basePos.south(5)) instanceof ShrineOutputBlockEntity shrineOutputBlockEntity) {
                shrineOutputBlockEntity.corePos = this.getBlockPos();
                shrineOutputBlockEntity.isLinked = true;
            }

            if (this.level.getBlockEntity(basePos.west(5)) instanceof ShrineOutputBlockEntity shrineOutputBlockEntity) {
                shrineOutputBlockEntity.corePos = this.getBlockPos();
                shrineOutputBlockEntity.isLinked = true;
            }
        }
    }

    private void disassemble() {
        this.isAssembled = false;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ShrineCoreBlockEntity blockEntity) {
        if(level.isClientSide()) {
            if (!blockEntity.isAssembled) {
                if (blockEntity.showingChangeCountdown <= 0) {
                    if (blockEntity.showingBlockId == 5)
                        blockEntity.showingBlockId = 0;
                    else
                        blockEntity.showingBlockId++;

                    blockEntity.showingChangeCountdown = 80;
                }

                blockEntity.showingChangeCountdown = Math.max(blockEntity.showingChangeCountdown - 1, 0);
            }
        } else {
            ItemStack stack = blockEntity.itemHandler.getStackInSlot(0);

            if (!stack.isEmpty() && !(blockEntity.manaStorage.getManaStored() == blockEntity.manaStorage.getCapacity())) {
                blockEntity.progress++;

                if (blockEntity.progress >= blockEntity.maxProgress) {
                    blockEntity.resetProgress();
                    blockEntity.receiveMana(null, blockEntity.getGeneratingMana(), false);
                }

                setChanged(level, pos, state);
            } else {
                blockEntity.resetProgress();
                setChanged(level, pos, state);
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu." + ModConstants.modId + ".shrine_core");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        syncMana();

        return new ShrineCoreMenu(id, inventory, this, this.data);
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putFloat("generatingManaMultiplier", this.generatingManaMultiplier);
        nbt.putBoolean("isAssembled", this.isAssembled);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("progress");
        generatingManaMultiplier = nbt.getFloat("generatingManaMultiplier");
        isAssembled = nbt.getBoolean("isAssembled");

        updateCapacity();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, lazyItemHandler);
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.multiblockTemplateBlocks.clear();

        this.multiblockTemplateBlocks.addAll(EternalMagic.instance.multiblockRegistry.getMultiblockByName(new ResourceLocation(ModConstants.modId, "shrine")).multiblockTemplateBlocks());

        multiblockLevel = new MultiblockLevel(this.getLevel(), multiblockTemplateBlocks);

        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        lazyItemHandler.invalidate();
    }

    public void dropContent() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        if (this.level != null)
            Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}
