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

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.velvet.impl.BlockRenderTypeRegistry;
import dev.cammiescorner.velvet.impl.RenderTypeUtil;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RenderType.class)
public abstract class RenderTypeMixin extends RenderStateShard {

	private RenderTypeMixin(String name, Runnable setupState, Runnable clearState) {
		super(name, setupState, clearState);
		throw new UnsupportedOperationException();
	}

	@Mutable
	@Final
	@Shadow
	private static ImmutableList<RenderType> CHUNK_BUFFER_LAYERS;

	@Unique
	private static boolean velvet$chunkBuffersInitialized = false;

	@ModifyReturnValue(method = "chunkBufferLayers", at = @At("RETURN"))
	private static List<RenderType> getBlockLayers(List<RenderType> original) {
		if (!velvet$chunkBuffersInitialized && RenderTypeUtil.freezeRegistryHack) {
			CHUNK_BUFFER_LAYERS = ImmutableList.<RenderType>builder().addAll(CHUNK_BUFFER_LAYERS).addAll(BlockRenderTypeRegistry.INSTANCE.getTypes()).build();
			velvet$chunkBuffersInitialized = true;
			return ImmutableList.<RenderType>builder().addAll(original).addAll(BlockRenderTypeRegistry.INSTANCE.getTypes()).build();
		}

		return original;
	}
}
