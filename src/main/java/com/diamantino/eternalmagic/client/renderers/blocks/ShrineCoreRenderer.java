package com.diamantino.eternalmagic.client.renderers.blocks;

import com.diamantino.eternalmagic.ModConstants;
import com.diamantino.eternalmagic.blockentities.ShrineCoreBlockEntity;
import com.diamantino.eternalmagic.client.model.entities.ShrineCoreInternalModel;
import com.diamantino.eternalmagic.client.renderers.FluidVertexConsumer;
import com.diamantino.eternalmagic.registration.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShrineCoreRenderer implements BlockEntityRenderer<ShrineCoreBlockEntity> {
    public static final Material coreLocation = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModConstants.modId, "entity/shrine_core_internal"));
    private final ShrineCoreInternalModel coreModel;

    private final List<BlockState> multiplierBlocks = new ArrayList<>();

    public ShrineCoreRenderer(BlockEntityRendererProvider.Context pContext) {
        this.coreModel = new ShrineCoreInternalModel(pContext.bakeLayer(ShrineCoreInternalModel.layer));

        this.multiplierBlocks.add(ModBlocks.decorativeBlocks.get("dark_stone").get().defaultBlockState());
        this.multiplierBlocks.add(Blocks.IRON_BLOCK.defaultBlockState());
        this.multiplierBlocks.add(Blocks.GOLD_BLOCK.defaultBlockState());
        this.multiplierBlocks.add(Blocks.DIAMOND_BLOCK.defaultBlockState());
        this.multiplierBlocks.add(Blocks.EMERALD_BLOCK.defaultBlockState());
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

        if (!pBlockEntity.isAssembled)
            renderBuilding(pBlockEntity, pPoseStack, pBufferSource);
    }

    private void renderBuilding(ShrineCoreBlockEntity pBlockEntity, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource) {
        Level level = Minecraft.getInstance().level;

        if (level != null && pBlockEntity.multiblockTemplateBlocks.size() > 0) {
            for (StructureTemplate.StructureBlockInfo structureBlockInfo : pBlockEntity.multiblockTemplateBlocks) {
                BlockPos pos = structureBlockInfo.pos().offset(-5, -4, -5);
                BlockPos worldPos = pBlockEntity.getBlockPos().offset(pos.getX(), pos.getY(), pos.getZ());
                BlockState state = structureBlockInfo.state();
                BlockState worldState = level.getBlockState(worldPos);

                BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();

                if (state.is(Blocks.IRON_BLOCK) && !multiplierBlocks.contains(worldState)) {
                    pPoseStack.pushPose();
                    pPoseStack.translate(pos.getX() + 0.25f, pos.getY() + 0.25f, pos.getZ() + 0.25f);

                    pPoseStack.scale(0.5f, 0.5f, 0.5f);

                    boolean valid = (worldState.isAir() || (worldState.is(ModBlocks.decorativeBlocks.get("dark_stone").get()) || worldState.is(Blocks.IRON_BLOCK) || worldState.is(Blocks.GOLD_BLOCK) || worldState.is(Blocks.DIAMOND_BLOCK) || worldState.is(Blocks.EMERALD_BLOCK)));

                    BlockState localState = multiplierBlocks.get(pBlockEntity.showingBlockId);

                    BakedModel model = blockRenderDispatcher.getBlockModel(localState);

                    translateAndRenderModel(pBlockEntity, localState, pPoseStack, pBufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, model, valid);
                    renderBlockEntity(level, pos, pPoseStack, pBufferSource);

                    pPoseStack.popPose();
                } else if (worldState != state && !state.is(Blocks.IRON_BLOCK)) {
                    pPoseStack.pushPose();

                    FluidState fluidState = state.getFluidState();

                    pPoseStack.translate(pos.getX() + 0.25f, pos.getY() + 0.25f, pos.getZ() + 0.25f);

                    pPoseStack.scale(0.5f, 0.5f, 0.5f);

                    if (!fluidState.isEmpty()) {
                        pPoseStack.translate(-pos.getX(), 0, (float) -pos.getZ());

                        pPoseStack.translate(-5, -1, -5);

                        VertexConsumer vertexconsumer = new FluidVertexConsumer(pBufferSource, fluidState, pPoseStack.last().pose(), pPoseStack.last().normal());

                        blockRenderDispatcher.renderLiquid(structureBlockInfo.pos(), pBlockEntity.multiblockLevel, vertexconsumer, state, fluidState);

                        pPoseStack.translate(5, 1, 5);

                        pPoseStack.translate(pos.getX(), 0, pos.getZ());
                    }

                    //pPoseStack.translate(pos.getX() + 0.25f, pos.getY() + 0.25f, pos.getZ() + 0.25f);

                    BakedModel model = blockRenderDispatcher.getBlockModel(state);

                    translateAndRenderModel(pBlockEntity, state, pPoseStack, pBufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, model, worldState.isAir());
                    renderBlockEntity(level, pos, pPoseStack, pBufferSource);

                    pPoseStack.popPose();
                }
            }
        }
    }

    private static void translateAndRenderModel(ShrineCoreBlockEntity pBlockEntity, BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, boolean valid) {
        RandomSource random = RandomSource.create();

        poseStack.pushPose();

        if (!valid) {
            poseStack.scale(2.02f, 2.02f, 2.02f);
            poseStack.translate(-0.255f, -0.255f, -0.255f);
        }

        ModelData data = model.getModelData(Objects.requireNonNull(Minecraft.getInstance().level), pBlockEntity.getBlockPos(), state, pBlockEntity.getModelData());

        List<RenderType> renderTypes = model.getRenderTypes(state, random, data).asList();

        for (RenderType renderType : renderTypes)
            renderModel(random, model, data, state, combinedLight, combinedOverlay, poseStack, bufferSource.getBuffer(renderType), renderType, valid);

        poseStack.popPose();
    }

    private static void renderModel(RandomSource random, BakedModel model, ModelData data, BlockState state, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer, RenderType renderType, boolean valid) {

        for(Direction direction : Direction.values()){
            random.setSeed(42L);
            renderQuads(poseStack, buffer, model.getQuads(state, direction, random, data, renderType), combinedLight, combinedOverlay, valid);
        }

        random.setSeed(42L);
        renderQuads(poseStack, buffer, model.getQuads(state, null, random, data, renderType), combinedLight, combinedOverlay, valid);
    }

    private static void renderQuads(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, int combinedLight, int combinedOverlay, boolean valid) {
        PoseStack.Pose matrix = poseStack.last();

        for(BakedQuad bakedquad : quads)
            buffer.putBulkData(matrix, bakedquad, 1, valid ? 1 : 0.5f, valid ? 1 : 0.5f, combinedLight, combinedOverlay);
    }

    private static void renderBlockEntity(Level level, BlockPos pos, PoseStack poseStack, MultiBufferSource bufferSource){
        BlockEntity entity = level.getBlockEntity(pos);

        if(entity != null){
            BlockEntityRenderer<BlockEntity> entityRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(entity);

            if(entityRenderer != null){
                poseStack.pushPose();
                poseStack.translate(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5);

                entityRenderer.render(entity, Minecraft.getInstance().getFrameTime(), poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

                poseStack.popPose();
            }
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
