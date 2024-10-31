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
import com.mojang.blaze3d.platform.GlConst;
import dev.cammiescorner.velvet.impl.CustomFormatRenderTarget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows render targets to have custom formats. Default is still GL_RGBA8.
 *
 * @see CustomFormatRenderTarget
 */
@Mixin(RenderTarget.class)
public abstract class CustomFormatRenderTargetMixin {
	@Unique
	private int format = GlConst.GL_RGBA8;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void satin$setFormat(boolean useDepth, CallbackInfo ci) {
		@Nullable CustomFormatRenderTarget.TextureFormat format = CustomFormatRenderTarget.getCustomFormat();
		if(format != null) {
			this.format = format.value;
			CustomFormatRenderTarget.clearCustomFormat();
		}
	}

	@ModifyArg(
		method = "createBuffers",
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;setFilterMode(IZ)V"),
			to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glBindFramebuffer(II)V", remap = false)
		),
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V", remap = false),
		index = 2
	)
	private int satin$modifyInternalFormat(int internalFormat) {
		return this.format;
	}
}
