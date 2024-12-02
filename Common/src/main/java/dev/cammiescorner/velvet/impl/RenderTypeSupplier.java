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
package dev.cammiescorner.velvet.impl;

import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.velvet.mixin.client.render.RenderStateShardAccessor;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RenderTypeSupplier {
	private final Consumer<RenderType.CompositeState.CompositeStateBuilder> transform;
	private final Map<RenderType, RenderType> renderTypeCache = new HashMap<>();
	private final String uniqueName;
	private final @Nullable VertexFormat vertexFormat;

	public static RenderTypeSupplier renderTarget(String name, Runnable setupState, Runnable cleanupState) {
		RenderStateShard.OutputStateShard target = new RenderStateShard.OutputStateShard(
				name + "_target",
				setupState,
				cleanupState
		);
		return new RenderTypeSupplier(name, builder -> builder.setOutputState(target));
	}

	public static RenderTypeSupplier shader(String name, VertexFormat vertexFormat, Supplier<ShaderInstance> shaderSupplier) {
		RenderStateShard shader = Helper.makeShader(shaderSupplier);
		return new RenderTypeSupplier(name, vertexFormat, builder -> Helper.applyShader(builder, shader));
	}

	public RenderTypeSupplier(String name, Consumer<RenderType.CompositeState.CompositeStateBuilder> transformer) {
		this(name, null, transformer);
	}

	public RenderTypeSupplier(String name, @Nullable VertexFormat vertexFormat, Consumer<RenderType.CompositeState.CompositeStateBuilder> transformer) {
		this.uniqueName = name;
		this.vertexFormat = vertexFormat;
		this.transform = transformer;
	}

	public RenderType getRenderType(RenderType baseLayer) {
		RenderType existing = this.renderTypeCache.get(baseLayer);
		if(existing != null) {
			return existing;
		}
		String newName = ((RenderStateShardAccessor) baseLayer).getName() + "_" + this.uniqueName;
		RenderType newLayer = RenderTypeDuplicator.copy(baseLayer, newName, this.vertexFormat, this.transform);
		this.renderTypeCache.put(baseLayer, newLayer);
		return newLayer;
	}

	/**
	 * Big brain move right there
	 */
	private static class Helper extends RenderStateShard {
		public static RenderStateShard makeShader(Supplier<ShaderInstance> shader) {
			return new ShaderStateShard(shader);
		}

		public static void applyShader(RenderType.CompositeState.CompositeStateBuilder builder, RenderStateShard shader) {
			builder.setShaderState((ShaderStateShard) shader);
		}

		private Helper(String name, Runnable beginAction, Runnable endAction) {
			super(name, beginAction, endAction);
		}
	}
}
