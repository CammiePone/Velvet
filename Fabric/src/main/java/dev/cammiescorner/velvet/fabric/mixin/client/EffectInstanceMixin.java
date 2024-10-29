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
package dev.cammiescorner.velvet.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.Program;
import dev.cammiescorner.velvet.impl.SamplerAccess;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

/**
 * Minecraft does not take into account domains when parsing a shader program.
 * These hooks redirect identifier instantiations to allow specifying a domain for shader files.
 */
@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin implements SamplerAccess {
	@Shadow @Final private Map<String, IntSupplier> samplerMap;

	@Override
	public void removeSampler(String name) {
		this.samplerMap.remove(name);
	}

	@Override
	public boolean hasSampler(String name) {
		return this.samplerMap.containsKey(name);
	}

	@Override
	@Accessor("samplerNames")
	public abstract List<String> getSamplerNames();

	@Override
	@Accessor("samplerLocations")
	public abstract List<Integer> getSamplerShaderLocs();

	/**
	 * Fix identifier creation to allow different namespaces
	 */
	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
			ordinal = 0
		)
	)
	ResourceLocation constructProgramIdentifier(String arg, Operation<ResourceLocation> original, ResourceProvider unused, String id) {
		if(!id.contains(":")) {
			return original.call(arg);
		}
		ResourceLocation split = ResourceLocation.parse(id);
		return ResourceLocation.fromNamespaceAndPath(split.getNamespace(), "shaders/program/" + split.getPath() + ".json");
	}

	@WrapOperation(
		method = "getOrCreate",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
			ordinal = 0
		)
	)
	private static ResourceLocation constructProgramIdentifier(String arg, Operation<ResourceLocation> original, ResourceProvider unused, Program.Type shaderType, String id) {
		if(!arg.contains(":")) {
			return original.call(arg);
		}
		ResourceLocation split = ResourceLocation.parse(id);
		return ResourceLocation.fromNamespaceAndPath(split.getNamespace(), "shaders/program/" + split.getPath() + shaderType.getExtension());
	}
}
