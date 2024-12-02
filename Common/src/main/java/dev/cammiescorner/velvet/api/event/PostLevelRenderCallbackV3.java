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
import org.joml.Matrix4f;

@FunctionalInterface
public interface PostLevelRenderCallbackV3 {
	/**
	 * Fired after Minecraft has rendered everything in the world, before it renders hands, HUDs and GUIs.
	 */
	Event<PostLevelRenderCallbackV3> EVENT = EventFactoryImpl.create(PostLevelRenderCallbackV3.class,
			(listeners) -> (matrices, projectionMat, modelViewMath, camera, tickDelta) -> {
				PostLevelRenderCallbackV2.EVENT.invoker().onLevelRendered(matrices, camera, tickDelta);

				for(PostLevelRenderCallbackV3 handler : listeners)
					handler.onLevelRendered(matrices, projectionMat, modelViewMath, camera, tickDelta);
			});

	/**
	 * @param posingStack   a blank {@link PoseStack} that can be used for rendering custom elements
	 * @param modelViewMat  the model-view matrix corresponding to the camera's perspective
	 * @param projectionMat the base projection matrix for world rendering
	 * @param camera        the camera from which perspective the world is being rendered
	 * @param tickDelta     fraction of time between two consecutive ticks (before 0 and 1)
	 */
	void onLevelRendered(PoseStack posingStack, Matrix4f modelViewMat, Matrix4f projectionMat, Camera camera, float tickDelta);
}
