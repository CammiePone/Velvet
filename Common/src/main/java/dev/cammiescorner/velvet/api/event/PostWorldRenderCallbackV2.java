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
package dev.cammiescorner.velvet.api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.event.Event;
import dev.upcraft.sparkweave.event.EventFactoryImpl;
import net.minecraft.client.Camera;
import net.minecraft.client.GraphicsStatus;

@FunctionalInterface
public interface PostWorldRenderCallbackV2 {
	/**
	 * Fired after Minecraft has rendered everything in the world, before it renders hands, HUDs and GUIs.
	 *
	 * <p>{@link net.minecraft.client.renderer.PostChain}s <strong>must not</strong> be rendered in this callback, as they will prevent
	 * {@link GraphicsStatus#FABULOUS fabulous graphics} and other effects from working properly.
	 */
	Event<PostWorldRenderCallbackV2> EVENT = EventFactoryImpl.create(PostWorldRenderCallbackV2.class,
			(listeners) -> (posingStack, camera, tickDelta) -> {
				PostWorldRenderCallback.EVENT.invoker().onWorldRendered(camera, tickDelta);
				for(PostWorldRenderCallbackV2 handler : listeners) {
					handler.onWorldRendered(posingStack, camera, tickDelta);
				}
			});

	/**
	 * @param posingStack a blank {@link PoseStack} that can be used for rendering custom elements
	 * @param camera      the camera from which perspective the world is being rendered
	 * @param tickDelta   fraction of time between two consecutive ticks (before 0 and 1)
	 */
	void onWorldRendered(PoseStack posingStack, Camera camera, float tickDelta);
}
