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

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.cammiescorner.velvet.Velvet;
import dev.cammiescorner.velvet.api.managed.ManagedRenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;

public final class RenderTargetWrapper implements ManagedRenderTarget {
	private final RenderLayerSupplier renderLayerSupplier;
	private final String name;
	@Nullable
	private RenderTarget wrapped;

	RenderTargetWrapper(String name) {
		this.name = name;
		this.renderLayerSupplier = RenderLayerSupplier.renderTarget(
				this.name + System.identityHashCode(this),
				() -> this.beginWrite(false),
				() -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false)
		);
	}

	void findTarget(@Nullable PostChain shaderEffect) {
		if(shaderEffect == null) {
			this.wrapped = null;
		}
		else {
			this.wrapped = shaderEffect.getTempTarget(this.name);

			if(this.wrapped == null) {
				Velvet.LOGGER.warn("No target framebuffer found with name {} in shader {}", this.name, shaderEffect.getName());
			}
		}
	}

	public String getName() {
		return name;
	}

	@Nullable
	public RenderTarget getRenderTarget() {
		return wrapped;
	}

	@Override
	public void copyDepthFrom(RenderTarget target) {
		if(this.wrapped != null) {
			this.wrapped.copyDepthFrom(target);
		}
	}

	@Override
	public void beginWrite(boolean updateViewport) {
		if(this.wrapped != null) {
			this.wrapped.bindWrite(updateViewport);
		}
	}

	@Override
	public void draw(boolean disableBlend) {
		Window window = Minecraft.getInstance().getWindow();
		this.draw(window.getWidth(), window.getHeight(), disableBlend);
	}

	@Override
	public void draw(int width, int height, boolean disableBlend) {
		if(this.wrapped != null) {
			this.wrapped.blitToScreen(width, height, disableBlend);
		}
	}

	@Override
	public void clear() {
		clear(Minecraft.ON_OSX);
	}

	@Override
	public void clear(boolean swallowErrors) {
		if(this.wrapped != null) {
			this.wrapped.clear(swallowErrors);
		}
	}

	@Override
	public RenderType getRenderType(RenderType base) {
		return this.renderLayerSupplier.getRenderLayer(base);
	}
}
