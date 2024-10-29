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
package dev.cammiescorner.velvet.mixin.client.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.velvet.impl.RenderTypeDuplicator;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(RenderType.CompositeRenderType.class)
public abstract class RenderTypeCompositeRenderTypeMixin extends RenderType implements RenderTypeDuplicator.VelvetRenderType {
	@Shadow @Final private CompositeState state;

	public RenderTypeCompositeRenderTypeMixin(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	@Override
	public RenderType copy(String newName, @Nullable VertexFormat vertexFormat, Consumer<CompositeState.CompositeStateBuilder> op) {
		return RenderTypeAccessor.create(
				newName,
				vertexFormat == null ? this.format() : vertexFormat,
				this.mode(),
				this.bufferSize(),
				this.affectsCrumbling(),
				((RenderTypeAccessor) this).getSortOnUpload(),
				this.copyPhaseParameters(op)
		);
	}

	@Override
	public CompositeState copyPhaseParameters(Consumer<CompositeState.CompositeStateBuilder> op) {
		// Yes we can cast to accessor
		@SuppressWarnings("ConstantConditions") CompositeStateAccessor access = ((CompositeStateAccessor) (Object) this.state);
		CompositeState.CompositeStateBuilder builder = CompositeState.builder()
				.setTextureState(access.getTextureState())
				.setShaderState(access.getShaderState())
				.setTransparencyState(access.getTransparencyState())
				.setDepthTestState(access.getDepthTestState())
				.setCullState(access.getCullState())
				.setLightmapState(access.getLightmapState())
				.setOverlayState(access.getOverlayState())
				.setLayeringState(access.getLayeringState())
				.setOutputState(access.getOutputState())
				.setTexturingState(access.getTexturingState())
				.setWriteMaskState(access.getWriteMaskState())
				.setLineState(access.getLineState());
		op.accept(builder);
		return builder.createCompositeState(access.getOutlineProperty());
	}
}
