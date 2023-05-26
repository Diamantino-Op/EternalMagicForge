package com.diamantino.eternalmagic.client.renderers.blocks;

import com.diamantino.eternalmagic.ModReferences;
import com.diamantino.eternalmagic.blockentities.ShrineCoreBlockEntity;
import com.diamantino.eternalmagic.client.model.entities.ShrineCoreInternalModel;
import com.diamantino.eternalmagic.client.model.entities.WandBenchSphereModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

public class ShrineCoreRenderer implements BlockEntityRenderer<ShrineCoreBlockEntity> {
    public static final Material coreLocation = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModReferences.modId, "entity/shrine_core_internal"));
    private final ShrineCoreInternalModel coreModel;

    public ShrineCoreRenderer(BlockEntityRendererProvider.Context pContext) {
        this.coreModel = new ShrineCoreInternalModel(pContext.bakeLayer(ShrineCoreInternalModel.layer));
    }

    @Override
    public void render(@NotNull ShrineCoreBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemStack itemStack = pBlockEntity.getRenderStack();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        pPoseStack.pushPose();
        pPoseStack.translate(0.5F, -0.5F, 0.5F);

        if (pBlockEntity.getLevel() != null && !itemStack.isEmpty())
            coreModel.setupAnim(pBlockEntity, pPartialTick);

        VertexConsumer vertexconsumer = coreLocation.buffer(pBufferSource, RenderType::entitySolid);
        coreModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        if (pBlockEntity.getLevel() != null && !itemStack.isEmpty()) {
            pPoseStack.translate(0, 1F, 0);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(coreModel.Core.xRot));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(coreModel.Core.yRot));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(coreModel.Core.zRot));
            pPoseStack.scale(0.5f, 0.5f, 0.5f);

            itemRenderer.renderStatic(itemStack, ItemDisplayContext.GUI, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, null, 1);
        }

        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
