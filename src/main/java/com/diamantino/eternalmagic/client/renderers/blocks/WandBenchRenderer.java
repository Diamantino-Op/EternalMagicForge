package com.diamantino.eternalmagic.client.renderers.blocks;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.blockentities.WandBenchBlockEntity;
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

public class WandBenchRenderer implements BlockEntityRenderer<WandBenchBlockEntity> {
    public static final Material sphereLocation = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModConstants.modId, "entity/wand_bench_sphere"));
    private final WandBenchSphereModel wandBenchSphereModel;

    public WandBenchRenderer(BlockEntityRendererProvider.Context pContext) {
        this.wandBenchSphereModel = new WandBenchSphereModel(pContext.bakeLayer(WandBenchSphereModel.layer));
    }

    @Override
    public void render(WandBenchBlockEntity blockEntity, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack itemStack = blockEntity.getRenderStack();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.8F, 0.5F);

        wandBenchSphereModel.setupAnim(blockEntity, partialTick);
        VertexConsumer vertexconsumer = sphereLocation.buffer(bufferSource, RenderType::entitySolid);
        wandBenchSphereModel.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        if (blockEntity.getLevel() != null && !itemStack.isEmpty()) {
            poseStack.translate(0F, 1F, 0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(wandBenchSphereModel.ItemSpawn.xRot));
            poseStack.mulPose(Axis.YP.rotationDegrees(wandBenchSphereModel.ItemSpawn.yRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(wandBenchSphereModel.ItemSpawn.zRot));
            poseStack.scale(0.5f, 0.5f, 0.5f);

            itemRenderer.renderStatic(itemStack, ItemDisplayContext.GUI, getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, null, 1);
        }

        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
