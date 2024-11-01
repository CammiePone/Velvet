package org.ladysnake.satintestcore.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import org.ladysnake.satintestcore.SatinTestCore;
import org.ladysnake.satintestcore.common.debugbehavior.DebugBehavior;

public class SatinTestDataComponents {

	public static final RegistryHandler<DataComponentType<?>> DATA_COMPONENTS = RegistryHandler.create(Registries.DATA_COMPONENT_TYPE, SatinTestCore.MOD_ID);
	public static final RegistrySupplier<DataComponentType<Holder<DebugBehavior>>> DEBUG_BEHAVIOR = DATA_COMPONENTS.register("debug_behavior", () -> DataComponentType.<Holder<DebugBehavior>>builder().persistent(SatinTestDebugBehaviors.DEBUG_BEHAVIORS_REGISTRY.holderByNameCodec()).networkSynchronized(ByteBufCodecs.holderRegistry(SatinTestDebugBehaviors.DEBUG_BEHAVIORS_KEY)).cacheEncoding().build());
}
