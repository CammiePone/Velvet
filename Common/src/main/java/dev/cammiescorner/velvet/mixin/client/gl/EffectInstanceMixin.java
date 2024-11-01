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
package dev.cammiescorner.velvet.mixin.client.gl;

import dev.cammiescorner.velvet.api.util.SamplerAccess;
import net.minecraft.client.renderer.EffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin implements SamplerAccess {
	@Shadow @Final private Map<String, IntSupplier> samplerMap;

	@Override
	public void velvet$removeSampler(String name) {
		this.samplerMap.remove(name);
	}

	@Override
	public boolean velvet$hasSampler(String name) {
		return this.samplerMap.containsKey(name);
	}

	@Override
	@Accessor("samplerNames")
	public abstract List<String> velvet$getSamplerNames();

	@Override
	@Accessor("samplerLocations")
	public abstract List<Integer> velvet$getSamplerShaderLocs();
}
