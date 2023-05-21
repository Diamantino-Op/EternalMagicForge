package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.api.mana.ItemStackManaStorage;
import com.diamantino.eternalmagic.client.model.Model;
import com.diamantino.eternalmagic.utils.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WandItem extends Item {
    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemStackManaStorage(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getOrCreateTag();
        CompoundTag spellsTag = tag.getCompound("spells");
        CompoundTag coreTag = tag.getCompound("core");

        long storedMana = tag.getLong("storedMana");
        long maxStoredMana = tag.getLong("maxStoredMana");
        float manaUsageReduction = tag.getFloat("manaUsageReduction");
        String coreElement = WandCoreItem.WandCoreElement.fromId(coreTag.getInt("element")).getName();
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
        CompoundTag tag = nbt.getCompound("upgrades");

        if (tag.contains(WandUpgradeItem.WandUpgradeType.cooldownReduction.regName)) {

        }
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
