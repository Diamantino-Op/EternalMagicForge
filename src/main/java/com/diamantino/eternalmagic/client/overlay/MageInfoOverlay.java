package com.diamantino.eternalmagic.client.overlay;

import com.diamantino.eternalmagic.ModConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class MageInfoOverlay {
    public static final ResourceLocation overlayTexture = new ResourceLocation(ModConstants.modId, "textures/overlay/mage_info.png");

    public static final IGuiOverlay mageInfoOverlay = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
       int x = screenWidth / 2;
       int y = screenHeight / 2;

       //guiGraphics.blit(overlayTexture, 5, y - 25, 0, 0, 13, 51);
    });
}
