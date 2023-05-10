package com.diamantino.eternalmagic.client.screens.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;

public abstract class InfoArea extends GuiComponent {
    protected final Rect2i sourceArea;
    protected final Rect2i destArea;

    protected InfoArea(Rect2i sourceArea, Rect2i destArea) {
        this.sourceArea = sourceArea;
        this.destArea = destArea;
    }

    public abstract void draw(PoseStack transform, int x, int y);
}