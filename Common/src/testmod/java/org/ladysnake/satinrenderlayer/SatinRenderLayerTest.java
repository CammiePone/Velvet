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
package org.ladysnake.satinrenderlayer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.velvet.api.event.EntitiesPreRenderCallback;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.cammiescorner.velvet.api.managed.ManagedCoreShader;
import dev.cammiescorner.velvet.api.managed.ManagedRenderTarget;
import dev.cammiescorner.velvet.api.managed.ManagedShaderEffect;
import dev.cammiescorner.velvet.api.managed.ShaderEffectManager;
import dev.cammiescorner.velvet.api.managed.uniform.Uniform1f;
import dev.cammiescorner.velvet.api.util.RenderTypeHelper;
import dev.upcraft.sparkweave.api.client.event.RegisterEntityRenderersEvent;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.ladysnake.satintestcore.event.EndClientTickEvent;
import org.ladysnake.satintestcore.init.SatinTestEntities;

public final class SatinRenderLayerTest implements ClientEntryPoint {
	public static final String MOD_ID = "velvetrenderlayer";
	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();
    /* * * * ManagedShaderEffect-based RenderLayer entity rendering * * * */
    public static final ManagedShaderEffect illusionEffect = ShaderEffectManager.getInstance().manage(ResourceLocation.fromNamespaceAndPath(MOD_ID, "shaders/post/illusion.json"),
            effect -> effect.setUniformValue("ColorModulate", 1.2f, 0.7f, 0.2f, 1.0f));
    public static final ManagedRenderTarget illusionBuffer = illusionEffect.getTarget("final");

    /* * * * ManagedShaderProgram-based RenderLayer entity rendering * * * */
    public static final ManagedCoreShader rainbow = ShaderEffectManager.getInstance().manageCoreShader(ResourceLocation.fromNamespaceAndPath(MOD_ID, "rainbow"));
    private static final Uniform1f uniformSTime = rainbow.findUniform1f("STime");

    private static int ticks;

	@Override
	public void onInitializeClient(ModContainer mod) {
		LOGGER.info("Loading SatinRenderLayerTest");

		RenderType blockRenderType = illusionBuffer.getRenderType(RenderType.translucent());
		RenderTypeHelper.registerBlockRenderType(blockRenderType);

		// TODO move to sparkweave event
//		BlockRenderLayerMap.INSTANCE.putBlock(SatinTestBlocks.DEBUG_BLOCK.get(), blockRenderType);

		RegisterEntityRenderersEvent.EVENT.register(event -> {
			event.registerRenderer(SatinTestEntities.ILLUSION_GOLEM, IllusionGolemEntityRenderer::new);
			event.registerRenderer(SatinTestEntities.RAINBOW_WITHER, RainbowWitherEntityRenderer::new);
		});

		EndClientTickEvent.EVENT.register(client -> ticks++);
		EntitiesPreRenderCallback.EVENT.register((camera, frustum, tickDelta) -> uniformSTime.set((ticks + tickDelta) * 0.05f));
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
				Minecraft client = Minecraft.getInstance();
				illusionEffect.render(tickDelta);
				client.getMainRenderTarget().bindWrite(true);
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
				illusionBuffer.draw(client.getWindow().getWidth(), client.getWindow().getHeight(), false);
				illusionBuffer.clear();
				client.getMainRenderTarget().bindWrite(true);
				RenderSystem.disableBlend();
			}
		);
	}
}
