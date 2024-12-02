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
package dev.cammiescorner.velvet.mixin.client.blockrendertype;

import dev.cammiescorner.velvet.impl.BlockRenderTypeRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow protected abstract void renderSectionLayer(RenderType renderType, double x, double y, double z, Matrix4f frustrumMatrix, Matrix4f projectionMatrix);

	//TODO
	@Inject(
		method = "renderLevel",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSectionLayer(Lnet/minecraft/client/renderer/RenderType;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V",
			ordinal = 2,
			shift = At.Shift.AFTER
		),
		slice = @Slice(
			from = @At(
				value = "INVOKE_STRING",
				target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
				args = "ldc=terrain"
			),
			to = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/renderer/DimensionSpecialEffects;constantAmbientLight()Z"
			)
		)
	)
	private void renderCustom(DeltaTracker tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		// Render all the custom ones
		for(RenderType layer : BlockRenderTypeRegistry.INSTANCE.getTypes()) {
			renderSectionLayer(layer, camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, matrix4f, matrix4f2);
		}
	}


}
