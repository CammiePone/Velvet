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
package org.ladysnake.satintestcore;

import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.ladysnake.satintestcore.init.*;

public class SatinTestCore implements MainEntryPoint {
    public static final String MOD_ID = "velvettestcore";
	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Loading SatinTestCore");
		RegistryService registryService = RegistryService.get();
		SatinTestDebugBehaviors.DEBUG_BEHAVIORS.accept(registryService);
		SatinTestDataComponents.DATA_COMPONENTS.accept(registryService);
		SatinTestBlocks.BLOCKS.accept(registryService);
		SatinTestItems.ITEMS.accept(registryService);
		SatinTestCreativeTabs.CREATIVE_TABS.accept(registryService);
		SatinTestEntities.ENTITY_TYPES.accept(registryService);
	}
}
