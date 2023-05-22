package com.diamantino.eternalmagic.blockentities;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.api.mana.IManaStorage;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import com.diamantino.eternalmagic.client.model.Model;
import com.diamantino.eternalmagic.items.WandCoreItem;
import com.diamantino.eternalmagic.items.WandItem;
import com.diamantino.eternalmagic.items.WandUpgradeItem;
import com.diamantino.eternalmagic.networking.s2c.ItemStackSyncS2CPacket;
import com.diamantino.eternalmagic.networking.s2c.ManaSyncS2CPacket;
import com.diamantino.eternalmagic.networking.s2c.RequiredManaSyncS2CPacket;
import com.diamantino.eternalmagic.networking.s2c.WandBenchWandSyncS2CPacket;
import com.diamantino.eternalmagic.registration.ModBlockEntityTypes;
import com.diamantino.eternalmagic.registration.ModCapabilities;
import com.diamantino.eternalmagic.registration.ModMessages;
import com.diamantino.eternalmagic.storage.mana.ModManaStorage;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WandBenchBlockEntity extends BlockEntity implements MenuProvider {
    private long requiredMana = 0;

    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
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
                case 0 -> stack.getOrCreateTag().contains("mana") && !(stack.getItem() instanceof WandItem);
                case 1 -> stack.getItem() instanceof WandUpgradeItem;
                case 2 -> stack.getItem() instanceof WandCoreItem;
                case 3 -> stack.getItem() instanceof WandItem;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final ModManaStorage manaStorage = new ModManaStorage(1000000, 1024) {
        @Override
        public void onManaChanged() {
            setChanged();

            ModMessages.sendToClients(new ManaSyncS2CPacket(this.mana, getBlockPos()));
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IManaStorage> lazyManaHandler = LazyOptional.empty();

    public WandBenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.wandBenchBlockEntity.get(), pos, state);
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

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu." + ModReferences.modId + ".wand_bench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        ModMessages.sendToClients(new ManaSyncS2CPacket(this.manaStorage.getManaStored(), getBlockPos()));
        return new WandBenchMenu(id, inventory, this);
    }

    public IManaStorage getManaStorage() {
        return manaStorage;
    }

    public void setManaLevel(long mana) {
        this.manaStorage.setMana(mana);
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

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.put("manaStorage", manaStorage.serializeNBT());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        manaStorage.deserializeNBT(nbt.get("manaStorage"));

        super.load(nbt);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, lazyItemHandler);
        }

        if (cap == ModCapabilities.mana) {
            return ModCapabilities.mana.orEmpty(cap, lazyManaHandler);
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyManaHandler = LazyOptional.of(() -> manaStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        lazyItemHandler.invalidate();
        lazyManaHandler.invalidate();
    }

    public void dropContent() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        if (this.level != null)
            Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private static void extractMana(WandBenchBlockEntity blockEntity, long amount) {
        blockEntity.manaStorage.extractMana(amount, false);
    }

    private static void receiveMana(WandBenchBlockEntity blockEntity, long amount) {
        blockEntity.manaStorage.receiveMana(amount, false);
    }

    private static boolean hasEnoughMana(WandBenchBlockEntity blockEntity, long requiredMana) {
        return blockEntity.manaStorage.getManaStored() >= requiredMana;
    }
}
