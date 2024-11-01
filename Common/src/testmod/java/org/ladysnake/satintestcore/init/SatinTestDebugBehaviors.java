package org.ladysnake.satintestcore.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.ladysnake.satinbasictest.SatinBasicTest;
import org.ladysnake.satincustomformattest.SatinCustomFormatTest;
import org.ladysnake.satindepthtest.SatinDepthTest;
import org.ladysnake.satintestcore.SatinTestCore;
import org.ladysnake.satintestcore.common.debugbehavior.DebugBehavior;

public class SatinTestDebugBehaviors {

	public static final ResourceKey<Registry<DebugBehavior>> DEBUG_BEHAVIORS_KEY = ResourceKey.createRegistryKey(SatinTestCore.id("debug_behaviors"));
	public static final RegistryHandler<DebugBehavior> DEBUG_BEHAVIORS = RegistryHandler.create(DEBUG_BEHAVIORS_KEY, SatinTestCore.MOD_ID);
	public static final Registry<DebugBehavior> DEBUG_BEHAVIORS_REGISTRY = DEBUG_BEHAVIORS.createNewRegistry(true, SatinTestCore.id("basic"));

	public static final RegistrySupplier<SatinBasicTest.BasicTestDebugBehavior> BASIC = DEBUG_BEHAVIORS.register("basic", SatinBasicTest.BasicTestDebugBehavior::new);
	public static final RegistrySupplier<SatinCustomFormatTest.CustomFormatDebugBehavior> CUSTOM_FORMAT = DEBUG_BEHAVIORS.register("custom_format", SatinCustomFormatTest.CustomFormatDebugBehavior::new);
	public static final RegistrySupplier<SatinDepthTest.DepthTestDebugBehavior> DEPTH_TEST = DEBUG_BEHAVIORS.register("depth_test", SatinDepthTest.DepthTestDebugBehavior::new);
}
