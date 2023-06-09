package com.diamantino.eternalmagic.client.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.pipeline.RemappingVertexPipeline;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class FluidVertexConsumer implements VertexConsumer
{
    private final VertexConsumer superParent;
    private final VertexConsumer parent;
    private final Matrix4f pose;
    private final Matrix3f normal;

    public FluidVertexConsumer(MultiBufferSource buffer, FluidState fluid, Matrix4f pose, Matrix3f normal)
    {
        RenderType renderType = ItemBlockRenderTypes.getRenderLayer(fluid);
        this.superParent = buffer.getBuffer(RenderTypeHelper.getEntityRenderType(renderType, false));
        this.parent = new RemappingVertexPipeline(superParent, DefaultVertexFormat.NEW_ENTITY);
        this.pose = pose;
        this.normal = normal;
    }

    @Override
    public @NotNull VertexConsumer vertex(double pX, double pY, double pZ)
    {
        return vertex(pose, (float) pX, (float) pY, (float) pZ);
    }

    @Override
    public @NotNull VertexConsumer color(int pRed, int pGreen, int pBlue, int pAlpha)
    {
        parent.color(pRed, pGreen, pBlue, pAlpha);
        return this;
    }

    @Override
    public VertexConsumer uv(float pU, float pV)
    {
        parent.uv(pU, pV);
        return this;
    }

    @Override
    public @NotNull VertexConsumer overlayCoords(int pU, int pV)
    {
        parent.overlayCoords(pU, pV);
        return this;
    }

    @Override
    public @NotNull VertexConsumer uv2(int pU, int pV)
    {
        parent.uv2(pU, pV);
        return this;
    }

    @Override
    public @NotNull VertexConsumer normal(float pX, float pY, float pZ)
    {
        return normal(normal, pX, pY, pZ);
    }

    @Override
    public void endVertex()
    {
        parent.endVertex();
    }

    @Override
    public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA)
    {
        parent.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
    }

    @Override
    public void unsetDefaultColor()
    {
        parent.unsetDefaultColor();
    }

    @Override
    public @NotNull VertexConsumer vertex(Matrix4f pMatrix, float pX, float pY, float pZ)
    {
        Vector4f vector4f = pMatrix.transform(new Vector4f(pX, pY, pZ, 1.0F));
        parent.vertex(vector4f.x(), vector4f.y(), vector4f.z());
        return this;
    }

    @Override
    public @NotNull VertexConsumer normal(Matrix3f pMatrix, float pX, float pY, float pZ)
    {
        Vector3f vector3f = pMatrix.transform(new Vector3f(pX, pY, pZ));
        parent.normal(vector3f.x(), vector3f.y(), vector3f.z());
        return this;
    }
}