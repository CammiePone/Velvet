package dev.cammiescorner.velvet.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.cammiescorner.velvet.api.util.VelvetShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.ShaderInstance$1")
public class ShaderInstanceImportProcessorMixin {
	@Inject(method = "applyImport", at = @At("HEAD"))
	private void captureImport(boolean inline, String name, CallbackInfoReturnable<String> info, @Share("name") LocalRef<String> ref) {
		ref.set(name);
	}

	@ModifyVariable(method = "applyImport", at = @At("STORE"), ordinal = 0, argsOnly = true)
	private String modifyImportId(String id, boolean inline, @Share("name") LocalRef<String> ref) {
		if(!inline && ref.get().indexOf(ResourceLocation.NAMESPACE_SEPARATOR) != -1)
			return VelvetShaderInstance.rewriteAsId(id, ref.get()).toString();

		return id;
	}
}
