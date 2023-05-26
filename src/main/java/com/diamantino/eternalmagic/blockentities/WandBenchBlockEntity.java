package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.api.mana.ItemStackManaStorage;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import com.diamantino.eternalmagic.client.model.Model;
import com.diamantino.eternalmagic.items.CoreItem;
import com.diamantino.eternalmagic.items.WandItem;
import com.diamantino.eternalmagic.items.WandUpgradeItem;
import com.diamantino.eternalmagic.networking.s2c.ItemStackSyncS2CPacket;
import com.diamantino.eternalmagic.networking.s2c.RequiredManaSyncS2CPacket;
import com.diamantino.eternalmagic.networking.s2c.WandBenchWandSyncS2CPacket;
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

public class WandBenchBlockEntity extends ManaBlockEntityBase implements MenuProvider {
    private long requiredMana = 0;

    private final ItemStackHandler itemHandler;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 600;

    public WandBenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.wandBenchBlockEntity.get(), pos, state, 1000000, 1024);

        this.itemHandler = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();

                if(level != null && !level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));

                    if (slot == 3)
                        ModMessages.sendToClients(new WandBenchWandSyncS2CPacket(this.getStackInSlot(3), true));
                }
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case 0 -> stack.getCapability(ItemStackManaStorage.itemStackManaStorageCapability).isPresent() && !(stack.getItem() instanceof WandItem);
                    case 1 -> stack.getItem() instanceof WandUpgradeItem;
                    case 2 -> stack.getItem() instanceof CoreItem;
                    case 3 -> stack.getItem() instanceof WandItem;
                    default -> super.isItemValid(slot, stack);
                };
            }
        };

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> WandBenchBlockEntity.this.progress;
                    case 1 -> WandBenchBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> WandBenchBlockEntity.this.progress = value;
                    case 1 -> WandBenchBlockEntity.this.maxProgress = value;
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
        return itemHandler.getStackInSlot(3);
    }

    public long getRequiredMana() {
        return requiredMana;
    }

    public void changeRequiredMana(long requiredMana) {
        setRequiredMana(requiredMana);

        ModMessages.sendToClients(new RequiredManaSyncS2CPacket(requiredMana, worldPosition));
    }

    public void setRequiredMana(long requiredMana) {
        this.requiredMana = requiredMana;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WandBenchBlockEntity blockEntity) {
        if(level.isClientSide()) {
            return;
        }

        if (hasItemInSlot(blockEntity, 0))
            transferManaToTank(blockEntity);

        ItemStack upgradeStack = getItemInSlot(blockEntity, 1);
        ItemStack coreStack = getItemInSlot(blockEntity, 2);
        ItemStack wandStack = getItemInSlot(blockEntity, 3);

        if (hasItemInSlot(blockEntity, 1) && hasItemInSlot(blockEntity, 3) && WandItem.canAddUpgrade(wandStack.getOrCreateTag(), WandUpgradeItem.getUpgrade(upgradeStack).upgradeType)) {
            WandUpgradeItem.WandUpgrade upgrade = WandUpgradeItem.getUpgrade(upgradeStack);
            long requiredMana = upgrade.rarity.id * 100L;
            blockEntity.changeRequiredMana(requiredMana);

            if (hasEnoughMana(blockEntity, requiredMana)) {
                blockEntity.progress++;
                extractMana(blockEntity, requiredMana, false);
                setChanged(level, pos, state);

                if(blockEntity.progress >= blockEntity.maxProgress) {
                    WandItem.addUpgrade(wandStack, upgrade);
                }
            }
        } else if (hasItemInSlot(blockEntity, 2) && hasItemInSlot(blockEntity, 3)) {
            CoreItem.WandCoreElement coreElement = ((CoreItem) coreStack.getItem()).getElement();
            long requiredMana = (Math.max(CoreItem.getLevel(coreStack.getOrCreateTag()), 1) * 1000L) * (coreElement == CoreItem.WandCoreElement.infinity ? 10 : 1);
            blockEntity.changeRequiredMana(requiredMana);

            if (hasEnoughMana(blockEntity, requiredMana)) {
                blockEntity.progress++;
                extractMana(blockEntity, requiredMana, false);
                setChanged(level, pos, state);

                if(blockEntity.progress >= blockEntity.maxProgress) {
                    WandItem.setCore(wandStack, coreStack.getOrCreateTag());
                }
            }
        } else {
            blockEntity.resetProgress();
            blockEntity.changeRequiredMana(0);
            setChanged(level, pos, state);
        }
    }

    public static void transferManaToTank(WandBenchBlockEntity blockEntity) {
        blockEntity.itemHandler.getStackInSlot(0).getCapability(ItemStackManaStorage.itemStackManaStorageCapability).ifPresent(itemStackManaStorage -> {
            long manaExtract = itemStackManaStorage.extractMana(blockEntity.manaStorage.getMaxManaStored() - blockEntity.manaStorage.getManaStored(), false);

            receiveMana(blockEntity, manaExtract, false);
        });
    }

    public static boolean hasItemInSlot(WandBenchBlockEntity blockEntity, int slot) {
        return !blockEntity.itemHandler.getStackInSlot(slot).isEmpty();
    }

    public static ItemStack getItemInSlot(WandBenchBlockEntity blockEntity, int slot) {
        return blockEntity.itemHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu." + ModReferences.modId + ".wand_bench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        syncMana();

        return new WandBenchMenu(id, inventory, this, this.data);
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    public int getNextModelId() {
        return WandItem.getFirstFreeId(itemHandler.getStackInSlot(3).getTag());
    }

    public void addModelToItem(Model model) {
        ItemStack stack = itemHandler.getStackInSlot(3);

        WandItem.addPart(stack.getOrCreateTag(), model);

        ModMessages.sendToClients(new WandBenchWandSyncS2CPacket(stack, false));
    }

    public void setModelSelected(int id, boolean val) {
        ItemStack stack = itemHandler.getStackInSlot(3);

        WandItem.editPart(stack.getOrCreateTag(), id, 0, 0, 0, 0, 0, 0, 0, 0, 0, val, false);

        ModMessages.sendToClients(new WandBenchWandSyncS2CPacket(stack, false));
    }

    public void editModelToItem(int id, float transX, float transY, float transZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        ItemStack stack = itemHandler.getStackInSlot(3);

        WandItem.editPart(stack.getOrCreateTag(), id, transX, transY, transZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, false, true);

        ModMessages.sendToClients(new WandBenchWandSyncS2CPacket(stack, false));
    }

    public void removeModelFromItem(int id) {
        ItemStack stack = itemHandler.getStackInSlot(3);

        WandItem.removePart(stack.getOrCreateTag(), id);

        ModMessages.sendToClients(new WandBenchWandSyncS2CPacket(stack, false));
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("progress", this.progress);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("progress");

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
