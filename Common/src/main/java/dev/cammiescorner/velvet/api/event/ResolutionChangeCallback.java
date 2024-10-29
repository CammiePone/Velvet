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

import dev.upcraft.sparkweave.api.event.Event;
import dev.upcraft.sparkweave.event.EventFactoryImpl;

@FunctionalInterface
public interface ResolutionChangeCallback {
	/**
	 * Fired each time Minecraft's window resolution changes
	 */
	Event<ResolutionChangeCallback> EVENT = EventFactoryImpl.create(ResolutionChangeCallback.class,
			(listeners) -> (newWidth, newHeight) -> {
				for(ResolutionChangeCallback event : listeners) {
					event.onResolutionChanged(newWidth, newHeight);
				}
			});

	void onResolutionChanged(int newWidth, int newHeight);
}
