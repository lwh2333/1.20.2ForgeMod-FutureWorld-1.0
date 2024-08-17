package com.mcfire.autoplay.entity.renderer;

import com.mcfire.autoplay.entity.model.RobotModel;
import com.mcfire.autoplay.entity.monster.RobotEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class RobotRenderer extends GeoEntityRenderer<RobotEntity> {
    public RobotRenderer(Context context) {
        super(context, new RobotModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.shadowRadius=3F;
    }

    @Override
    public void applyRenderLayers(PoseStack poseStack, RobotEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

    }
}
