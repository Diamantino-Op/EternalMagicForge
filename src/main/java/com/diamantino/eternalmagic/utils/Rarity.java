package com.diamantino.eternalmagic.utils;

import com.diamantino.eternalmagic.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum Rarity {
    common(0, 1.15f, Component.translatable("rarity." + ModConstants.modId + ".common"), ChatFormatting.GRAY),
    uncommon(1, 1.2f, Component.translatable("rarity." + ModConstants.modId + ".uncommon"), ChatFormatting.GREEN),
    rare(2, 1.5f, Component.translatable("rarity." + ModConstants.modId + ".rare"), ChatFormatting.BLUE),
    epic(3, 1.75f, Component.translatable("rarity." + ModConstants.modId + ".epic"), ChatFormatting.DARK_PURPLE),
    legendary(4, 2.0f, Component.translatable("rarity." + ModConstants.modId + ".legendary"), ChatFormatting.GOLD),
    mythic(5, 2.5f, Component.translatable("rarity." + ModConstants.modId + ".mythic"), ChatFormatting.LIGHT_PURPLE);

    public final int id;
    public final float multiplier;
    private final Component name;
    public final ChatFormatting color;

    Rarity(int id, float multiplier, Component name, ChatFormatting color) {
        this.id = id;
        this.multiplier = multiplier;
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name.getString();
    }

    public static Rarity fromId(int id) {
        for (Rarity rarity : Rarity.values()) {
            if (rarity.id == id)
                return rarity;
        }

        return common;
    }
}