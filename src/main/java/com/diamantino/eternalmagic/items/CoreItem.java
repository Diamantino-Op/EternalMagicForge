package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.api.capabilities.player.Element;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoreItem extends Item {
    private final Element element;

    public CoreItem(Properties pProperties, Element element) {
        super(pProperties);

        this.element = element;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getOrCreateTag();

        String level = getLevelStr(tag);

        pTooltipComponents.add(Component.translatable("tooltip.wand_core_item." + ModConstants.modId + ".element", element.getName()).withStyle(ChatFormatting.DARK_BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.wand_core_item." + ModConstants.modId + ".level", level).withStyle(ChatFormatting.DARK_BLUE));
    }

    public static boolean incrementLevel(CompoundTag nbt) {
        int level = nbt.getInt("level");

        if (level < 10) {
            nbt.putInt("level", level + 1);

            return true;
        }

        return false;
    }

    public static String getLevelStr(CompoundTag nbt) {
        int level = nbt.getInt("level");

        return level < 10 ? String.valueOf(level) : "MAX";
    }

    public static int getLevel(CompoundTag nbt) {
        return nbt.getInt("level");
    }

    public Element getElement() {
        return element;
    }
}
