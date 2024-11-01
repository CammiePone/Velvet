package org.ladysnake.satintestcore.event;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface BlockRenderLayerMap {
	BlockRenderLayerMap INSTANCE = new BlockRenderLayerMapImpl();

	void putBlock(Block block, RenderType renderLayer);

	void putBlocks(RenderType renderLayer, Block... blocks);

	@Deprecated(forRemoval = true)
	void putItem(Item item, RenderType renderLayer);

	@Deprecated(forRemoval = true)
	void putItems(RenderType renderLayer, Item... items);

	void putFluid(Fluid fluid, RenderType renderLayer);

	void putFluids(RenderType renderLayer, Fluid... fluids);
}
