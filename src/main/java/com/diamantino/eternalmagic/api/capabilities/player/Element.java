package com.diamantino.eternalmagic.api.capabilities.player;

import com.diamantino.eternalmagic.ModConstants;
import net.minecraft.network.chat.Component;

public enum Element {
    none(Component.translatable("element." + ModConstants.modId + ".none")),
    earth(Component.translatable("element." + ModConstants.modId + ".earth")),
    air(Component.translatable("element." + ModConstants.modId + ".air")),
    water(Component.translatable("element." + ModConstants.modId + ".water")),
    fire(Component.translatable("element." + ModConstants.modId + ".fire")),
    electricity(Component.translatable("element." + ModConstants.modId + ".electricity")),
    ice(Component.translatable("element." + ModConstants.modId + ".ice")),
    magma(Component.translatable("element." + ModConstants.modId + ".magma")),
    darkness(Component.translatable("element." + ModConstants.modId + ".darkness")),
    light(Component.translatable("element." + ModConstants.modId + ".light")),
    infinity(Component.translatable("element." + ModConstants.modId + ".infinity"));

    private final Component name;

    Element(Component name) {
        this.name = name;
    }

    public String getName() {
        return name.getString();
    }

    public static Element fromId(int elementId) {
        for (Element elem : Element.values()) {
            if (elem.ordinal() == elementId)
                return elem;
        }

        return none;
    }
}
