package dev.cammiescorner.velvet.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.velvet.api.util.VelvetShaderInstance;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {
	@Shadow @Final private String name;

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"), allow = 1)
	private ResourceLocation modifyId(String id, Operation<ResourceLocation> original) {
		if((Object) this instanceof VelvetShaderInstance)
			return VelvetShaderInstance.rewriteAsId(id, name);

		return original.call(id);
	}

	@WrapOperation(method = "getOrCreate", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"), allow = 1)
	private static ResourceLocation allowNoneMinecraftId(String id, Operation<ResourceLocation> original) {
		if(id.indexOf(ResourceLocation.NAMESPACE_SEPARATOR) != -1)
			return ResourceLocation.parse(id);

		return original.call(id);
	}
}
