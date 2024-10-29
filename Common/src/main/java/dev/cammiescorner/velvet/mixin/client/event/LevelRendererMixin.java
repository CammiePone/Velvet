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
package dev.cammiescorner.velvet.mixin.client.event;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.velvet.api.event.EntitiesPostRenderCallback;
import dev.cammiescorner.velvet.api.event.EntitiesPreRenderCallback;
import dev.cammiescorner.velvet.api.event.PostWorldRenderCallbackV3;
import dev.cammiescorner.velvet.api.experimental.ReadableDepthRenderTarget;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Unique private Frustum frustum;

	//TODO
	@ModifyVariable(
			method = "renderLevel",
			at = @At(value = "CONSTANT", args = "stringValue=entities", ordinal = 0, shift = At.Shift.BEFORE)
	)
	private Frustum captureFrustum(Frustum frustum) {
		this.frustum = frustum;
		return frustum;
	}


	@Inject(
			method = "renderLevel",
			at = @At(value = "CONSTANT", args = "stringValue=entities", ordinal = 0)
	)
	private void firePreRenderEntities(DeltaTracker tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		EntitiesPreRenderCallback.EVENT.invoker().beforeEntitiesRender(camera, frustum, tickCounter.getGameTimeDeltaPartialTick(false));
	}

	@Inject(
			method = "renderLevel",
			at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0)
	)
	private void firePostRenderEntities(DeltaTracker tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		EntitiesPostRenderCallback.EVENT.invoker().onEntitiesRendered(camera, frustum, tickCounter.getGameTimeDeltaPartialTick(false));
	}

	@Inject(
			method = "renderLevel",
			slice = @Slice(from = @At(value = "FIELD:LAST", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/LevelRenderer;transparencyChain:Lnet/minecraft/client/renderer/PostChain;")),
			at = {
					@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;process(F)V"),
					@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V", ordinal = 1, shift = At.Shift.AFTER, remap = false)
			}
	)
	private void hookPostWorldRender(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local PoseStack matrices) {
		((ReadableDepthRenderTarget) Minecraft.getInstance().getMainRenderTarget()).freezeDepthMap();
		PostWorldRenderCallbackV3.EVENT.invoker().onWorldRendered(matrices, frustumMatrix, projectionMatrix, camera, deltaTracker.getGameTimeDeltaPartialTick(true));
	}
}
