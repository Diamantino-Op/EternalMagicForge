package com.diamantino.eternalmagic.client.screens.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;

public abstract class InfoArea {
    protected final Rect2i sourceArea;
    protected final Rect2i destArea;

    protected InfoArea(Rect2i sourceArea, Rect2i destArea) {
        this.sourceArea = sourceArea;
        this.destArea = destArea;
    }

    public abstract void draw(GuiGraphics graphics, int x, int y);
}