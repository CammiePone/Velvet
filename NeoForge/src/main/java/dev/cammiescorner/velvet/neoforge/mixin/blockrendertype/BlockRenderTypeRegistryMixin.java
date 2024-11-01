package dev.cammiescorner.velvet.neoforge.mixin.blockrendertype;

import dev.cammiescorner.velvet.impl.BlockRenderTypeRegistry;
import dev.cammiescorner.velvet.neoforge.impl.NeoRenderType;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockRenderTypeRegistry.class, remap = false)
public class BlockRenderTypeRegistryMixin {

	@Inject(method = "registerRenderType", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
	private void doThing(RenderType type, CallbackInfo ci) {
		((NeoRenderType) type).velvet$initBlockLayerId();
	}
}
