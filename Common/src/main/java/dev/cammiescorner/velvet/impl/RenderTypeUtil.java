package dev.cammiescorner.velvet.impl;

import net.minecraft.client.renderer.RenderType;

public class RenderTypeUtil {

	/**
	 * NeoForge patches {@link RenderType}'s class initializer to assign IDs to certain render types, causing our registry to freeze.
	 * we dont want that to happen so for neo only, we set this to false when that code runs.
	 */
	public static boolean freezeRegistryHack = true;

}
