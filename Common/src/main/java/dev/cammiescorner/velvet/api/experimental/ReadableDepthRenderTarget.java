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
package dev.cammiescorner.velvet.api.experimental;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.cammiescorner.velvet.api.event.PostLevelRenderCallback;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.apiguardian.api.API;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

/**
 * Implemented by every {@link RenderTarget} when the feature is enabled in the config.
 * <p>
 * This allows access to a depth texture that the  render target writes to instead of the usual render buffer.
 */
@API(status = EXPERIMENTAL)
public interface ReadableDepthRenderTarget {

	/**
	 * Returns a still of this  render target's depth texture.
	 * For most intents and purposes, this is the texture that API consumers should read from.
	 *
	 * <p>This texture is updated only when {@link #freezeDepthMap()} is called. For the main  render target,
	 * it is updated automatically right before {@link PostLevelRenderCallback#EVENT} is fired.
	 * <strong>Note that the content of the main  render target's depth texture depends on the graphic quality setting.</strong>
	 * With {@link GraphicsStatus#FABULOUS}, no translucent object will appear on the main depth texture, so consumers may
	 * need to also use the depth textures from the various layers used by the {@link LevelRenderer}'s transparency shader.
	 *
	 * <p>Because this texture is independent from the  render target's actual depth texture,
	 * it is safe to use anytime (whereas using the actual depth texture may cause corruption if it is simultaneously written to).
	 *
	 * <p>The returned value may be -1 if the  render target does not have a {@linkplain RenderTarget#getDepthTextureId() depth attachment}.
	 * This should never be the case for the main  render target.
	 *
	 * @return a still of this  render target's depth texture
	 */
	@API(status = EXPERIMENTAL)
	int getStillDepthMap();

	/**
	 * Freezes the {@linkplain RenderTarget#getDepthTextureId() current depth texture} for use in
	 * {@link #getStillDepthMap()}.
	 * <p>
	 * This is called by default on the {@link Minecraft#getMainRenderTarget()} main  render target
	 * once every frame, right before {@link PostLevelRenderCallback} is fired.
	 * <p>
	 * Calling this method will bind {@code this}  render target.
	 */
	@API(status = EXPERIMENTAL)
	void freezeDepthMap();

	static ReadableDepthRenderTarget getFrom(RenderTarget target) {
		return (ReadableDepthRenderTarget) target;
	}

	static int getStillDepthMap(RenderTarget target) {
		return getFrom(target).getStillDepthMap();
	}

	static void freezeDepthMap(RenderTarget target) {
		getFrom(target).freezeDepthMap();
	}
}
