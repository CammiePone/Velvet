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

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.velvet.Velvet;
import dev.cammiescorner.velvet.api.managed.ManagedCoreShader;
import dev.cammiescorner.velvet.api.managed.uniform.SamplerUniform;
import dev.cammiescorner.velvet.api.util.VelvetShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ResettableManagedCoreShader extends ResettableManagedShaderBase<ShaderInstance> implements ManagedCoreShader {
	/**
	 * Callback to run once each time the shader effect is initialized
	 */
	private final Consumer<ManagedCoreShader> initCallback;
	private final RenderTypeSupplier renderTypeSupplier;
	private final VertexFormat vertexFormat;
	private final Map<String, ManagedSamplerUniformV1> managedSamplers = new HashMap<>();

	public ResettableManagedCoreShader(ResourceLocation location, VertexFormat vertexFormat, Consumer<ManagedCoreShader> initCallback) {
		super(location);
		this.vertexFormat = vertexFormat;
		this.initCallback = initCallback;
		this.renderTypeSupplier = RenderTypeSupplier.shader(
				String.format("%s_%d", location, System.identityHashCode(this)),
				vertexFormat,
				this::getProgram);
	}

	@Override
	protected ShaderInstance parseShader(ResourceProvider resourceManager, Minecraft mc, ResourceLocation location) throws IOException {
		return new VelvetShaderInstance(resourceManager, this.getLocation(), this.vertexFormat);
	}

	@Override
	public void setup(int newWidth, int newHeight) {
		Preconditions.checkNotNull(this.shader);
		for(ManagedUniformBase uniform : this.getManagedUniforms()) {
			setupUniform(uniform, this.shader);
		}
		this.initCallback.accept(this);
	}

	@Override
	public ShaderInstance getProgram() {
		return this.shader;
	}

	@Override
	public RenderType getRenderType(RenderType type) {
		return this.renderTypeSupplier.getRenderType(type);
	}

	@Override
	protected boolean setupUniform(ManagedUniformBase uniform, ShaderInstance shader) {
		return uniform.findUniformTarget(shader);
	}

	@Override
	public SamplerUniform findSampler(String samplerName) {
		return manageUniform(this.managedSamplers, ManagedSamplerUniformV1::new, samplerName, "sampler");
	}

	@Override
	protected void logInitError(IOException e) {
		Velvet.LOGGER.error("Could not create shader program {}", this.getLocation(), e);
	}
}
