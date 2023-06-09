package com.diamantino.eternalmagic.client.menu;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
import com.diamantino.eternalmagic.client.model.Model;
import com.diamantino.eternalmagic.client.model.ModelLoader;
import com.diamantino.eternalmagic.client.screens.components.EMSlot;
import com.diamantino.eternalmagic.registration.ModBlocks;
import com.diamantino.eternalmagic.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Objects;

public class WandBenchMenu extends AbstractContainerMenu {
    public final WandBenchBlockEntity blockEntity;
    private final Level level;

    private int selectedModelId;
    private final ContainerData data;
    private String selectedModelToAddId;

    public WandBenchMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public WandBenchMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.wandBenchMenu.get(), id);
        checkContainerSize(inv, 4);
        this.data = data;
        blockEntity = (WandBenchBlockEntity) entity;
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.selectedModelId = -1;
        this.selectedModelToAddId = "";

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 8, 125));
            this.addSlot(new SlotItemHandler(handler, 1, 108, 125));
            this.addSlot(new SlotItemHandler(handler, 2, 126, 125));
            this.addSlot(new EMSlot(handler, 3, 180, 125, this));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 22;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public void removed(@NotNull Player pPlayer) {
        blockEntity.setModelSelected(this.selectedModelId, false);

        super.removed(pPlayer);
    }

    public void setSelectedModelId(int selectedModelId) {
        if (this.selectedModelId != -1)
            this.blockEntity.setModelSelected(this.selectedModelId, false);

        this.selectedModelId = selectedModelId;
        this.blockEntity.setModelSelected(this.selectedModelId, true);
    }

    public int getSelectedModelId() {
        return this.selectedModelId;
    }

    public void editSelectedModel(float transX, float transY, float transZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        if (selectedModelId != -1 && blockEntity.getRenderStack() != ItemStack.EMPTY)
            blockEntity.editModelToItem(selectedModelId, transX, transY, transZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public void removeSelectedModel() {
        if (selectedModelId != -1 && blockEntity.getRenderStack() != ItemStack.EMPTY) {
            blockEntity.removeModelFromItem(selectedModelId);

            selectedModelId = -1;
        }
    }

    public void setSelectedModelToAddId(String selectedModelToAddId) {
        this.selectedModelToAddId = selectedModelToAddId;
    }

    public void addSelectedModel() {
        if (!Objects.equals(selectedModelToAddId, "") && blockEntity.getRenderStack() != ItemStack.EMPTY) {
            Model model = new Model(ModelLoader.loadedModels.getOrDefault(selectedModelToAddId, new ResourceLocation(ModConstants.modId, "em_models/wands/base_wand_stick")), blockEntity.getNextModelId(), false, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

            blockEntity.addModelToItem(model);

            selectedModelToAddId = "";
        }
    }

    public long getRequiredMana() {
        return blockEntity.getRequiredMana();
    }

    public WandBenchBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.wandBenchBlock.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 22 + l * 18, 157 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 22 + i * 18, 215));
        }
    }
}
