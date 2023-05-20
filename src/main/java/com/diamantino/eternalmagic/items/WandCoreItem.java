package com.diamantino.eternalmagic.items;

import net.minecraft.world.item.Item;

import java.util.Objects;

public class WandCoreItem extends Item {
    private final WandCoreElement element;

    public WandCoreItem(Properties pProperties, WandCoreElement element) {
        super(pProperties);

        this.element = element;
    }

    public WandCoreElement getElement() {
        return element;
    }

    public enum WandCoreElement {
        earth("Earth"),
        air("Air"),
        water("Water"),
        fire("Fire"),
        electricity("Electricity"),
        darkness("Darkness"),
        light("Light"),
        ice("Ice"),
        magma("Magma"),
        infinity("Infinity");

        private final String name;

        WandCoreElement(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static WandCoreElement fromString(String element) {
            for (WandCoreElement elem : WandCoreElement.values()) {
                if (Objects.equals(elem.name, element))
                    return elem;
            }

            return earth;
        }
    }
}
