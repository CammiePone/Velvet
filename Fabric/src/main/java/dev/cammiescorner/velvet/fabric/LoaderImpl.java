package dev.cammiescorner.velvet.fabric;

import dev.cammiescorner.velvet.api.util.Loader;
import net.fabricmc.loader.api.FabricLoader;

public class LoaderImpl implements Loader {
	@Override
	public boolean isModLoaded(String namespace) {
		return FabricLoader.getInstance().isModLoaded(namespace);
	}
}
