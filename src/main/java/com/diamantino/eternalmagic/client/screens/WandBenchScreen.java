package com.diamantino.eternalmagic.client.screens;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.api.mana.IManaStorage;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import com.diamantino.eternalmagic.client.model.ModelLoader;
import com.diamantino.eternalmagic.client.screens.render.ManaInfoArea;
import com.diamantino.eternalmagic.networking.c2s.WandBenchButtonC2SPacket;
import com.diamantino.eternalmagic.registration.ModMessages;
import com.diamantino.eternalmagic.utils.MouseUtils;
import com.diamantino.eternalmagic.utils.TextUtils;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.*;

public class WandBenchScreen extends AbstractContainerScreen<WandBenchMenu> {
    private static final ResourceLocation texture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench.png");
    private static final ResourceLocation backgroundTexture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench_background.png");
    private static final ResourceLocation elementsTexture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench_elements.png");
    private IManaStorage manaStorage;
    private ManaInfoArea manaInfoArea;

    private ModelsScrollPanel availableModelsScrollPanel;
    private ModelsScrollPanel insertedModelsScrollPanel;

    public WandBenchScreen(WandBenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        imageWidth = 204;
        imageHeight = 239;

        inventoryLabelX += 14;
        inventoryLabelY = 145;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.manaStorage = menu.blockEntity.getManaStorage();
        this.availableModelsScrollPanel = new ModelsScrollPanel(7, 15, 73, 42, x, y, 0, elementsTexture);
        this.insertedModelsScrollPanel = new ModelsScrollPanel(7, 61, 73, 42, x, y, 1, elementsTexture);

        for (String modelName : ModelLoader.loadedModels.keySet().stream().toList())
        {
            availableModelsScrollPanel.addAndUpdateButtons(this, modelName, 0);
        }

        this.addRenderableWidget(this.availableModelsScrollPanel);
        this.addRenderableWidget(this.insertedModelsScrollPanel);
        assignManaInfoArea();
    }

    private void assignManaInfoArea() {
        manaInfoArea = new ManaInfoArea(0, 28, 26, 136, 80, 3, elementsTexture, manaStorage);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.font.draw(poseStack, Component.literal("Required: " + TextUtils.formatNumberWithPrefix(menu.getRequiredMana())), 26, 126, 0xff0000);

        renderManaAreaTooltips(poseStack, mouseX, mouseY, x, y);
    }

