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
package org.ladysnake.satincustomformattest;

import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.cammiescorner.velvet.api.managed.ManagedShaderEffect;
import dev.cammiescorner.velvet.api.managed.ShaderEffectManager;
import dev.cammiescorner.velvet.api.managed.uniform.Uniform4f;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.satintestcore.common.item.SatinTestItems;

public final class SatinCustomFormatTest implements ClientEntryPoint {
    public static final String MOD_ID = "velvetcustomformattest";

    private boolean renderingBlit = false;
    // this shader has the same overall effect as Minecraft's blit,
    // but blits the texture around between various framebuffer formats
    private final ManagedShaderEffect testShader = ShaderEffectManager.getInstance().manage(ResourceLocation.fromNamespaceAndPath(MOD_ID, "shaders/post/blit.json"));
    // note that this is applied ~4x, so may be more intense than expected.
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
				color.set((float) Math.random() * 0.5f + 0.75f, (float) Math.random() * 0.5f + 0.75f, (float) Math.random() * 0.5f + 0.75f, 1.0f);
			}
		});
	}
}

