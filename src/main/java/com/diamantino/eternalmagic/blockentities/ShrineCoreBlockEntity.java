package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.client.menu.ShrineCoreMenu;
import com.diamantino.eternalmagic.items.CoreItem;
import com.diamantino.eternalmagic.networking.s2c.GeneratingManaSyncS2CPacket;
import com.diamantino.eternalmagic.networking.s2c.ItemStackSyncS2CPacket;
import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import com.diamantino.eternalmagic.registration.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShrineCoreBlockEntity extends ManaBlockEntityBase implements MenuProvider {
    private final ItemStackHandler itemHandler;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress = 20;

    private long generatingMana = 0;

    public ShrineCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.shrineCoreBlockEntity.get(), pPos, pBlockState, 10000, 2048);

        this.itemHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();

                if(level != null && !level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
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

        ModMessages.sendToClients(new GeneratingManaSyncS2CPacket(generatingMana, worldPosition));
    }

    public void setGeneratingMana(long generatingMana) {
        this.generatingMana = generatingMana;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ShrineCoreBlockEntity blockEntity) {
        if(level.isClientSide()) {
            return;
        }

        ItemStack stack = blockEntity.itemHandler.getStackInSlot(0);

        if (!stack.isEmpty()) {
            blockEntity.progress++;

            if (blockEntity.progress >= blockEntity.maxProgress) {
                blockEntity.resetProgress();
                receiveMana(blockEntity, blockEntity.getGeneratingMana(), false);
            }

            setChanged(level, pos, state);
        } else {
            blockEntity.resetProgress();
            blockEntity.changeGeneratingMana(0);
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu." + ModReferences.modId + ".shrine_core");
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
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putLong("generatingMana", this.getGeneratingMana());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("progress");
        changeGeneratingMana(nbt.getLong("generatingMana"));

        super.load(nbt);
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