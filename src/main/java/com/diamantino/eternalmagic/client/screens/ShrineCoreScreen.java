package com.diamantino.eternalmagic.client.screens;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.api.capabilities.mana.IManaStorage;
import com.diamantino.eternalmagic.client.menu.ShrineCoreMenu;
import com.diamantino.eternalmagic.client.screens.render.ManaInfoArea;
import com.diamantino.eternalmagic.utils.MouseUtils;
import com.diamantino.eternalmagic.utils.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ShrineCoreScreen extends AbstractContainerScreen<ShrineCoreMenu> {
    private static final ResourceLocation texture = new ResourceLocation(ModConstants.modId,"textures/gui/shrine_core.png");
    private IManaStorage manaStorage;
    private ManaInfoArea manaInfoArea;

    public ShrineCoreScreen(ShrineCoreMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 165;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.manaStorage = menu.blockEntity.getManaStorage();

        assignManaInfoArea();
    }

    private void assignManaInfoArea() {
        manaInfoArea = new ManaInfoArea(176, 0, 50, 55, 80, 5, texture, manaStorage);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        Component text1 = Component.translatable("screen." + ModConstants.modId + ".shrine_core.block_multiplier", TextUtils.formatNumberWithPrefix(menu.blockEntity.generatingManaMultiplier * 100) + "%");
        Component text2 = Component.translatable("screen." + ModConstants.modId + ".shrine_core.core_multiplier", TextUtils.formatNumberWithPrefix((menu.blockEntity.coreLevel * menu.blockEntity.coreLevel) * 10) + "%");
        Component text3 = Component.translatable("screen." + ModConstants.modId + ".shrine_core.level", menu.blockEntity.coreLevel < 10 ? String.valueOf(menu.blockEntity.coreLevel) : "MAX");
        Component text4 = Component.translatable("screen." + ModConstants.modId + ".shrine_core.mana_per_second", TextUtils.formatNumberWithPrefix(menu.blockEntity.generatingMana));

        float width3 = this.font.width(text3.getVisualOrderText()) * 0.5f;
        float width4 = this.font.width(text4.getVisualOrderText()) * 0.5f;

        graphics.pose().pushPose();

        graphics.pose().scale(0.5f, 0.5f, 0.5f);

        graphics.drawString(this.font, text1.getVisualOrderText(), 7 / 0.5f, 25 / 0.5f, 0xff0000, false);
        graphics.drawString(this.font, text2.getVisualOrderText(), 7 / 0.5f, 40 / 0.5f, 0xff0000, false);
        graphics.drawString(this.font, text4.getVisualOrderText(), (176 - width4 - 8) / 0.5f, 25 / 0.5f, 0xff0000, false);
        graphics.drawString(this.font, text3.getVisualOrderText(), (176 - width3 - 8) / 0.5f, 40 / 0.5f, 0xff0000, false);

        graphics.pose().popPose();

        renderManaAreaTooltips(graphics, mouseX, mouseY, x, y);
    }

    private void renderManaAreaTooltips(GuiGraphics graphics, int mouseX, int mouseY, int x, int y) {
        if(isMouseAboveArea(mouseX, mouseY, x, y, 50, 55, 80, 5)) {
            graphics.renderTooltip(this.font, manaInfoArea.getTooltips(), Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    private void renderProgressArrow(GuiGraphics graphics, int x, int y) {
        if(menu.isCrafting()) {
            graphics.blit(texture, x + 65, y + 32, 176, 5, 15, menu.getScaledProgress());
            graphics.blit(texture, x + 100, y + 32, 191, 5, 15, menu.getScaledProgress());
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(graphics, x, y);

        manaInfoArea.draw(graphics, x, y);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtils.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
