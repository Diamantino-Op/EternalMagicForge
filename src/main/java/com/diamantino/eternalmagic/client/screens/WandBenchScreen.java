package com.diamantino.eternalmagic.client.screens;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.api.mana.IManaStorage;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import com.diamantino.eternalmagic.client.model.ModelLoader;
import com.diamantino.eternalmagic.client.screens.render.ManaInfoArea;
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
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.*;

public class WandBenchScreen extends AbstractContainerScreen<WandBenchMenu> {
    private static final ResourceLocation texture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench.png");
    private static final ResourceLocation backgroundTexture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench_background.png");
    private static final ResourceLocation barTexture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench_elements.png");
    private IManaStorage manaStorage;
    private ManaInfoArea manaInfoArea;

    public String selectedButtonName;

    private ModelsScrollPanel availableModelsScrollPanel;
    private ModelsScrollPanel insertedModelsScrollPanel;

    public WandBenchScreen(WandBenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        imageWidth = 204;
        imageHeight = 239;

        inventoryLabelX += 14;
        inventoryLabelY = 145;

        selectedButtonName = "";
    }

    @Override
    protected void init() {
        super.init();

        this.manaStorage = menu.blockEntity.getManaStorage();
        this.availableModelsScrollPanel = new ModelsScrollPanel(this, ModelLoader.loadedModels.keySet().stream().toList(), 7, 15, 73, 42);
        this.insertedModelsScrollPanel = new ModelsScrollPanel(this, List.of(), 7, 61, 73, 42);

        this.children().add(this.availableModelsScrollPanel);
        this.children().add(this.insertedModelsScrollPanel);
        assignManaInfoArea();
    }

    private void assignManaInfoArea() {
        manaInfoArea = new ManaInfoArea(0, 28, 26, 136, 80, 3, barTexture, manaStorage);
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

        poseStack.translate(0, 0, -10);

        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        poseStack.popPose();

        renderItem(poseStack, partialTick, x, y);

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

    public static class ModelButton extends ExtendedButton {
        private final int baseY;
        public final String modelName;

        public ModelButton(WandBenchScreen screen, String modelName, int x, int y, int width) {
            super(x, y, width, 14, Component.literal(modelName), button -> onButtonClicked(screen, modelName));

            this.baseY = y;
            this.modelName = modelName;
        }

        public static void onButtonClicked(WandBenchScreen localScreen, String modelName) {
            localScreen.selectedButtonName = modelName;
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
        private final List<ModelButton> buttons = new ArrayList<>();
        public int totalButtonHeight;

        public ModelsScrollPanel(WandBenchScreen screen, List<String> modelNames, int left, int top, int width, int height)
        {
            super(Minecraft.getInstance(), width, height, top, left);

            Level world = Minecraft.getInstance().level;
            if (world != null)
            {
                for (String modelName : modelNames)
                {
                    addAndUpdateButtons(screen, modelName);
                }
            }
        }

        public void addAndUpdateButtons(WandBenchScreen screen, String modelName) {
            ModelButton modelButton = new ModelButton(screen, modelName, left, top + totalButtonHeight, this.width - 15);
            this.buttons.add(modelButton);
            totalButtonHeight += modelButton.getHeight();
        }

        public void clearContent() {
            totalButtonHeight = 0;

            this.buttons.clear();
        }

        public void searchContent(String search) {
            List<ModelButton> tempButtons = new ArrayList<>();
            int tempTotalButtonsHeight = 0;

            for (ModelButton btn : this.buttons) {
                if (btn.modelName.contains(search)) {
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

            if (extraHeight > 0) {
                int barTop = (int)this.scrollDistance * (height - 13) / extraHeight + this.top;
                if (barTop < this.top)
                {
                    barTop = this.top;
                }

                if (this.scrolling)
                    blit(stack, (this.top + this.width) - 12, barTop, 174, 0, 12, 15);
                else
                    blit(stack, (this.top + this.width) - 12, this.top, 186, 0, 12, 15);
            } else {
                blit(stack, (this.top + this.width) - 12, this.top, 186, 0, 12, 15);
            }

            RenderSystem.disableBlend();
            RenderSystem.disableScissor();
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
            for (ModelButton button : this.buttons)
            {
                button.scrollButton((int) this.scrollDistance);
                button.render(stack, mouseX, mouseY, 0);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button)
        {
            return super.mouseClicked(mouseX, mouseY, button);
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
