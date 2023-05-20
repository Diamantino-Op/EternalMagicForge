package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.client.model.Model;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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

    public static void editPart(CompoundTag nbt, int id, int transX, int transY, int transZ, int rotX, int rotY, int rotZ, int scaleX, int scaleY, int scaleZ, boolean selected, boolean isEditing) {
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
            wandParts.put(0, new Model(new ResourceLocation(ModReferences.modId, "em_models/wands/base_wand_stick"), 0, true, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
        }

        return wandParts;
    }
}
