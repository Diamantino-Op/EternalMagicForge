package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.client.model.Model;
import com.diamantino.eternalmagic.utils.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WandItem extends ManaItemBase {
    public static int baseSlots = 4;

    public WandItem(Properties properties) {
        super(properties, 10000, 1000000);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getOrCreateTag();
        CompoundTag spellsTag = tag.getCompound("spells");
        CompoundTag coreTag = tag.getCompound("core");

        long storedMana = getManaLevel(pStack);
        long maxStoredMana = getCapacity(pStack);
        float manaUsageReduction = tag.getFloat("manaUsageReduction");
        String coreElement = CoreItem.WandCoreElement.fromId(coreTag.getInt("element")).getName();
        int coreLevel = coreTag.getInt("level");
        int usedSpellSlots = spellsTag.getInt("usedSlots");
        int totalSpellSlots = spellsTag.getInt("totalSlots");
        float cooldownReduction = tag.getFloat("cooldownReduction");
        float castTimeReduction = tag.getFloat("castTimeReduction");

        pTooltipComponents.add(Component.translatable("tooltip.wand_item." + ModReferences.modId + ".stored_mana", TextUtils.formatNumberWithPrefix(storedMana), TextUtils.formatNumberWithPrefix(maxStoredMana)).withStyle(ChatFormatting.AQUA));
        pTooltipComponents.add(Component.translatable("tooltip.wand_item." + ModReferences.modId + ".core_element", coreElement).withStyle(ChatFormatting.DARK_BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.wand_item." + ModReferences.modId + ".core_level", coreLevel < 10 ? String.valueOf(coreLevel) : "MAX").withStyle(ChatFormatting.DARK_BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.wand_item." + ModReferences.modId + ".spell_slots", usedSpellSlots, totalSpellSlots).withStyle(ChatFormatting.GREEN));
        pTooltipComponents.add(Component.translatable("tooltip.wand_item." + ModReferences.modId + ".cooldown_reduction", cooldownReduction + "%").withStyle(ChatFormatting.GOLD));
        pTooltipComponents.add(Component.translatable("tooltip.wand_item." + ModReferences.modId + ".cast_time_reduction", castTimeReduction + "%").withStyle(ChatFormatting.GOLD));
        pTooltipComponents.add(Component.translatable("tooltip.wand_item." + ModReferences.modId + ".mana_usage_reduction", manaUsageReduction + "%").withStyle(ChatFormatting.GOLD));
    }

    public static float getTotalCooldownReduction(CompoundTag nbt) {
        List<WandUpgradeItem.WandUpgrade> upgrades = getUpgrades(nbt);

        float totalCooldownReduction = 0;

        for (WandUpgradeItem.WandUpgrade upgrade : upgrades) {
            if (upgrade.upgradeType == WandUpgradeItem.WandUpgradeType.cooldownReduction) {
                totalCooldownReduction += upgrade.getValue();
            }
        }

        return totalCooldownReduction;
    }

    public static float getTotalCastTimeReduction(CompoundTag nbt) {
        List<WandUpgradeItem.WandUpgrade> upgrades = getUpgrades(nbt);

        float totalCastTimeReduction = 0;

        for (WandUpgradeItem.WandUpgrade upgrade : upgrades) {
            if (upgrade.upgradeType == WandUpgradeItem.WandUpgradeType.castTimeReduction) {
                totalCastTimeReduction += upgrade.getValue();
            }
        }

        return totalCastTimeReduction;
    }

    public static float getTotalManaUsageReduction(CompoundTag nbt) {
        List<WandUpgradeItem.WandUpgrade> upgrades = getUpgrades(nbt);

        float totalManaUsageReduction = 0;

        for (WandUpgradeItem.WandUpgrade upgrade : upgrades) {
            if (upgrade.upgradeType == WandUpgradeItem.WandUpgradeType.manaUsageReduction) {
                totalManaUsageReduction += upgrade.getValue();
            }
        }

        return totalManaUsageReduction;
    }

    public static long getTotalManaCapacity(CompoundTag nbt) {
        List<WandUpgradeItem.WandUpgrade> upgrades = getUpgrades(nbt);
        CompoundTag coreTag = nbt.getCompound("core");
        int coreLevel = coreTag.getInt("level");

        long totalManaCapacity = baseStoredMana;

        for (WandUpgradeItem.WandUpgrade upgrade : upgrades) {
            if (upgrade.upgradeType == WandUpgradeItem.WandUpgradeType.manaCapacity) {
                totalManaCapacity += upgrade.getValue();
            }
        }

        return totalManaCapacity * Math.max(1, coreLevel);
    }

    public static int getTotalSlots(CompoundTag nbt) {
        List<WandUpgradeItem.WandUpgrade> upgrades = getUpgrades(nbt);

        int totalSlots = baseSlots;

        for (WandUpgradeItem.WandUpgrade upgrade : upgrades) {
            if (upgrade.upgradeType == WandUpgradeItem.WandUpgradeType.slot) {
                totalSlots += upgrade.getValue();
            }
        }

        return totalSlots;
    }

    public static void setCore(ItemStack stack, CompoundTag coreNbt) {
        CompoundTag nbt = stack.getOrCreateTag();
        CompoundTag coreTag = nbt.getCompound("core");

        CoreItem.WandCoreElement element = ((CoreItem) stack.getItem()).getElement();
        int level = CoreItem.getLevel(coreNbt);

        coreTag.putInt("element", element.getId());
        coreTag.putInt("level", level);

        nbt.put("core", coreTag);
    }

    public static boolean canAddUpgrade(CompoundTag nbt, WandUpgradeItem.WandUpgradeType upgradeType) {
        List<WandUpgradeItem.WandUpgrade> upgrades = getUpgrades(nbt);

        int upgradeAmount = 0;

        for (WandUpgradeItem.WandUpgrade upgrade : upgrades) {
            if (upgrade.upgradeType == upgradeType)
                upgradeAmount++;
        }

        return upgradeAmount < 16;
    }

    public static void addUpgrade(ItemStack stack, WandUpgradeItem.WandUpgrade upgrade) {
        CompoundTag nbt = stack.getOrCreateTag();
        List<WandUpgradeItem.WandUpgrade> upgrades = getUpgrades(nbt);
        List<Tag> upgradesTags = new ArrayList<>();

        upgrades.add(upgrade);

        for (WandUpgradeItem.WandUpgrade upg : upgrades) {
            CompoundTag tag = new CompoundTag();
            upg.toNBT(tag);
            upgradesTags.add(tag);
        }

        ListTag tag = new ListTag(upgradesTags, (byte) 10);

        nbt.put("upgrades", tag);

        nbt.putFloat("cooldownReduction", getTotalCooldownReduction(nbt));
        nbt.putFloat("castTimeReduction", getTotalCastTimeReduction(nbt));
        nbt.putFloat("manaUsageReduction", getTotalManaUsageReduction(nbt));
        setCapacity(stack, getTotalManaCapacity(nbt));
        nbt.putInt("totalSlots", getTotalSlots(nbt));
    }

    public static List<WandUpgradeItem.WandUpgrade> getUpgrades(CompoundTag tag) {
        ListTag upgradeTag = tag.getList("upgrades", 10);

        List<WandUpgradeItem.WandUpgrade> upgrades = new ArrayList<>();

        for (Tag nbt : upgradeTag) {
            upgrades.add(WandUpgradeItem.WandUpgrade.fromNBT((CompoundTag) nbt));
        }

        return upgrades;
    }

    public static void savePartsToNbt(CompoundTag nbt, List<Model> wandParts) {
        CompoundTag tag = new CompoundTag();

        int i = 0;

        for (Model model : wandParts) {
            tag.put(String.valueOf(i), model.toNbt());

            i++;
        }

        tag.putInt("modelsAmount", i);

        nbt.put("models", tag);
    }

    public static int getFirstFreeId(@Nullable CompoundTag nbt) {
        int nextId = 1;

        if (nbt != null && nbt.contains("models")) {
            CompoundTag tag = nbt.getCompound("models");

            int i = tag.getInt("modelsAmount");

            for (int j = 0; j < i + 1; j++) {
                if (!tag.contains(String.valueOf(j))) {
                    nextId = j;
                    break;
                }
            }
        }

        return nextId;
    }

    public static void addPart(CompoundTag nbt, Model model) {
        if (nbt.contains("models")) {
            CompoundTag tag = nbt.getCompound("models");

            int i = tag.getInt("modelsAmount");

            tag.put(String.valueOf(i), model.toNbt());

            tag.putInt("modelsAmount", i + 1);

            nbt.put("models", tag);
        } else {
            CompoundTag tag = new CompoundTag();

            tag.put("0", new Model(new ResourceLocation(ModReferences.modId, "em_models/wands/base_wand_stick"), 0, false, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)).toNbt());
            tag.put("1", model.toNbt());

            tag.putInt("modelsAmount", 2);

            nbt.put("models", tag);
        }
    }

    public static void removePart(CompoundTag nbt, int id) {
        CompoundTag tag = nbt.getCompound("models");

        tag.putInt("modelsAmount", tag.getInt("modelsAmount") - 1);

        tag.remove(String.valueOf(id));

        nbt.put("models", tag);
    }

    public static void editPart(CompoundTag nbt, int id, float transX, float transY, float transZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, boolean selected, boolean isEditing) {
        CompoundTag tag = nbt.getCompound("models");

        CompoundTag modelTag = tag.getCompound(String.valueOf(id));

        Model model = nbt.contains("models") ? Model.fromNbt(modelTag) : new Model(new ResourceLocation(ModReferences.modId, "em_models/wands/base_wand_stick"), id, selected, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

        modelTag.putString("modelId", model.modelId().toString());
        modelTag.putInt("id", model.id());
        modelTag.putBoolean("selected", isEditing ? model.selected() : selected);
        modelTag.putFloat("translationX", transX + model.translation().x());
        modelTag.putFloat("translationY", transY + model.translation().y());
        modelTag.putFloat("translationZ", transZ + model.translation().z());
        modelTag.putFloat("rotationX", rotX + model.rotation().x());
        modelTag.putFloat("rotationY", rotY + model.rotation().y());
        modelTag.putFloat("rotationZ", rotZ + model.rotation().z());
        modelTag.putFloat("scaleX", scaleX + model.scale().x());
        modelTag.putFloat("scaleY", scaleY + model.scale().y());
        modelTag.putFloat("scaleZ", scaleZ + model.scale().z());

        tag.put(String.valueOf(id), modelTag);

        if (!nbt.contains("models"))
            tag.putInt("modelsAmount", 1);

        nbt.put("models", tag);
    }

    public static Map<Integer, Model> loadPartsFromNbt(@Nullable CompoundTag nbt) {
        Map<Integer, Model> wandParts = new LinkedHashMap<>();

        if (nbt != null && nbt.contains("models")) {
            CompoundTag modelsTag = nbt.getCompound("models");

            int amount = modelsTag.getInt("modelsAmount");

            for(int i = 0; i < amount; i++) {
                CompoundTag tag = modelsTag.getCompound(String.valueOf(i));

                wandParts.put(i, Model.fromNbt(tag));
            }
        } else {
            wandParts.put(0, new Model(new ResourceLocation(ModReferences.modId, "em_models/wands/base_wand_stick"), 0, false, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
        }

        return wandParts;
    }
}
