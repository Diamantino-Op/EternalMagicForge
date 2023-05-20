package com.diamantino.eternalmagic.client.screens.components;

import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import com.diamantino.eternalmagic.items.WandItem;
import com.diamantino.eternalmagic.registration.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class EMSlot extends SlotItemHandler {
    private final WandBenchMenu menu;

    public EMSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, WandBenchMenu menu) {
        super(itemHandler, index, xPosition, yPosition);

        this.menu = menu;
    }

    @Override
    public void onTake(@NotNull Player pPlayer, @NotNull ItemStack pStack) {
        if (pStack.is(ModItems.wandItem.get()))
            WandItem.editPart(pStack.getOrCreateTag(), menu.getSelectedModelId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false);

        super.onTake(pPlayer, pStack);
    }
}
