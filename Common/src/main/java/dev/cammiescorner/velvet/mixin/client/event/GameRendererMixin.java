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
package dev.cammiescorner.velvet.mixin.client.event;

import dev.cammiescorner.velvet.api.event.PickEntityShaderCallback;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.cammiescorner.velvet.impl.ReloadableShaderEffectManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow @Nullable PostChain postEffect;
	@Shadow protected abstract void loadEffect(ResourceLocation id);

	/**
	 * Fires {@link ShaderEffectRenderCallback#EVENT}
	 */
	@Inject(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = AFTER)
	)
	private void hookShaderRender(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
		ShaderEffectRenderCallback.EVENT.invoker().renderShaderEffects(tickCounter.getGameTimeDeltaPartialTick(tick));
	}

	/**
	 * Fires {@link PickEntityShaderCallback#EVENT}
	 * Disabled by optifine
	 */
	@Inject(method = "checkEntityPostEffect", at = @At(value = "RETURN"), require = 0)
	private void useCustomEntityShader(@Nullable Entity entity, CallbackInfo info) {
		if(this.postEffect == null) {
			// Mixin does not like method references to shadowed methods
			// noinspection Convert2MethodRef
			PickEntityShaderCallback.EVENT.invoker().pickEntityShader(entity, resourceLocation -> loadEffect(resourceLocation), () -> this.postEffect);
		}
	}

	@Inject(method = "reloadShaders", at = @At(value = "RETURN"))
	private void loadVelvetPrograms(ResourceProvider factory, CallbackInfo ci) {
		ReloadableShaderEffectManager.INSTANCE.reload(factory);
	}
}
