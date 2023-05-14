package com.diamantino.eternalmagic.items;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.client.model.Model;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

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

            tag.put("0", new Model(new ResourceLocation(ModReferences.modId, "em_models/wands/base_wand_stick"), 0, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)).toNbt());
            tag.put("1", model.toNbt());

            tag.putInt("modelsAmount", 2);

            nbt.put("models", tag);
        }
    }

    public static List<Model> loadPartsFromNbt(@Nullable CompoundTag nbt) {
        List<Model> wandParts = new ArrayList<>();

        if (nbt != null && nbt.contains("models")) {
            CompoundTag modelsTag = nbt.getCompound("models");

            int amount = modelsTag.getInt("modelsAmount");

            for(int i = 0; i < amount; i++) {
                CompoundTag tag = modelsTag.getCompound(String.valueOf(i));

                wandParts.add(Model.fromNbt(tag));
            }
        } else {
            wandParts.add(new Model(new ResourceLocation(ModReferences.modId, "em_models/wands/base_wand_stick"), 0, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
        }

        return wandParts;
    }
}
