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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.cammiescorner.velvet.impl.CustomFormatRenderTarget;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PostChain.class)
public class CustomFormatPostEffectProcessorMixin {
	@Inject(method = "parseTargetNode", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;addTempTarget(Ljava/lang/String;II)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void parseCustomTargetFormat(JsonElement jsonTarget, CallbackInfo ci, JsonObject jsonObject) {
		String format = GsonHelper.getAsString(jsonObject, CustomFormatRenderTarget.FORMAT_KEY, null);
		if(format != null) {
			CustomFormatRenderTarget.prepareCustomFormat(format);
		}
	}

	/**
	 * @reason need to clean up state if an exception is thrown
	 */
	@Inject(
			method = "load",
			slice = @Slice(
					from = @At(value = "CONSTANT:FIRST", args = "stringValue=targets"),
					to = @At(value = "CONSTANT:FIRST", args = "stringValue=passes")
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/ChainedJsonException;forException(Ljava/lang/Exception;)Lnet/minecraft/server/ChainedJsonException;"
			),
			allow = 1
	)
	private void cleanupCustomTargetFormat(TextureManager textureManager, ResourceLocation id, CallbackInfo ci) {
		CustomFormatRenderTarget.clearCustomFormat();
	}
}
