package com.diamantino.eternalmagic.client.model.entities;

import com.diamantino.eternalmagic.ModReferences;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class WandBenchSphereModel extends Model {
	public static final ModelLayerLocation layer = new ModelLayerLocation(new ResourceLocation(ModReferences.modId, "wand_bench_sphere"), "main");
	private final ModelPart Ring1;
	private final ModelPart Ring2;
	public final ModelPart ItemSpawn;

	public WandBenchSphereModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.Ring1 = root.getChild("Ring1");
		this.Ring2 = Ring1.getChild("Ring2");
		this.ItemSpawn = Ring2.getChild("ItemSpawn");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Ring1 = partdefinition.addOrReplaceChild("Ring1", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition Side1 = Ring1.addOrReplaceChild("Side1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, 7.5F, -0.5F, 4.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 4).addBox(-3.5F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 2).addBox(2.0F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 18).addBox(3.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-4.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 23).addBox(-5.0F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 23).addBox(4.5F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(23, 7).addBox(5.0F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(11, 23).addBox(-5.5F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Side2 = Ring1.addOrReplaceChild("Side2", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, 7.5F, -0.5F, 4.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-3.5F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(2.0F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(13, 17).addBox(3.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 17).addBox(-4.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 23).addBox(-5.0F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 18).addBox(4.5F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 16).addBox(5.0F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 14).addBox(-5.5F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition Side3 = Ring1.addOrReplaceChild("Side3", CubeListBuilder.create().texOffs(0, 2).addBox(-2.0F, 7.5F, -0.5F, 4.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(11, 15).addBox(-3.5F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 9).addBox(2.0F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(3.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 14).addBox(-4.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 22).addBox(-5.0F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 12).addBox(4.5F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 22).addBox(5.0F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-5.5F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition Side4 = Ring1.addOrReplaceChild("Side4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 7.5F, -0.5F, 4.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 6).addBox(-3.5F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 14).addBox(2.0F, 7.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 12).addBox(3.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 16).addBox(-4.5F, 6.5F, -0.5F, 1.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 21).addBox(-5.0F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 21).addBox(4.5F, 6.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(11, 21).addBox(5.0F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 9).addBox(-5.5F, 5.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition Ring2 = Ring1.addOrReplaceChild("Ring2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Side5 = Ring2.addOrReplaceChild("Side5", CubeListBuilder.create().texOffs(9, 3).addBox(-1.5F, 5.5F, -0.5F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-3.0F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 21).addBox(-3.5F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 5).addBox(-4.0F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 1).addBox(3.5F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 3).addBox(3.0F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(11, 13).addBox(1.5F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Side6 = Ring2.addOrReplaceChild("Side6", CubeListBuilder.create().texOffs(9, 1).addBox(-1.5F, 5.5F, -0.5F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 12).addBox(-3.0F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 20).addBox(-3.5F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 20).addBox(-4.0F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(3.5F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 19).addBox(3.0F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(1.5F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition Side7 = Ring2.addOrReplaceChild("Side7", CubeListBuilder.create().texOffs(8, 8).addBox(-1.5F, 5.5F, -0.5F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(11, 11).addBox(-3.0F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 17).addBox(-3.5F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 15).addBox(-4.0F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 13).addBox(3.5F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 11).addBox(3.0F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 10).addBox(1.5F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition Side8 = Ring2.addOrReplaceChild("Side8", CubeListBuilder.create().texOffs(0, 8).addBox(-1.5F, 5.5F, -0.5F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(-3.0F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(11, 19).addBox(-3.5F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 7).addBox(-4.0F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 19).addBox(3.5F, 4.0F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 18).addBox(3.0F, 4.5F, -0.5F, 0.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 5).addBox(1.5F, 5.0F, -0.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition ItemSpawn = Ring2.addOrReplaceChild("ItemSpawn", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public void setupAnim() {
		Ring1.yRot += 0.001f;
		Ring2.xRot += 0.001f;
		ItemSpawn.xRot -= 0.001f;
		ItemSpawn.yRot -= 0.001f;
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Ring1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}