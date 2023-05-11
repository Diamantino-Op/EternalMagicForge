package com.diamantino.eternalmagic.client.screens.render;

import com.diamantino.eternalmagic.api.mana.IManaStorage;
import com.diamantino.eternalmagic.utils.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
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
        return List.of(Component.literal("Mana: " + TextUtils.formatNumberWithPrefix(manaStorage.getManaStored()) + "/" + TextUtils.formatNumberWithPrefix(manaStorage.getMaxManaStored())).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public void draw(PoseStack transform, int x, int y) {
        RenderSystem.setShaderTexture(0, barLocation);

        blit(transform, x + destArea.getX(), y + destArea.getY(), sourceArea.getX(), sourceArea.getY(), (int) ((double) manaStorage.getManaStored() * ((double) sourceArea.getWidth() / (double) manaStorage.getMaxManaStored())), sourceArea.getHeight());
    }
}
