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

import dev.cammiescorner.velvet.Velvet;
import dev.cammiescorner.velvet.api.managed.uniform.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.apiguardian.api.API;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static org.apiguardian.api.API.Status.INTERNAL;

public abstract class ResettableManagedShaderBase<S extends AutoCloseable> implements UniformFinder {
	/**
	 * Location of the shader json definition file
	 */
	private final ResourceLocation location;
	private final Map<String, ManagedUniform> managedUniforms = new HashMap<>();
	private final List<ManagedUniformBase> allUniforms = new ArrayList<>();
	private boolean errored;
	protected S shader;

	public ResettableManagedShaderBase(ResourceLocation location) {
		this.location = location;
	}

	@API(status = INTERNAL)
	public void initializeOrLog(ResourceProvider mgr) {
		try {
			this.initialize(mgr);
		}
		catch(IOException e) {
			this.errored = true;
			this.logInitError(e);
		}
	}

	protected abstract void logInitError(IOException e);

	protected void initialize(ResourceProvider resourceManager) throws IOException {
		this.release();
		Minecraft mc = Minecraft.getInstance();
		this.shader = parseShader(resourceManager, mc, this.location);
		this.setup(mc.getWindow().getWidth(), mc.getWindow().getHeight());
	}

	protected abstract S parseShader(ResourceProvider resourceFactory, Minecraft mc, ResourceLocation location) throws IOException;

	public void release() {
		if(this.isInitialized()) {
			try {
				assert this.shader != null;
				this.shader.close();
				this.shader = null;
			}
			catch(Exception e) {
				throw new RuntimeException("Failed to release shader " + this.location, e);
			}
		}
		this.errored = false;
	}

	protected Collection<ManagedUniformBase> getManagedUniforms() {
		return this.allUniforms;
	}

	protected abstract boolean setupUniform(ManagedUniformBase uniform, S shader);

	public boolean isInitialized() {
		return this.shader != null;
	}

	public boolean isErrored() {
		return this.errored;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	protected <U extends ManagedUniformBase> U manageUniform(Map<String, U> uniformMap, Function<String, U> factory, String uniformName, String uniformKind) {
		U existing = uniformMap.get(uniformName);
		if(existing != null) {
			return existing;
		}
		U ret = factory.apply(uniformName);
		if(this.shader != null) {
			boolean found = setupUniform(ret, shader);
			if(!found) {
				Velvet.LOGGER.warn("No {} found with name {} in shader {}", uniformKind, uniformName, this.location);
			}
		}
		uniformMap.put(uniformName, ret);
		allUniforms.add(ret);
		return ret;
	}

	@Override
	public Uniform1i findUniform1i(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 1), uniformName, "uniform");
	}

	@Override
	public Uniform2i findUniform2i(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 2), uniformName, "uniform");
	}

	@Override
	public Uniform3i findUniform3i(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 3), uniformName, "uniform");
	}

	@Override
	public Uniform4i findUniform4i(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 4), uniformName, "uniform");
	}

	@Override
	public Uniform1f findUniform1f(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 1), uniformName, "uniform");
	}

	@Override
	public Uniform2f findUniform2f(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 2), uniformName, "uniform");
	}

	@Override
	public Uniform3f findUniform3f(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 3), uniformName, "uniform");
	}

	@Override
	public Uniform4f findUniform4f(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 4), uniformName, "uniform");
	}

	@Override
	public UniformMat4 findUniformMat4(String uniformName) {
		return manageUniform(this.managedUniforms, name -> new ManagedUniform(name, 16), uniformName, "uniform");
	}

	@API(status = INTERNAL)
	public abstract void setup(int newWidth, int newHeight);

	@Override
	public String toString() {
		return "%s[%s]".formatted(this.getClass().getSimpleName(), this.location);
	}
}
