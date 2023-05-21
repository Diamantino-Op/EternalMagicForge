package com.diamantino.eternalmagic.client.screens;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.api.mana.IManaStorage;
import com.diamantino.eternalmagic.client.menu.WandBenchMenu;
import com.diamantino.eternalmagic.client.model.Model;
import com.diamantino.eternalmagic.client.model.ModelLoader;
import com.diamantino.eternalmagic.client.screens.render.ManaInfoArea;
import com.diamantino.eternalmagic.items.WandItem;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.EditBox;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

public class WandBenchScreen extends AbstractContainerScreen<WandBenchMenu> {
    private static final ResourceLocation texture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench.png");
    private static final ResourceLocation elementsTexture = new ResourceLocation(ModReferences.modId,"textures/gui/wand_bench_elements.png");
    private IManaStorage manaStorage;
    private ManaInfoArea manaInfoArea;

    private ModelsScrollPanel availableModelsScrollPanel;
    private ModelsScrollPanel insertedModelsScrollPanel;
    private BenchEditBox searchBox;

    private Map<Integer, Model> loadedModels = new LinkedHashMap<>();

    private List<BenchButton> moveButtons = new ArrayList<>();

    private int scale;
    private int tranX;
    private int tranY;

    public WandBenchScreen(WandBenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 204;
        this.imageHeight = 239;

        this.inventoryLabelX += 14;
        this.inventoryLabelY = 145;

        this.tranX = 0;
        this.tranY = 0;
        this.scale = 1;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.manaStorage = menu.blockEntity.getManaStorage();
        this.availableModelsScrollPanel = new ModelsScrollPanel(7, 15, 73, 42, x, y, 0, elementsTexture);
        this.insertedModelsScrollPanel = new ModelsScrollPanel(7, 61, 73, 42, x, y, 1, elementsTexture);

        this.addRenderableWidget(this.availableModelsScrollPanel);
        this.addRenderableWidget(this.insertedModelsScrollPanel);

        // Translation buttons
        this.moveButtons.add(new BenchButton(this, "UpX", 0, x + 85, y + 14, 9, 7, 2, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "UpY", 0, x + 98, y + 14, 9, 7, 3, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "UpZ", 0, x + 111, y + 14, 9, 7, 4, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownX", 0, x + 85, y + 28, 9, 7, 5, ButtonType.down_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownY", 0, x + 98, y + 28, 9, 7, 6, ButtonType.down_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownZ", 0, x + 111, y + 28, 9, 7, 7, ButtonType.down_arrow, false, null, elementsTexture));

        // Rotation buttons
        this.moveButtons.add(new BenchButton(this, "UpX", 0, x + 85, y + 40, 9, 7, 8, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "UpY", 0, x + 98, y + 40, 9, 7, 9, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "UpZ", 0, x + 111, y + 40, 9, 7, 10, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownX", 0, x + 85, y + 54, 9, 7, 11, ButtonType.down_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownY", 0, x + 98, y + 54, 9, 7, 12, ButtonType.down_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownZ", 0, x + 111, y + 54, 9, 7, 13, ButtonType.down_arrow, false, null, elementsTexture));

        // Scaling buttons
        this.moveButtons.add(new BenchButton(this, "UpX", 0, x + 85, y + 66, 9, 7, 14, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "UpY", 0, x + 98, y + 66, 9, 7, 15, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "UpZ", 0, x + 111, y + 66, 9, 7, 16, ButtonType.up_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownX", 0, x + 85, y + 80, 9, 7, 17, ButtonType.down_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownY", 0, x + 98, y + 80, 9, 7, 18, ButtonType.down_arrow, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "DownZ", 0, x + 111, y + 80, 9, 7, 19, ButtonType.down_arrow, false, null, elementsTexture));

        // Add / Remove
        this.moveButtons.add(new BenchButton(this, "Add", 0, x + 82, y + 89, 41, 14, 20, ButtonType.normal, false, null, elementsTexture));
        this.moveButtons.add(new BenchButton(this, "Delete", 0, x + 82, y + 104, 41, 14, 21, ButtonType.normal, false, null, elementsTexture));

        // Preview buttons
        this.addRenderableWidget(new BenchButton(this, "Plus", 0, x + 132, y + 104, 15, 15, 22, ButtonType.plus, false, null, elementsTexture));
        this.addRenderableWidget(new BenchButton(this, "Minus", 0, x + 148, y + 104, 15, 15, 23, ButtonType.minus, false, null, elementsTexture));
        this.addRenderableWidget(new BenchButton(this, "LeftArrow", 0, x + 165, y + 104, 7, 15, 24, ButtonType.left_arrow, false, null, elementsTexture));
        this.addRenderableWidget(new BenchButton(this, "UpArrow", 0, x + 173, y + 104, 9, 7, 25, ButtonType.up_arrow, false, null, elementsTexture));
        this.addRenderableWidget(new BenchButton(this, "DownArrow", 0, x + 173, y + 112, 9, 7, 26, ButtonType.down_arrow, false, null, elementsTexture));
        this.addRenderableWidget(new BenchButton(this, "RightArrow", 0, x + 183, y + 104, 7, 15, 27, ButtonType.right_arrow, false, null, elementsTexture));

        for (String modelName : ModelLoader.loadedModels.keySet().stream().toList())
        {
            availableModelsScrollPanel.addAndUpdateButtons(this, modelName.replace(".json", ""), 0, false);
        }

        for (BenchButton btn : moveButtons) {
            this.addRenderableWidget(btn);
        }

        updateAddedModels(menu.blockEntity.getRenderStack());

        selectFirstModel();

        this.searchBox = new BenchEditBox(this.font, x + 7, y + 104, 73, 14, Component.literal("Search Models"), elementsTexture);
        this.searchBox.setTextColor(-1);
        this.searchBox.setTextColorUneditable(-1);
        this.searchBox.setMaxLength(30);
        this.searchBox.setValue("");

        this.addRenderableWidget(searchBox);

        assignManaInfoArea();
    }

    public void selectFirstModel() {
        if (insertedModelsScrollPanel.sortedButtons.size() > 0) {
            BenchButton btn = insertedModelsScrollPanel.sortedButtons.get(0);

            btn.mouseClicked(btn.getX() + 1, btn.getY() + 1, 0);
            btn.setFocused(true);
            btn.selected = true;
        }
    }

    private void onSearchBoxTextChanged(String text) {
        insertedModelsScrollPanel.searchContent(text);
        availableModelsScrollPanel.searchContent(text);
    }

    public void updateAddedModels(ItemStack stack) {
        insertedModelsScrollPanel.clearContent();
        loadedModels.clear();

        if (stack != ItemStack.EMPTY) {
            loadedModels = WandItem.loadPartsFromNbt(stack.getTag());

            for (Model model : loadedModels.values())
            {
                String modelName = TextUtils.removeUnderscoresAndCapitalize(TextUtils.removeBeforeLastSlash(model.modelId().getPath()));

                insertedModelsScrollPanel.addAndUpdateButtons(this, modelName, model.id(), menu.getSelectedModelId() == model.id());
            }

            updateMoveButtons(true);
        } else {
            updateMoveButtons(false);
        }
    }

    private void updateMoveButtons(boolean active) {
        for (BenchButton btn : moveButtons) {
            btn.active = active;
        }
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

        renderModelText(poseStack);
    }

    private void renderModelText(PoseStack stack) {
        Vector3f translation = new Vector3f(0, 0, 0);
        Vector3f rotation = new Vector3f(0, 0, 0);
        Vector3f scale = new Vector3f(0, 0, 0);

        if (loadedModels.containsKey(menu.getSelectedModelId())) {
            Model model = loadedModels.get(menu.getSelectedModelId());

            translation = model.translation();
            rotation = model.rotation();
            scale = model.scale();
        }

        stack.pushPose();

        stack.scale(0.5f, 0.6f, 0.5f);

        this.font.draw(stack, Component.literal(String.valueOf(translation.x())), 172, 37, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(translation.y())), 198, 37, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(translation.z())), 224, 37, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(rotation.x())), 172, 80.5f, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(rotation.y())), 198, 80.5f, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(rotation.z())), 224, 80.5f, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(scale.x())), 172, 124, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(scale.y())), 198, 124, 4210752);
        this.font.draw(stack, Component.literal(String.valueOf(scale.z())), 224, 124, 4210752);

        stack.popPose();
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
            stack.scale(20.0F, 20.0F, 20.0F);

            PoseStack itemPoseStack = new PoseStack();
            itemPoseStack.pushPose();

            itemPoseStack.mulPoseMatrix(stack.last().pose());

            itemPoseStack.translate(tranX, tranY, 0);
            itemPoseStack.scale(scale, scale, scale);

            float currentRot = (Objects.requireNonNull(menu.blockEntity.getLevel()).getGameTime() + partialTicks) * 0.5f;
            itemPoseStack.mulPose(Axis.YP.rotationDegrees(currentRot));

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

        RenderSystem.setShaderTexture(0, texture);

        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        enableScissor(x + 126, y + 8, x + 196, y + 102);

        renderItem(poseStack, partialTick, x, y);

        disableScissor();

        searchBox.render(poseStack, mouseX, mouseY, partialTick);

        manaInfoArea.draw(poseStack, x, y);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        return isMouseAboveArea((int) pMouseX, (int) pMouseY, x, y, 7, 104, 73, 14) && pButton == 0 ? searchBox.mouseClicked(pMouseX, pMouseY, pButton) : super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void previewBtnPressed(int btnId) {
        switch (btnId) {
            case 22 -> scale++;
            case 23 -> scale--;
            case 24 -> tranX++;
            case 25 -> tranY--;
            case 26 -> tranY++;
            case 27 -> tranX--;
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.searchBox.tick();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (this.minecraft != null && this.minecraft.player != null && pKeyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        boolean f = this.searchBox.keyPressed(pKeyCode, pScanCode, pModifiers);

        onSearchBoxTextChanged(searchBox.getValue());

        return f || this.searchBox.canConsumeInput() || super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        boolean f = this.searchBox.charTyped(pCodePoint, pModifiers);

        onSearchBoxTextChanged(searchBox.getValue());

        return f;
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

    public static class BenchEditBox extends EditBox {
        private final ResourceLocation elementsTexture;

        public BenchEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage, ResourceLocation elementsTexture) {
            super(pFont, pX, pY, pWidth, pHeight, pMessage);

            this.elementsTexture = elementsTexture;
        }

        @Override
        public void renderWidget(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            if (this.isVisible()) {
                RenderSystem.setShaderTexture(0, elementsTexture);

                blit(poseStack, this.getX(), this.getY(), (this.isFocused() ? 1 : 0) * 73, 0, 73, 14);

                int i2 = this.isEditable ? this.textColor : this.textColorUneditable;
                int j = this.cursorPos - this.displayPos;
                int k = this.highlightPos - this.displayPos;
                String s = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
                boolean flag = j >= 0 && j <= s.length();
                boolean flag1 = this.isFocused() && this.frame / 6 % 2 == 0 && flag;
                int l = this.bordered ? this.getX() + 4 : this.getX();
                int i1 = this.bordered ? this.getY() + (this.height - 8) / 2 : this.getY();
                int j1 = l;
                if (k > s.length()) {
                    k = s.length();
                }

                if (!s.isEmpty()) {
                    String s1 = flag ? s.substring(0, j) : s;
                    j1 = this.font.drawShadow(poseStack, this.formatter.apply(s1, this.displayPos), (float)l, (float)i1, i2);
                }

                boolean flag2 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
                int k1 = j1;
                if (!flag) {
                    k1 = j > 0 ? l + this.width : l;
                } else if (flag2) {
                    k1 = j1 - 1;
                    --j1;
                }

                if (!s.isEmpty() && flag && j < s.length()) {
                    this.font.drawShadow(poseStack, this.formatter.apply(s.substring(j), this.cursorPos), (float)j1, (float)i1, i2);
                }

                if (this.hint != null && s.isEmpty() && !this.isFocused()) {
                    this.font.drawShadow(poseStack, this.hint, (float)j1, (float)i1, i2);
                }

                if (!flag2 && this.suggestion != null) {
                    this.font.drawShadow(poseStack, this.suggestion, (float)(k1 - 1), (float)i1, -8355712);
                }

                if (flag1) {
                    if (flag2) {
                        GuiComponent.fill(poseStack, k1, i1 - 1, k1 + 1, i1 + 1 + 9, -3092272);
                    } else {
                        this.font.drawShadow(poseStack, "_", (float)k1, (float)i1, i2);
                    }
                }

                if (k != j) {
                    int l1 = l + this.font.width(s.substring(0, k));
                    this.renderHighlight(poseStack, k1, i1 - 1, l1 - 1, i1 + 1 + 9);
                }
            }
        }
    }

    public static class BenchButton extends ExtendedButton {
        private final int baseY;
        public final String btnText;
        public final int modelId;
        private final ButtonType buttonType;
        public boolean selected;
        private final ResourceLocation elementsTexture;

        public BenchButton(WandBenchScreen screen, String btnText, int modelId, int x, int y, int width, int height, int btnId, ButtonType buttonType, boolean canBeSelected, @Nullable ModelsScrollPanel scrollPanel, ResourceLocation elementsTexture) {
            super(x, y, width, height, Component.literal(btnText), button -> onButtonClicked(screen, btnId, modelId, btnText, canBeSelected, scrollPanel));

            this.selected = false;

            this.baseY = y;
            this.btnText = btnText;
            this.modelId = modelId;
            this.buttonType = buttonType;
            this.elementsTexture = elementsTexture;
        }

        public static void onButtonClicked(WandBenchScreen localScreen, int btnId, int modelId, String btnText, boolean canBeSelected, @Nullable ModelsScrollPanel scrollPanel) {
            if (btnId == 1)
                localScreen.menu.setSelectedModelId(modelId);

            if (btnId <= 21 && Minecraft.getInstance().player != null)
                ModMessages.sendToServer(new WandBenchButtonC2SPacket(localScreen.menu.blockEntity.getBlockPos(), btnId, modelId, btnText, Minecraft.getInstance().player.isShiftKeyDown()));
            else
                localScreen.previewBtnPressed(btnId);

            if (scrollPanel != null && canBeSelected) {
                scrollPanel.buttonPressed(btnText, modelId, btnId == 1);
            }
        }

        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            Minecraft mc = Minecraft.getInstance();
            int k = !this.active ? 1 : (this.isHovered() ? 2 : ((this.isFocused() || selected) ? 1 : 0));

            RenderSystem.setShaderTexture(0, elementsTexture);

            switch (buttonType) {
                case model -> blit(poseStack, this.getX(), this.getY(), k * 58, 14, this.getWidth(), this.getHeight());
                case normal -> blit(poseStack, this.getX(), this.getY(), 198, k * 14, this.getWidth(), this.getHeight());
                case up_arrow -> blit(poseStack, this.getX(), this.getY(), 146 + (k * 9), 0, this.getWidth(), this.getHeight());
                case down_arrow -> blit(poseStack, this.getX(), this.getY(), 146 + (k * 9), 7, this.getWidth(), this.getHeight());
                case left_arrow -> blit(poseStack, this.getX(), this.getY(), 174, 15 + (k * 15), this.getWidth(), this.getHeight());
                case right_arrow -> blit(poseStack, this.getX(), this.getY(), 181, 15 + (k * 15), this.getWidth(), this.getHeight());
                case plus -> blit(poseStack, this.getX(), this.getY(), 144, 28 + (k * 15), this.getWidth(), this.getHeight());
                case minus -> blit(poseStack, this.getX(), this.getY(), 159, 28 + (k * 15), this.getWidth(), this.getHeight());
            }

            if (buttonType == ButtonType.model || buttonType == ButtonType.normal) {
                final FormattedText buttonText = mc.font.ellipsize(this.getMessage(), this.width - 6); // Remove 6 pixels so that the text is always contained within the button's borders
                drawCenteredString(poseStack, mc.font, Language.getInstance().getVisualOrder(buttonText), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor());
            }
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
        private final List<BenchButton> originalButtons = new ArrayList<>();
        private final List<BenchButton> sortedButtons = new ArrayList<>();
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

        public void addAndUpdateButtons(WandBenchScreen screen, String modelName, int modelId, boolean isFocused) {
            BenchButton modelButton = new BenchButton(screen, modelName, modelId, left, top + totalButtonHeight, this.width - 15, 14, buttonsId, ButtonType.model, buttonsId == 1, this, elementsTexture);
            modelButton.setFocused(isFocused);
            modelButton.selected = isFocused;
            this.originalButtons.add(modelButton);
            this.sortedButtons.add(modelButton);
            totalButtonHeight += modelButton.getHeight();
        }

        public void clearContent() {
            totalButtonHeight = 0;

            this.sortedButtons.clear();
            this.originalButtons.clear();
        }

        public void buttonPressed(String btnText, int modelId, boolean isAvailable) {
            for (BenchButton btn : sortedButtons) {
                if ((Objects.equals(btn.btnText, btnText) && !isAvailable) || (btn.modelId == modelId && isAvailable)) {
                    btn.selected = true;
                } else {
                    btn.setFocused(false);
                    btn.selected = false;
                }
            }
        }

        public void searchContent(String search) {
            List<BenchButton> tempButtons = new ArrayList<>();
            int tempTotalButtonsHeight = 0;

            for (BenchButton btn : this.originalButtons) {
                if (btn.btnText.contains(search)) {
                    tempTotalButtonsHeight += btn.getHeight();
                    tempButtons.add(btn);
                }
            }

            this.sortedButtons.clear();

            this.sortedButtons.addAll(tempButtons);
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
            return this.sortedButtons;
        }

        @Override
        protected int getContentHeight()
        {
            return this.totalButtonHeight;
        }

        @Override
        protected void drawPanel(PoseStack stack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY)
        {
            for (BenchButton button : this.sortedButtons)
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
