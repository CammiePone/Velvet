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

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.cammiescorner.velvet.api.managed.uniform.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.function.Consumer;

import static org.apiguardian.api.API.Status.*;

/**
 * A post processing shader that is applied to the main render target
 * <p>
 * Post shaders loaded through {@link ShaderEffectManager#manage(ResourceLocation, Consumer)} are self-managed and will be
 * reloaded when shader assets are reloaded (through <kbd>F3-T</kbd> or <kbd>/ladylib_shader_reload</kbd>) or the
 * screen resolution changes.
 * <p>
 * Examples of json-defined shader effects are available in <kbd>assets/minecraft/shaders</kbd>.
 *
 * @see ShaderEffectManager
 * @since 1.0.0
 */
public interface ManagedShaderEffect extends UniformFinder {
	/**
	 * Returns this object's managed {@link PostChain}, creating and initializing it if it doesn't exist.
	 * <p>
	 * This method will return <code>null</code> if an error occurs during initialization.
	 * <p>
	 * <em>Note: calling this before the graphic context is ready will cause issues.</em>
	 *
	 * @return the {@link PostChain} managed by this object
	 * @see #initialize()
	 * @see #isInitialized()
	 */
	@Nullable
	@API(status = MAINTAINED, since = "1.0.0")
	PostChain getShaderEffect();

	/**
	 * Initializes this shader, allocating required system resources
	 * such as render target objects, shaders objects and texture objects.
	 * Any exception thrown during initialization is relayed to the caller.
	 * <p>
	 * If the shader is already initialized, previously allocated
	 * resources will be disposed of before initializing new ones.
	 *
	 * @apiNote Calling this method directly is not required in most cases.
	 * @see #getShaderEffect()
	 * @see #isInitialized()
	 * @see #release()
	 */
	@API(status = MAINTAINED, since = "1.0.0")
	void initialize() throws IOException;

	/**
	 * Checks whether this shader is initialized. If it is not, next call to {@link #getShaderEffect()}
	 * will setup the shader group.
	 *
	 * @return true if this does not require initialization
	 * @see #initialize()
	 */
	@API(status = MAINTAINED, since = "1.0.0")
	boolean isInitialized();

	/**
	 * @return <code>true</code> if this shader erred during initialization
	 */
	@API(status = MAINTAINED, since = "1.0.0")
	boolean isErrored();

	/**
	 * Releases this shader's resources.
	 * <p>
	 * After this method is called, this shader will go back to its uninitialized state.
	 * Future calls to {@link #isInitialized()} will return false until {@link #initialize()}
	 * is called again, recreating the shader group.
	 *
	 * <p>If the caller does not intend to use this shader effect again, they
	 * should call {@link ShaderEffectManager#dispose(ManagedShaderEffect)}.
	 *
	 * @see ShaderEffectManager#dispose(ManagedShaderEffect)
	 * @see #isInitialized()
	 * @see #getShaderEffect()
	 */
	@API(status = EXPERIMENTAL, since = "1.0.0")
	void release();

	/**
	 * Renders this shader.
	 *
	 * <p>
	 * Calling this method first setups the graphic state for rendering,
	 * then uploads uniforms to the GPU if they have been changed since last
	 * draw, draws the {@link Minecraft#getMainRenderTarget() main render target}'s texture
	 * to intermediate {@link RenderTarget render targets} as defined by the JSON files
	 * and resets part of the graphic state. The shader will be {@link #initialize() initialized}
	 * if it has not been before.
	 * <p>
	 * This method should be called every frame when the shader is active.
	 * Uniforms should be set before rendering.
	 */
	@API(status = STABLE, since = "1.0.0")
	void render(float tickDelta);

	@API(status = EXPERIMENTAL, since = "1.4.0")
	ManagedRenderTarget getTarget(String name);

	/**
	 * Forwards to {@link #setupDynamicUniforms(int, Runnable)} with an index of 0
	 *
	 * @param dynamicSetBlock a block in which dynamic uniforms are set
	 */
	@API(status = EXPERIMENTAL, since = "1.0.0")
	void setupDynamicUniforms(Runnable dynamicSetBlock);

	/**
	 * Runs the given block while the shader at the given index is active
	 *
	 * @param index           the shader index within the group
	 * @param dynamicSetBlock a block in which dynamic name uniforms are set
	 */
	@API(status = EXPERIMENTAL, since = "1.0.0")
	void setupDynamicUniforms(int index, Runnable dynamicSetBlock);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value       int value
	 * @see #findUniform1i(String)
	 * @see Uniform1i#set(int)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, int value);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value0      int value
	 * @param value1      int value
	 * @see #findUniform2i(String)
	 * @see Uniform2i#set(int, int)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, int value0, int value1);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value0      int value
	 * @param value1      int value
	 * @param value2      int value
	 * @see #findUniform3i(String)
	 * @see Uniform3i#set(int, int, int)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, int value0, int value1, int value2);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value0      int value
	 * @param value1      int value
	 * @param value2      int value
	 * @param value3      int value
	 * @see #findUniform4i(String)
	 * @see Uniform4i#set(int, int, int, int)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, int value0, int value1, int value2, int value3);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value       float value
	 * @see #findUniform1f(String)
	 * @see Uniform1f#set(float)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, float value);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value0      float value
	 * @param value1      float value
	 * @see #findUniform2f(String)
	 * @see Uniform2f#set(float, float)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, float value0, float value1);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value0      float value
	 * @param value1      float value
	 * @param value2      float value
	 * @see #findUniform3f(String)
	 * @see Uniform3f#set(float, float, float)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, float value0, float value1, float value2);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value0      float value
	 * @param value1      float value
	 * @param value2      float value
	 * @param value3      float value
	 * @see #findUniform4f(String)
	 * @see Uniform4f#set(float, float, float, float)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, float value0, float value1, float value2, float value3);

	/**
	 * Sets the value of a uniform declared in json
	 *
	 * @param uniformName the name of the uniform field in the shader source file
	 * @param value       a matrix
	 * @see #findUniformMat4(String)
	 * @see UniformMat4#set(Matrix4f)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setUniformValue(String uniformName, Matrix4f value);

	/**
	 * Sets the value of a sampler uniform declared in json
	 *
	 * @param samplerName the name of the sampler uniform field in the shader source file and json
	 * @param texture     a texture object
	 * @see #findSampler(String)
	 * @see SamplerUniform#set(AbstractTexture)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setSamplerUniform(String samplerName, AbstractTexture texture);

	/**
	 * Sets the value of a sampler uniform declared in json
	 *
	 * @param samplerName the name of the sampler uniform field in the shader source file and json
	 * @param textureFbo  a render target which main texture will be used
	 * @see #findSampler(String)
	 * @see SamplerUniform#set(RenderTarget)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setSamplerUniform(String samplerName, RenderTarget textureFbo);

	/**
	 * Sets the value of a sampler uniform declared in json
	 *
	 * @param samplerName the name of the sampler uniform field in the shader source file and json
	 * @param textureName an opengl texture name
	 * @see #findSampler(String)
	 * @see SamplerUniform#set(int)
	 */
	@API(status = STABLE, since = "1.0.0")
	void setSamplerUniform(String samplerName, int textureName);

	@Override
	SamplerUniformV2 findSampler(String samplerName);
}
