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
import dev.cammiescorner.velvet.impl.BlockRenderLayerRegistry;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public abstract class RenderTypeMixin extends RenderStateShard {
	private RenderTypeMixin() {
		super(null, null, null);
	}

	@Inject(
			method = "chunkBufferLayers",
			at = @At("RETURN"),
			cancellable = true
	)
	private static void getBlockLayers(CallbackInfoReturnable<ImmutableList<Object>> info) {
		info.setReturnValue(
				ImmutableList.builder()
						.addAll(info.getReturnValue())
						.addAll(BlockRenderLayerRegistry.INSTANCE.getTypes()
						).build());
	}
}
