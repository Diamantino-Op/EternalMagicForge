package com.diamantino.eternalmagic.client.model;

import com.diamantino.eternalmagic.items.WandItem;
import com.diamantino.eternalmagic.registration.ModItems;
import com.diamantino.eternalmagic.utils.TextUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;

public class ModelLoader
{
    public static final Map<String, ResourceLocation> loadedModels = new LinkedHashMap<>();

    public void registerModels(ModelEvent.RegisterAdditional event) {
        FileToIdConverter fileToIdConverter = FileToIdConverter.json("models/em_models");

        fileToIdConverter.listMatchingResources(Minecraft.getInstance().getResourceManager()).keySet().stream().toList().forEach(modelId -> {
            String id = modelId.toString().replace(".json", "").replace("models/em_models", "em_models");

            ResourceLocation modId = new ResourceLocation(id);
            String modelName = TextUtils.removeUnderscoresAndCapitalize(TextUtils.removeBeforeLastSlash(modelId.getPath()));

            loadedModels.put(modelName, modId);

            event.register(modId);
        });
    }

    public static class ModelGeometryLoader implements IGeometryLoader<EMGeometry> {
        @Override
        public EMGeometry read(JsonObject contents, JsonDeserializationContext ctx)
        {
            return new EMGeometry(ctx.deserialize(contents.get("base_model"), BlockModel.class));
        }
    }

    public static class EMGeometry implements IUnbakedGeometry<EMGeometry>
    {
        private final BlockModel handleModel;

        public EMGeometry(BlockModel handleModel) {
            this.handleModel = handleModel;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
        {
            BakedModel bakedHandle = handleModel.bake(baker, handleModel, spriteGetter, modelState, modelLocation, false);
            return new EMOverrideModel(bakedHandle);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
        {
            handleModel.resolveParents(modelGetter);
        }
    }

    public static class EMOverrideModel extends BakedModelWrapper<BakedModel>
    {
        private final ItemOverrides overrideList;

        public EMOverrideModel(BakedModel originalModel)
        {
            super(originalModel);
            this.overrideList = new EMOverrideList();
        }

        @Override
        public @NotNull ItemOverrides getOverrides()
        {
            return overrideList;
        }
    }

    public static class EMModel extends BakedModelWrapper<BakedModel>
    {
        private final List<BakedQuad> quads;

        public EMModel(BakedModel handleModel, List<BakedQuad> quads)
        {
            super(handleModel);
            this.quads = quads;
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand)
        {
            if (side == null)
            {
                return quads;
            }
            return List.of();
        }

        @Override
        public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext transformType, @NotNull PoseStack poseStack, boolean applyLeftHandTransform)
        {
            this.getTransforms().getTransform(transformType).apply(applyLeftHandTransform, poseStack);
            return this;
        }

        @Override
        public @NotNull List<BakedModel> getRenderPasses(@NotNull ItemStack itemStack, boolean fabulous)
        {
            return List.of(this);
        }
    }

    public static class EMOverrideList extends ItemOverrides
    {
        private static final RandomSource randomSource = RandomSource.create();

        @Nullable
        @Override
        public BakedModel resolve(@NotNull BakedModel model, @NotNull ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed)
        {
            List<BakedQuad> quads = new ArrayList<>(model.getQuads(null, null, randomSource, ModelData.EMPTY, null));

            if (stack.is(ModItems.wandItem.get())) {
                List<Model> modelList = WandItem.loadPartsFromNbt(stack.getTag()).values().stream().toList();

                modelList.forEach(mdl -> {
                    BakedModel bakedModel = Minecraft.getInstance().getModelManager().getModel(mdl.modelId());
                    List<BakedQuad> tempQuads = new ArrayList<>(bakedModel.getQuads(null, null, randomSource, ModelData.EMPTY, null));

                    if (mdl.selected()) {
                        IQuadTransformer colorTransformer = QuadTransformers.applyingColor(127, 0, 0, 0);

                        tempQuads = colorTransformer.process(tempQuads);
                    }

                    Quaternionf leftRot = new Quaternionf().rotationXYZ((float) Math.toRadians(-mdl.rotation().x()), (float) Math.toRadians(-mdl.rotation().y()), (float) Math.toRadians(-mdl.rotation().z()));
                    Quaternionf rightRot = new Quaternionf().rotationXYZ((float) Math.toRadians(mdl.rotation().x()), (float) Math.toRadians(mdl.rotation().y()), (float) Math.toRadians(mdl.rotation().z()));

                    Transformation translation = new Transformation(mdl.translation(), leftRot, mdl.scale(), rightRot);
                    IQuadTransformer transformer = QuadTransformers.applying(translation.applyOrigin(new Vector3f(.5F, .5F, .5F)));

                    quads.addAll(transformer.process(tempQuads));
                });
            }

            return new EMModel(model, quads);
        }
    }
}