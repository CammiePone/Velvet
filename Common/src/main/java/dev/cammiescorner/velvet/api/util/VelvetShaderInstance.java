package dev.cammiescorner.velvet.api.util;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public class VelvetShaderInstance extends ShaderInstance {
	public VelvetShaderInstance(ResourceProvider resourceProvider, ResourceLocation id, VertexFormat vertexFormat) throws IOException {
		super(resourceProvider, id.toString(), vertexFormat);
	}

	public static ResourceLocation rewriteAsId(String input, String containedId) {
		ResourceLocation contained = ResourceLocation.parse(containedId);
		return contained.withPath(path -> input.replace(containedId, path));
	}
}
