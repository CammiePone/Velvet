package org.ladysnake.satintestcore.mixin.client;

import net.minecraft.client.Minecraft;
import org.ladysnake.satintestcore.event.EndClientTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "tick", at = @At("RETURN"))
	private void endTick(CallbackInfo ci) {
		EndClientTickEvent.EVENT.invoker().onEndTick((Minecraft) (Object) this);
	}
}
