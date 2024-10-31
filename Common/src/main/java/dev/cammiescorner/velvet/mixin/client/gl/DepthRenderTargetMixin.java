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
package dev.cammiescorner.velvet.mixin.client.gl;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.velvet.api.experimental.ReadableDepthRenderTarget;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(RenderTarget.class)
public abstract class DepthRenderTargetMixin implements ReadableDepthRenderTarget {
	@Unique private int stillDepthTexture = -1;

	@Shadow @Final public boolean useDepth;
	@Shadow public int width;
	@Shadow public int height;

	@Shadow public abstract void bindWrite(boolean boolean_1);

	@Inject(
		method = "createBuffers",
		at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;depthBufferId:I", shift = AFTER)
	)
	private void initFbo(int width, int height, boolean flushErrors, CallbackInfo ci) {
		if(this.useDepth) {
			this.stillDepthTexture = setupDepthTexture();
		}
	}

	@Unique
	private int setupDepthTexture() {
		int shadowMap = GL11.glGenTextures();
		RenderSystem.bindTexture(shadowMap);
		RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		GlStateManager._texImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, this.width, this.height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, null);
		return shadowMap;
	}

	@Inject(method = "destroyBuffers", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;depthBufferId:I"))
	private void delete(CallbackInfo ci) {
		if(this.stillDepthTexture > -1) {
			// delete texture
			TextureUtil.releaseTextureId(this.stillDepthTexture);
			this.stillDepthTexture = -1;
		}
	}

	@Override
	public int getStillDepthMap() {
		return this.stillDepthTexture;
	}

	@Override
	public void freezeDepthMap() {
		if(this.useDepth) {
			this.bindWrite(false);
			RenderSystem.bindTexture(this.stillDepthTexture);
			glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, this.width, this.height);
		}
	}
}
