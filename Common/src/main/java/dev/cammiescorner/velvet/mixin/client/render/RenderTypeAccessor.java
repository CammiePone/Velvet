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
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccessor {
	@Accessor
	boolean getSortOnUpload();

	@Invoker("create")
	static RenderType.CompositeRenderType create(@SuppressWarnings("unused") String name, @SuppressWarnings("unused") VertexFormat vertexFormat, @SuppressWarnings("unused") VertexFormat.Mode drawMode, @SuppressWarnings("unused") int expectedBufferSize, @SuppressWarnings("unused") boolean hasCrumbling, @SuppressWarnings("unused") boolean translucent, @SuppressWarnings("unused") RenderType.CompositeState phases) {
		throw new IllegalStateException("Mixin not transformed");
	}
}
