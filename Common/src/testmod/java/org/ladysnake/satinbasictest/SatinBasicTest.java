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
package org.ladysnake.satinbasictest;

import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.cammiescorner.velvet.api.managed.ManagedShaderEffect;
import dev.cammiescorner.velvet.api.managed.ShaderEffectManager;
import dev.cammiescorner.velvet.api.managed.uniform.Uniform4f;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ladysnake.satintestcore.common.item.SatinTestItems;

public final class SatinBasicTest implements ClientEntryPoint {
    public static final String MOD_ID = "velvetbasictest";
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private boolean renderingBlit = false;
	// literally the same as minecraft's blit, we are just checking that custom paths work
	private final ManagedShaderEffect testShader = ShaderEffectManager.getInstance().manage(ResourceLocation.fromNamespaceAndPath(MOD_ID, "shaders/post/blit.json"), (effect) -> {
		LOGGER.info("Test shader got updated");
	});
	private final Uniform4f color = testShader.findUniform4f("ColorModulate");

	@Override
	public void onInitializeClient(ModContainer mod) {
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			if (renderingBlit) {
				testShader.render(tickDelta);
			}
		});
		SatinTestItems.DEBUG_ITEM.registerDebugMode(MOD_ID, (world, player, hand) -> {
			if (world.isClientSide()) {
				renderingBlit = !renderingBlit;
				color.set((float) Math.random(), (float) Math.random(), (float) Math.random(), 1.0f);
			}
		});
	}
}