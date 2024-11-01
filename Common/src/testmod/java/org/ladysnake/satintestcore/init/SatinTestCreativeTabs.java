package org.ladysnake.satintestcore.init;

import dev.upcraft.sparkweave.api.item.CreativeTabHelper;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import org.ladysnake.satintestcore.SatinTestCore;

public class SatinTestCreativeTabs {

	public static final RegistryHandler<CreativeModeTab> CREATIVE_TABS = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, SatinTestCore.MOD_ID);
	public static final ResourceKey<CreativeModeTab> ITEMS_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, SatinTestCore.id("items"));
	public static final RegistrySupplier<CreativeModeTab> ITEMS = CREATIVE_TABS.register(ITEMS_KEY, () -> CreativeTabHelper.newBuilder(ITEMS_KEY).icon(Items.GLOW_BERRIES::getDefaultInstance).displayItems((itemDisplayParameters, output) -> {
		CreativeTabHelper.addRegistryEntries(itemDisplayParameters, output, SatinTestItems.ITEMS);
		CreativeTabHelper.addRegistryEntries(itemDisplayParameters, output, SatinTestBlocks.BLOCKS);
	}).build());
}
