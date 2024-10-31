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

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.world.entity.animal.IronGolem;
import org.jetbrains.annotations.Nullable;

public class IllusionGolemEntityRenderer extends IronGolemRenderer {
    public IllusionGolemEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(IronGolem entity, boolean showBody, boolean translucent, boolean glowing) {
        RenderType baseLayer = super.getRenderType(entity, showBody, translucent, glowing);
        return baseLayer == null ? null : SatinRenderLayerTest.illusionBuffer.getRenderLayer(baseLayer);
    }
}
