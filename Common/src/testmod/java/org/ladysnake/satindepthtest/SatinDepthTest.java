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
package org.ladysnake.satindepthtest;

import dev.cammiescorner.velvet.api.event.PostWorldRenderCallbackV2;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import org.ladysnake.satintestcore.event.EndClientTickEvent;
import org.ladysnake.satintestcore.common.item.SatinTestItems;

public class SatinDepthTest implements ClientEntryPoint {
    public static final String MOD_ID = "velvetdepthtest";

	@Override
	public void onInitializeClient(ModContainer mod) {
		EndClientTickEvent.EVENT.register(DepthFx.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(DepthFx.INSTANCE);
		PostWorldRenderCallbackV2.EVENT.register(DepthFx.INSTANCE);
		SatinTestItems.DEBUG_ITEM.registerDebugMode(MOD_ID, (world, player, hand) -> {
			if(world.isClientSide()) {
				DepthFx.INSTANCE.testShader.release();
			}
		});
	}
}
