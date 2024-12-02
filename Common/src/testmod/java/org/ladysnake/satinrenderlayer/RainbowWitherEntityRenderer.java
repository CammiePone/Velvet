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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitherBossRenderer;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.jetbrains.annotations.Nullable;

public class RainbowWitherEntityRenderer extends WitherBossRenderer {
    public RainbowWitherEntityRenderer(EntityRendererProvider.Context entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

	@Override
	public void render(WitherBoss entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

    @Nullable
    @Override
    protected RenderType getRenderType(WitherBoss entity, boolean showBody, boolean translucent, boolean glowing) {
        RenderType baseLayer = super.getRenderType(entity, showBody, translucent, glowing);
        return baseLayer == null ? null : SatinRenderLayerTest.rainbow.getRenderType(baseLayer);
    }
}
