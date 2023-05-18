package com.diamantino.eternalmagic.client.model;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public record Model(ResourceLocation modelId, int id, boolean selected, Vector3f translation, Vector3f rotation, Vector3f scale) {
    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();

        tag.putString("modelId", modelId.toString());
        tag.putInt("id", id);
        tag.putBoolean("selected", selected);
        tag.putFloat("translationX", translation.x());
        tag.putFloat("translationY", translation.y());
        tag.putFloat("translationZ", translation.z());
        tag.putFloat("rotationX", rotation.x());
        tag.putFloat("rotationY", rotation.y());
        tag.putFloat("rotationZ", rotation.z());
        tag.putFloat("scaleX", scale.x());
        tag.putFloat("scaleY", scale.y());
        tag.putFloat("scaleZ", scale.z());

        return tag;
    }

    public static Model fromNbt(CompoundTag tag) {
        return new Model(
                new ResourceLocation(tag.getString("modelId")),
                tag.getInt("id"),
                tag.getBoolean("selected"),
                new Vector3f(tag.getFloat("translationX"), tag.getFloat("translation"), tag.getFloat("translationZ")),
                new Vector3f(tag.getFloat("rotationX"), tag.getFloat("rotation"), tag.getFloat("rotationZ")),
                new Vector3f(tag.getFloat("scaleX"), tag.getFloat("scaleY"), tag.getFloat("scaleZ"))
        );
    }
}