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

import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import org.apache.logging.log4j.Logger;
import org.ladysnake.satintestcore.common.debugbehavior.DebugBehavior;
import org.ladysnake.satintestcore.event.EndClientTickEvent;

public class SatinDepthTest implements ClientEntryPoint {
    public static final String MOD_ID = "velvetdepthtest";
	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	@Override
	public void onInitializeClient(ModContainer mod) {
		LOGGER.info("Loading SatinDepthTest");
		EndClientTickEvent.EVENT.register(DepthFx.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(DepthFx.INSTANCE);
		PostLevelRenderCallbackV2.EVENT.register(DepthFx.INSTANCE);
	}

	public static class DepthTestDebugBehavior extends DebugBehavior.Client {

		@Override
		public void clientAction(ClientLevel level, AbstractClientPlayer player, InteractionHand hand) {
			DepthFx.INSTANCE.testShader.release();
		}
	}
}
