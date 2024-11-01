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
package dev.cammiescorner.velvet.api.managed;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.cammiescorner.velvet.api.util.RenderTypeHelper;
import net.minecraft.client.renderer.RenderType;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@API(status = API.Status.EXPERIMENTAL, since = "1.4.0")
public interface ManagedRenderTarget {
	@Nullable
	RenderTarget getRenderTarget();

	/**
	 * Begins a write operation on this render target.
	 *
	 * <p>If the operation is successful, every subsequent draw call will write to this render target.
	 *
	 * @param updateViewport whether binding this render target should call {@link com.mojang.blaze3d.systems.RenderSystem#viewport(int, int, int, int)}
	 */
	void beginWrite(boolean updateViewport);

	/**
	 * Copies the depth texture from another render target to this render target.
	 *
	 * @param target the render target to copy depth from
	 */
	void copyDepthFrom(RenderTarget target);

	/**
	 * Draws this render target, scaling to the default render target's
	 * {@linkplain Window#getWidth() width} and {@linkplain Window#getWidth() height}.
	 */
	void draw();

	void draw(int width, int height, boolean disableBlend);

	/**
	 * Clears the content of this render target.
	 */
	void clear();

	void clear(boolean swallowErrors);

	/**
	 * Gets a simple {@link RenderType} that is functionally identical to {@code base},
	 * but with a different {@link net.minecraft.client.renderer.RenderStateShard.OutputStateShard} that binds this render target.
	 *
	 * @param base the layer to copy
	 * @return a render layer using this render target
	 * @see RenderTypeHelper#copy(RenderType, String, Consumer)
	 */
	RenderType getRenderType(RenderType base);
}
