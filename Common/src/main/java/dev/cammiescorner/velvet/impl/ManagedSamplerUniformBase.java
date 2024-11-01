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
package dev.cammiescorner.velvet.impl;

import com.mojang.blaze3d.shaders.Uniform;
import dev.cammiescorner.velvet.api.managed.uniform.SamplerUniform;
import dev.cammiescorner.velvet.api.util.SamplerAccess;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

/**
 * Mojank working in divergent branches for half a century, {@link ShaderInstance}
 * is a copy of an old implementation of {@link EffectInstance}.
 * The latter has since been updated to have the {@link EffectInstance#setSampler(String, IntSupplier)}
 * while the former still uses {@link ShaderInstance#setSampler(String, Object)}.
 *
 * <p>So we need to deal with both those extremely similar implementations
 */
public abstract class ManagedSamplerUniformBase extends ManagedUniformBase implements SamplerUniform {
	protected SamplerAccess[] targets = new SamplerAccess[0];
	protected int[] locations = new int[0];
	protected Object cachedValue;

	public ManagedSamplerUniformBase(String name) {
		super(name);
	}

	@Override
	public boolean findUniformTargets(List<PostPass> shaders) {
		List<SamplerAccess> targets = new ArrayList<>(shaders.size());
		IntList rawTargets = new IntArrayList(shaders.size());
		for(PostPass shader : shaders) {
			EffectInstance program = shader.getEffect();
			if(program.velvet$hasSampler(this.name)) {
				targets.add(program);
				rawTargets.add(getSamplerLoc(program));
			}
		}
		this.targets = targets.toArray(SamplerAccess[]::new);
		this.locations = rawTargets.toIntArray();
		this.syncCurrentValues();
		return this.targets.length > 0;
	}

	private int getSamplerLoc(SamplerAccess access) {
		return access.velvet$getSamplerShaderLocs().get(access.velvet$getSamplerNames().indexOf(this.name));
	}

	@Override
	public boolean findUniformTarget(ShaderInstance shader) {
		if(shader.velvet$hasSampler(this.name)) {
			this.targets = new SamplerAccess[]{shader};
			this.locations = new int[]{getSamplerLoc(shader)};
			this.syncCurrentValues();
			return true;
		}
		return false;
	}

	private void syncCurrentValues() {
		Object value = this.cachedValue;
		if(value != null) { // after the first upload
			this.cachedValue = null;
			this.set(value);
		}
	}

	protected abstract void set(Object value);

	@Override
	public void setDirect(int activeTexture) {
		int length = this.locations.length;
		for(int i = 0; i < length; i++) {
			this.targets[i].velvet$removeSampler(this.name);
			Uniform.uploadInteger(this.locations[i], activeTexture);
		}
	}
}
