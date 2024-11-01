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
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.logging.log4j.Logger;
import org.ladysnake.satintestcore.common.debugbehavior.DebugBehavior;

import java.util.List;

public final class SatinBasicTest implements ClientEntryPoint {
    public static final String MOD_ID = "velvetbasictest";
	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	private static boolean renderingBlit = false;
	private static int colorIntValue;
	// literally the same as minecraft's blit, we are just checking that custom paths work
	private static final ManagedShaderEffect testShader = ShaderEffectManager.getInstance().manage(ResourceLocation.fromNamespaceAndPath(MOD_ID, "shaders/post/blit.json"), (effect) -> {
		LOGGER.info("Test shader got updated");
	});
	private static final Uniform4f color = testShader.findUniform4f("ColorModulate");

	public static class BasicTestDebugBehavior extends DebugBehavior.Client {

		@Override
		public void clientAction(ClientLevel level, AbstractClientPlayer player, InteractionHand hand) {
			renderingBlit = !renderingBlit;
			float r = (float) Math.random();
			float g = (float) Math.random();
			float b = (float) Math.random();
			color.set(r, g, b, 1.0f);
			colorIntValue = Mth.color(r, g, b);
		}

		@Override
		public void appendHoverTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
			if(renderingBlit) {
				tooltipComponents.add(Component.translatable("tooltip.velvettestcore.debug_behavior.generic.enabled"));
				tooltipComponents.add(Component.translatable("tooltip.velvettestcore.debug_behavior.generic.color", "#%X".formatted(colorIntValue)));
			}
			else {
				tooltipComponents.add(Component.translatable("tooltip.velvettestcore.debug_behavior.generic.disabled"));
			}
		}
	}

	@Override
	public void onInitializeClient(ModContainer mod) {
		LOGGER.info("Loading SatinBasicTest");
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			if (renderingBlit) {
				testShader.render(tickDelta);
			}
		});
	}
}
