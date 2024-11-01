package org.ladysnake.satintestcore.neoforge.entrypoints;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;
import org.ladysnake.satinrenderlayer.SatinRenderLayerTest;
import org.ladysnake.satintestcore.SatinTestCore;

@EventBusSubscriber(modid = SatinTestCore.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
@Mod(SatinTestCore.MOD_ID)
public class Main {

	@SubscribeEvent
	public static void onRegisterBlockRenderTypes(RegisterNamedRenderTypesEvent event) {
		RenderType blockRenderType = SatinRenderLayerTest.illusionBuffer.getRenderType(RenderType.translucent());
		RenderType entityRenderType = SatinRenderLayerTest.illusionBuffer.getRenderType(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS, false));

		event.register(ResourceLocation.fromNamespaceAndPath(SatinRenderLayerTest.MOD_ID, "illusion"), blockRenderType, entityRenderType);
	}
}
