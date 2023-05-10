package com.diamantino.eternalmagic.client.screens.render;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.api.mana.IManaStorage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ManaInfoArea extends InfoArea {
    private final IManaStorage manaStorage;
    private final ResourceLocation barLocation;

    public ManaInfoArea(int sourceXMin, int sourceYMin, int destXMin, int destYMin, int width, int height, ResourceLocation barLocation, IManaStorage manaStorage) {
        super(new Rect2i(sourceXMin, sourceYMin, width, height), new Rect2i(destXMin, destYMin, width, height));

        this.manaStorage = manaStorage;
        this.barLocation = barLocation;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal("Mana: " + manaStorage.getManaStored() + "/" + manaStorage.getMaxManaStored()));
    }

    @Override
    public void draw(PoseStack transform, int x, int y) {
        RenderSystem.setShaderTexture(0, barLocation);

        blit(transform, x + destArea.getX(), y + destArea.getY(), sourceArea.getX(), sourceArea.getY(), manaStorage.getManaStored() * (sourceArea.getWidth() / manaStorage.getMaxManaStored()), sourceArea.getHeight());
    }
}
