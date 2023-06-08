package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.ModConstants;
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
    private final WandCoreElement element;

    public CoreItem(Properties pProperties, WandCoreElement element) {
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

    public WandCoreElement getElement() {
        return element;
    }

    public enum WandCoreElement {
        none(0, Component.translatable("element." + ModConstants.modId + ".none")),
        earth(1, Component.translatable("element." + ModConstants.modId + ".earth")),
        air(2, Component.translatable("element." + ModConstants.modId + ".air")),
        water(3, Component.translatable("element." + ModConstants.modId + ".water")),
        fire(4, Component.translatable("element." + ModConstants.modId + ".fire")),
        electricity(5, Component.translatable("element." + ModConstants.modId + ".electricity")),
        darkness(6, Component.translatable("element." + ModConstants.modId + ".darkness")),
        light(7, Component.translatable("element." + ModConstants.modId + ".light")),
        ice(8, Component.translatable("element." + ModConstants.modId + ".ice")),
        magma(9, Component.translatable("element." + ModConstants.modId + ".magma")),
        infinity(10, Component.translatable("element." + ModConstants.modId + ".infinity"));

        private final int id;
        private final Component name;

        WandCoreElement(int id, Component name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name.getString();
        }

        public int getId() {
            return id;
        }

        public static WandCoreElement fromId(int elementId) {
            for (WandCoreElement elem : WandCoreElement.values()) {
                if (elem.id == elementId)
                    return elem;
            }

            return earth;
        }
    }
}
