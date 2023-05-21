package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.utils.Rarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class WandUpgradeItem extends Item {
    public WandUpgradeType upgradeType;

    public WandUpgradeItem(Properties pProperties, WandUpgradeType upgradeType) {
        super(pProperties);

        this.upgradeType = upgradeType;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        WandUpgrade upgrade = getUpgrade(pStack);

        pTooltipComponents.add(Component.translatable("tooltip.wand_upgrade." + ModReferences.modId + ".type", upgrade.upgradeType.getName()));
        pTooltipComponents.add(Component.translatable("tooltip.wand_upgrade." + ModReferences.modId + ".rarity", upgrade.rarity.getName()));
        pTooltipComponents.add(Component.translatable("tooltip.wand_upgrade." + ModReferences.modId + ".value", String.valueOf(upgrade.value)));
    }

    public static void rerollRarity(ItemStack stack) {
        Random random = new Random();
        int randomNumber = random.nextInt(100);

        Rarity rarity;

        if (randomNumber < 60) {
            rarity = Rarity.common;
        } else if (randomNumber < 80) {
            rarity = Rarity.uncommon;
        } else if (randomNumber < 90) {
            rarity = Rarity.rare;
        } else if (randomNumber < 95) {
            rarity = Rarity.epic;
        } else if (randomNumber < 99) {
            rarity = Rarity.legendary;
        } else {
            rarity = Rarity.mythic;
        }

        if (stack.getOrCreateTag().contains("typeId")) {
            stack.getOrCreateTag().putInt("rarityId", rarity.id);
        } else {
            WandUpgrade upgrade = getUpgrade(stack);
            upgrade.rarity = rarity;

            upgrade.toNBT(stack.getOrCreateTag());
        }
    }

    public static WandUpgrade getUpgrade(ItemStack stack) {
        WandUpgrade upgrade = WandUpgrade.fromNBT(stack.getOrCreateTag());
        upgrade.upgradeType = ((WandUpgradeItem) stack.getItem()).upgradeType;

        return upgrade;
    }

    public static class WandUpgrade {
        public WandUpgradeType upgradeType;
        public Rarity rarity;
        private final float value;

        public WandUpgrade(WandUpgradeType type, Rarity rarity, float value) {
            this.upgradeType = type;
            this.rarity = rarity;
            this.value = value;
        }

        public float getValue() {
            float finalVal = value * rarity.multiplier;

            if (upgradeType == WandUpgradeType.slot)
                return Math.round(finalVal);

            return finalVal;
        }

        public void toNBT(CompoundTag tag) {
            tag.putInt("typeId", upgradeType.id);
            tag.putInt("rarityId", rarity.id);
            tag.putFloat("value", value);
        }

        public static WandUpgrade fromNBT(CompoundTag tag) {
            WandUpgradeType upgradeType = WandUpgradeType.fromId(tag.getInt("typeId"));
            Rarity rarity = Rarity.fromId(tag.getInt("rarityId"));
            float value = Math.max(1, tag.getFloat("value"));

            return new WandUpgrade(upgradeType, rarity, value);
        }
    }

    public enum WandUpgradeType {
        none(0, "none", Component.translatable("upgrade.wand." + ModReferences.modId + ".none")),
        cooldownReduction(1, "cooldown_reduction", Component.translatable("upgrade.wand." + ModReferences.modId + ".cooldown_reduction")),
        castTimeReduction(2, "cast_time_reduction", Component.translatable("upgrade.wand." + ModReferences.modId + ".cast_time_reduction")),
        manaUsageReduction(3, "mana_usage_reduction", Component.translatable("upgrade.wand." + ModReferences.modId + ".mana_usage_reduction")),
        manaCapacity(4, "mana_capacity", Component.translatable("upgrade.wand." + ModReferences.modId + ".mana_capacity")),
        slot(5, "slot", Component.translatable("upgrade.wand." + ModReferences.modId + ".slot"));

        private final Component name;
        public final String regName;
        public final int id;

        WandUpgradeType(int id, String regName, Component name) {
            this.regName = regName;
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name.getString();
        }

        public static WandUpgradeType fromId(int id) {
            for (WandUpgradeType upgradeType : WandUpgradeType.values()) {
                if (upgradeType.id == id)
                    return upgradeType;
            }

            return none;
        }
    }
}
