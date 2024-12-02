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
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class RenderTypeDuplicator {

	public static RenderType copy(RenderType existing, String newName, Consumer<RenderType.CompositeState.CompositeStateBuilder> op) {
		return copy(existing, newName, null, op);
	}

	/**
	 * @param existing     the {@link RenderType} to copy
	 * @param newName      a unique name for the new {@link RenderType}
	 * @param vertexFormat the new vertex format, or {@code null} if {@code existing}'s format should be used
	 * @param op           the transformation to apply to {@code existing}'s parameters
	 * @return a new {@link RenderType} based on {@code existing}
	 */
	public static RenderType copy(RenderType existing, String newName, @Nullable VertexFormat vertexFormat, Consumer<RenderType.CompositeState.CompositeStateBuilder> op) {
		checkDefaultImpl(existing);
		return ((VelvetRenderType) existing).copy(newName, vertexFormat, op);
	}

	public static RenderType.CompositeState copyPhaseParameters(RenderType existing, Consumer<RenderType.CompositeState.CompositeStateBuilder> op) {
		checkDefaultImpl(existing);
		return ((VelvetRenderType) existing).copyPhaseParameters(op);
	}

	private static void checkDefaultImpl(RenderType existing) {
		if(!(existing instanceof VelvetRenderType)) {
			throw new IllegalArgumentException("Unrecognized RenderType implementation " + existing.getClass() + ". Type duplication is only applicable to the default (MultiPhase) implementation");
		}
	}

	public interface VelvetRenderType {
		RenderType copy(String newName, @Nullable VertexFormat vertexFormat, Consumer<RenderType.CompositeState.CompositeStateBuilder> op);

		RenderType.CompositeState copyPhaseParameters(Consumer<RenderType.CompositeState.CompositeStateBuilder> op);
	}
}
