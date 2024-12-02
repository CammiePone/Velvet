package dev.cammiescorner.velvet;

import dev.cammiescorner.velvet.api.event.ResolutionChangeCallback;
import dev.cammiescorner.velvet.api.event.LevelRendererReloadCallback;
import dev.cammiescorner.velvet.service.Loader;
import dev.cammiescorner.velvet.impl.ReloadableShaderEffectManager;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apiguardian.api.API;

public class Velvet implements ClientEntryPoint {
	public static final String MOD_ID = "velvet";
	public static final Logger LOGGER = LogManager.getLogger("Velvet");
	public static final Loader LOADER = Services.getService(Loader.class);

	@Override
	public void onInitializeClient(ModContainer mod) {
		ResolutionChangeCallback.EVENT.register(ReloadableShaderEffectManager.INSTANCE);
		LevelRendererReloadCallback.EVENT.register(ReloadableShaderEffectManager.INSTANCE);

		if(LOADER.isModLoaded("optifabric") || LOADER.isModLoaded("optifine"))
			LOGGER.warn("[Velvet] Optifine present in the instance, custom entity post process shaders will not work");
		if(LOADER.isModLoaded("vivecraft"))
			LOGGER.warn("[Velvet] Vivecraft present in the instance, you may experience degraded performance - try turning eye stencil off in VR settings");
	}

	/**
	 * Checks if OpenGL shaders are disabled in the current game instance.
	 * Currently, this only checks if the hardware supports them, however
	 * in the future it may check a client option as well.
	 */
	@API(status = API.Status.STABLE)
	public static boolean areShadersDisabled() {
		return false;
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
