package dev.cammiescorner.velvet.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.Program;
import dev.cammiescorner.velvet.api.util.VelvetShaderInstance;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {
	@Shadow @Final private String name;

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"), allow = 1)
	private ResourceLocation modifyId(String id, Operation<ResourceLocation> original) {
		if ((Object) this instanceof VelvetShaderInstance) {
			return VelvetShaderInstance.rewriteAsId(id, name);
		}

		return original.call(id);
	}

	// Allow loading shader stages from arbitrary namespaces.
	@ModifyVariable(method = "getOrCreate", at = @At("STORE"), ordinal = 1)
	private static String modifyStageId(String id, ResourceProvider factory, Program.Type type, String name) {
		if (name.contains(String.valueOf(ResourceLocation.NAMESPACE_SEPARATOR))) {
			return VelvetShaderInstance.rewriteAsId(id, name).toString();
		}

		return id;
	}

	@WrapOperation(method = "getOrCreate", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"), allow = 1)
	private static ResourceLocation allowNoneMinecraftId(String id, Operation<ResourceLocation> original) {
		if (id.contains(String.valueOf(ResourceLocation.NAMESPACE_SEPARATOR))) {
			return ResourceLocation.parse(id);
		}

		return original.call(id);
	}
}
