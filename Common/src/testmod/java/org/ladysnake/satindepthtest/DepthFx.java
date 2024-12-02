/*
 * Satin
 * Copyright (C) 2019-2024 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package org.ladysnake.satindepthtest;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.velvet.api.event.PostLevelRenderCallback;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.cammiescorner.velvet.api.experimental.ReadableDepthRenderTarget;
import dev.cammiescorner.velvet.api.managed.ManagedShaderEffect;
import dev.cammiescorner.velvet.api.managed.ShaderEffectManager;
import dev.cammiescorner.velvet.api.managed.uniform.Uniform1f;
import dev.cammiescorner.velvet.api.managed.uniform.Uniform3f;
import dev.cammiescorner.velvet.api.managed.uniform.UniformMat4;
import dev.cammiescorner.velvet.api.util.GlMatrices;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.ladysnake.satintestcore.event.EndClientTickEvent;

public class DepthFx implements PostLevelRenderCallback, ShaderEffectRenderCallback, EndClientTickEvent {
    public static final ResourceLocation FANCY_NIGHT_SHADER_ID = ResourceLocation.fromNamespaceAndPath(SatinDepthTest.MOD_ID, "shaders/post/rainbow_ping.json");
    public static final DepthFx INSTANCE = new DepthFx();

    final ManagedShaderEffect testShader = ShaderEffectManager.getInstance().manage(FANCY_NIGHT_SHADER_ID, shader -> {
	    Minecraft mc = Minecraft.getInstance();
        shader.setSamplerUniform("DepthSampler", ReadableDepthRenderTarget.getFrom(mc.getMainRenderTarget()).getStillDepthMap());
        shader.setUniformValue("ViewPort", 0, 0, mc.getWindow().getWidth(), mc.getWindow().getHeight());
    });
    private final Uniform1f uniformSTime = testShader.findUniform1f("STime");
    private final UniformMat4 uniformInverseTransformMatrix = testShader.findUniformMat4("InverseTransformMatrix");
    private final Uniform3f uniformCameraPosition = testShader.findUniform3f("CameraPosition");
    private final Uniform3f uniformCenter = testShader.findUniform3f("Center");

    // fancy shader stuff
    private final Matrix4f projectionMatrix = new Matrix4f();
    private int ticks;

    private boolean isNight() {
		Level level = Minecraft.getInstance().level;
        if (level != null) {
            float celestialAngle = level.getTimeOfDay(1.0f);
            return 0.23f < celestialAngle && celestialAngle < 0.76f;
        }
        return false;
    }

    @Override
    public void onEndTick(Minecraft minecraftClient) {
        if(!minecraftClient.isPaused()) {
            ticks++;
        }
    }

	@Override
	public void onLevelRendered(PoseStack posingStack, Matrix4f modelViewMat, Matrix4f projectionMat, Camera camera, float tickDelta) {
		if (isNight()) {
			uniformSTime.set((ticks + tickDelta) / 20f);
			uniformInverseTransformMatrix.set(GlMatrices.getInverseTransformMatrix(projectionMatrix));
			Vec3 cameraPos = camera.getPosition();
			uniformCameraPosition.set((float)cameraPos.x, (float)cameraPos.y, (float)cameraPos.z);
			Entity e = camera.getEntity();
			uniformCenter.set(lerpf(e.getX(), e.xo, tickDelta), lerpf(e.getY(), e.yo, tickDelta), lerpf(e.getZ(), e.zo, tickDelta));
		}
	}

    @Override
    public void renderShaderEffects(float tickDelta) {
        testShader.render(tickDelta);
    }

    private static float lerpf(double n, double prevN, float tickDelta) {
        return (float) Mth.lerp(tickDelta, prevN, n);
    }


}
