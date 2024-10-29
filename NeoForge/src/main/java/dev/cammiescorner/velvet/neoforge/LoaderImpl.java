package dev.cammiescorner.velvet.neoforge;

import dev.cammiescorner.velvet.api.util.Loader;
import net.neoforged.fml.loading.LoadingModList;

public class LoaderImpl implements Loader {
	@Override
	public boolean isModLoaded(String namespace) {
		return LoadingModList.get().getMods().stream().anyMatch(modInfo -> modInfo.getModId().equals(namespace));
	}
}