    private void renderManaAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
        if(isMouseAboveArea(mouseX, mouseY, x, y, 26, 136, 80, 3)) {
            renderTooltip(poseStack, manaInfoArea.getTooltips(), Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    private void renderItem(PoseStack stack, float partialTicks, int x, int y) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = menu.blockEntity.getRenderStack();

        if (menu.blockEntity.getLevel() != null && !itemStack.isEmpty()) {
            stack.pushPose();

            stack.translate(x + 161, y + 62, -1);
            stack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
            stack.scale(16.0F, 16.0F, 16.0F);

            float currentRot = (Objects.requireNonNull(menu.blockEntity.getLevel()).getGameTime() + partialTicks) * 0.5f;
            stack.mulPose(Axis.YP.rotationDegrees(currentRot));

            PoseStack itemPoseStack = new PoseStack();
            itemPoseStack.pushPose();

            itemPoseStack.mulPoseMatrix(stack.last().pose());

            itemPoseStack.scale(5, 5, 5);

            MultiBufferSource.BufferSource src = Minecraft.getInstance().renderBuffers().bufferSource();

            RenderSystem.applyModelViewMatrix();

            itemRenderer.render(itemStack, ItemDisplayContext.GROUND, false, itemPoseStack, src, 15728880, OverlayTexture.NO_OVERLAY, itemRenderer.getModel(itemStack, null, null, 0));

            src.endBatch();
            RenderSystem.enableDepthTest();

            Lighting.setupFor3DItems();

            itemPoseStack.popPose();
            stack.popPose();

            RenderSystem.applyModelViewMatrix();
        }
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        poseStack.pushPose();

        RenderSystem.setShaderTexture(0, backgroundTexture);

        poseStack.translate(0, 0, -100);

        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        poseStack.translate(0, 0, -50);

        renderItem(poseStack, partialTick, x, y);

        poseStack.popPose();

        RenderSystem.setShaderTexture(0, texture);

        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        manaInfoArea.draw(poseStack, x, y);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtils.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    public enum ButtonType {
        model,
        normal,
        up_arrow,
        down_arrow,
        left_arrow,
        right_arrow,
        plus,
        minus
    }

    public static class BenchButton extends ExtendedButton {
        private final int baseY;
        public final String btnText;
        public final int modelId;
        private final ButtonType buttonType;
        private final ResourceLocation elementsTexture;

        public BenchButton(WandBenchScreen screen, String btnText, int modelId, int x, int y, int width, int btnId, ButtonType buttonType, ResourceLocation elementsTexture) {
            super(x, y, width, 14, Component.literal(btnText), button -> onButtonClicked(screen, btnId, modelId, btnText));

            this.baseY = y;
            this.btnText = btnText;
            this.modelId = modelId;
            this.buttonType = buttonType;
            this.elementsTexture = elementsTexture;
        }

        public static void onButtonClicked(WandBenchScreen localScreen, int btnId, int modelId, String btnText) {
            ModMessages.sendToServer(new WandBenchButtonC2SPacket(localScreen.menu.blockEntity.getBlockPos(), btnId, modelId, btnText));
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            Minecraft mc = Minecraft.getInstance();
            int k = !this.active ? 0 : (this.isHovered() ? 2 : (this.isFocused() ? 1 : 0));

            RenderSystem.setShaderTexture(0, elementsTexture);

            blit(poseStack, this.getX(), this.getY(), k * 58, 14, this.getWidth(), this.getHeight());

            final FormattedText buttonText = mc.font.ellipsize(this.getMessage(), this.width - 6); // Remove 6 pixels so that the text is always contained within the button's borders
            drawCenteredString(poseStack, mc.font, Language.getInstance().getVisualOrder(buttonText), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor());
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return false;
        }

        public void scrollButton(int currentScrollAmount)
        {
            this.setY(this.baseY - currentScrollAmount);
        }
    }

    public static class ModelsScrollPanel extends ScrollPanel {
        private final List<BenchButton> buttons = new ArrayList<>();
        private boolean scrolling;
        public int totalButtonHeight;
        private final int buttonsId;
        private final ResourceLocation elementsTexture;

        public ModelsScrollPanel(int left, int top, int width, int height, int x, int y, int buttonsId, ResourceLocation elementsTexture)
        {
            super(Minecraft.getInstance(), width, height, y + top, x + left);

            this.buttonsId = buttonsId;
            this.elementsTexture = elementsTexture;
        }

        public void addAndUpdateButtons(WandBenchScreen screen, String modelName, int modelId) {
            BenchButton modelButton = new BenchButton(screen, modelName, modelId, left, top + totalButtonHeight, this.width - 15, buttonsId, ButtonType.model, elementsTexture);
            this.buttons.add(modelButton);
            totalButtonHeight += modelButton.getHeight();
        }

        public void clearContent() {
            totalButtonHeight = 0;

            this.buttons.clear();
        }

        public void searchContent(String search) {
            List<BenchButton> tempButtons = new ArrayList<>();
            int tempTotalButtonsHeight = 0;

            for (BenchButton btn : this.buttons) {
                if (btn.btnText.contains(search)) {
                    tempTotalButtonsHeight += btn.getHeight();
                    tempButtons.add(btn);
                }
            }

            clearContent();

            this.buttons.addAll(tempButtons);
            this.totalButtonHeight = tempTotalButtonsHeight;
        }

        @Override
        public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
            Tesselator tess = Tesselator.getInstance();

            double scale = Minecraft.getInstance().getWindow().getGuiScale();
            RenderSystem.enableScissor((int)(left  * scale), (int)(Minecraft.getInstance().getWindow().getHeight() - (bottom * scale)), (int)(width * scale), (int)(height * scale));

            int baseY = this.top + border - (int)this.scrollDistance;
            this.drawPanel(stack, right, baseY, tess, mouseX, mouseY);

            RenderSystem.disableDepthTest();

            int extraHeight = (this.getContentHeight() + border) - height;

            RenderSystem.setShaderTexture(0, elementsTexture);

            if (extraHeight > 0) {
                int barTop = (int)this.scrollDistance * (height - 13) / extraHeight + this.top;
                if (barTop < this.top)
                {
                    barTop = this.top;
                }

                if (this.scrolling)
                    blit(stack, (this.left + this.width) - 13, barTop + 1, 174, 0, 12, 15);
                else
                    blit(stack, (this.left + this.width) - 13, this.top + 1, 186, 0, 12, 15);
            } else {
                blit(stack, (this.left + this.width) - 13, this.top + 1, 186, 0, 12, 15);
            }

            RenderSystem.disableBlend();
            RenderSystem.disableScissor();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button)
        {
            this.scrolling = button == 0 && mouseX >= (left + width) - 13 && mouseX < left + width;

            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button)
        {
            if (super.mouseReleased(mouseX, mouseY, button))
                return true;
            boolean ret = this.scrolling;
            this.scrolling = false;
            return ret;
        }

        @Override
        public List<? extends GuiEventListener> children()
        {
            return this.buttons;
        }

        @Override
        protected int getContentHeight()
        {
            return this.totalButtonHeight;
        }

        @Override
        protected void drawPanel(PoseStack stack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY)
        {
            for (BenchButton button : this.buttons)
            {
                button.scrollButton((int) this.scrollDistance);
                button.render(stack, mouseX, mouseY, 0);
            }
        }

        @Override
        public @NotNull NarrationPriority narrationPriority() {
            return NarrationPriority.FOCUSED;
        }

        @Override
        public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

        }
    }
}
