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
package dev.cammiescorner.velvet.api.managed;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.velvet.impl.ReloadableShaderEffectManager;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.apiguardian.api.API;

import java.util.function.Consumer;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.apiguardian.api.API.Status.STABLE;

/**
 * @see ManagedShaderEffect
 */
public interface ShaderEffectManager {
	@API(status = STABLE)
	static ShaderEffectManager getInstance() {
		return ReloadableShaderEffectManager.INSTANCE;
	}

	/**
	 * Manages a post-process {@link PostChain} loaded from a json definition file
	 *
	 * @param location the location of the json within your mod's assets
	 * @return a screen shader that will be automatically reloaded as needed
	 */
	@API(status = STABLE, since = "1.0.0")
	ManagedShaderEffect manage(ResourceLocation location);

	/**
	 * Manages a post-process {@link PostChain} loaded from a json definition file
	 *
	 * @param location     the location of the json within your mod's assets
	 * @param initCallback a block ran once the shader effect is initialized
	 * @return a screen shader that will be automatically reloaded as needed
	 */
	@API(status = STABLE, since = "1.0.0")
	ManagedShaderEffect manage(ResourceLocation location, Consumer<ManagedShaderEffect> initCallback);

	/**
	 * Manages a core {@link ShaderInstance} loaded from a json definition file
	 *
	 * <p>This overload forwards to {@link #manageCoreShader(ResourceLocation, VertexFormat)} with {@link DefaultVertexFormat#NEW_ENTITY},
	 * the format used by entity rendering
	 *
	 * <p>The shader location must be in {@code assets/shaders/core}.
	 * Eg. to get a shader located at {@code mymod/assets/shaders/core/myshader.json}:
	 * <pre>{@code ShaderEffectManager.getInstance().manageCoreShader(Identifier.of("mymod", "myshader")}</pre>
	 *
	 * @param location the location of the json within your mod's assets
	 * @return a core render shader that will be reloaded as needed
	 */
	@API(status = EXPERIMENTAL, since = "1.6.0")
	ManagedCoreShader manageCoreShader(ResourceLocation location);

	/**
	 * Manages a core {@link ShaderInstance} loaded from a json definition file
	 *
	 * <p>The shader location must be in {@code assets/shaders/core}.
	 * Eg. to get a shader located at {@code mymod/assets/shaders/core/myshader.json}:
	 * <pre>{@code ShaderEffectManager.getInstance().manageCoreShader(Identifier.of("mymod", "myshader")}</pre>
	 *
	 * @param location     the location of the json within your mod's assets
	 * @param vertexFormat the format expected by your shader
	 * @return a core render shader that will be reloaded as needed
	 */
	@API(status = EXPERIMENTAL, since = "1.6.0")
	ManagedCoreShader manageCoreShader(ResourceLocation location, VertexFormat vertexFormat);

	/**
	 * Manages a core {@link ShaderInstance} loaded from a json definition file
	 *
	 * <p>The shader location must be in {@code assets/shaders/core}.
	 * E.g. to get a shader located at {@code mymod/assets/shaders/core/myshader.json}:
	 * <pre>{@code ShaderEffectManager.getInstance().manageCoreShader(Identifier.of("mymod", "myshader")}</pre>
	 *
	 * @param location     the location of the json within your mod's assets
	 * @param vertexFormat the format expected by your shader
	 * @param initCallback a block ran once the shader effect is initialized
	 * @return a core render shader that will be reloaded as needed
	 */
	@API(status = EXPERIMENTAL, since = "1.6.0")
	ManagedCoreShader manageCoreShader(ResourceLocation location, VertexFormat vertexFormat, Consumer<ManagedCoreShader> initCallback);

	/**
	 * Removes a shader from the global list of managed shaders,
	 * making it not respond to resource reloading and screen resizing.
	 * This also calls {@link ManagedShaderEffect#release()} to release the shader's resources.
	 * A <code>ManagedShaderEffect</code> object cannot be used after it has been disposed of.
	 *
	 * @param shader the shader to stop managing
	 * @see ManagedShaderEffect#release()
	 */
	@API(status = STABLE, since = "1.0.0")
	void dispose(ManagedShaderEffect shader);

	@API(status = EXPERIMENTAL, since = "1.4.0")
	void dispose(ManagedCoreShader shader);
}
