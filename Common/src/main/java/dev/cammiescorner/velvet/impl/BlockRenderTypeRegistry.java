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

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.renderer.RenderType;

import java.util.Set;

public final class BlockRenderTypeRegistry {
	public static final BlockRenderTypeRegistry INSTANCE = new BlockRenderTypeRegistry();
	private final Set<RenderType> renderTypes = new ObjectArraySet<>();   // ArraySet for faster iteration
	private volatile boolean registryLocked = false;

	private BlockRenderTypeRegistry() {
	}

	public void registerRenderType(RenderType type) {
		if(registryLocked) {
			throw new IllegalStateException(String.format(
					"RenderType %s was added too late.",
					type
			));
		}

		renderTypes.add(type);
	}

	public Set<RenderType> getTypes() {
		registryLocked = true;
		return renderTypes;
	}
}
