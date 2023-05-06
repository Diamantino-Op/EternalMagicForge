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

    public static List<Model> loadPartsFromNbt(@Nullable CompoundTag nbt) {
        List<Model> wandParts = new ArrayList<>();

        if (nbt != null && nbt.contains("models")) {
            int amount = nbt.getInt("modelsAmount");

            for(int i = 0; i < amount; i++) {
                CompoundTag tag = nbt.getCompound(String.valueOf(i));

                wandParts.add(Model.fromNbt(tag));
            }
        } else {
            wandParts.add(new Model(new ResourceLocation(ModReferences.modId, "em_models/wands/base_wand_stick"), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
        }

        return wandParts;
    }
}
