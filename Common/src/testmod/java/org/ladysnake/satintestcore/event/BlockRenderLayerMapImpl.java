package org.ladysnake.satintestcore.event;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class BlockRenderLayerMapImpl implements BlockRenderLayerMap {
	public BlockRenderLayerMapImpl() { }

	@Override
	public void putBlock(Block block, RenderType renderLayer) {
		if (block == null) throw new IllegalArgumentException("Request to map null block to BlockRenderLayer");
		if (renderLayer == null) throw new IllegalArgumentException("Request to map block " + block.toString() + " to null BlockRenderLayer");

		blockHandler.accept(block, renderLayer);
	}

	@Override
	public void putBlocks(RenderType renderLayer, Block... blocks) {
		for (Block block : blocks) {
			putBlock(block, renderLayer);
		}
	}

	@Override
	public void putItem(Item item, RenderType renderLayer) {
		if (item == null) throw new IllegalArgumentException("Request to map null item to BlockRenderLayer");
		if (renderLayer == null) throw new IllegalArgumentException("Request to map item " + item.toString() + " to null BlockRenderLayer");

		itemHandler.accept(item, renderLayer);
	}

	@Override
	public void putItems(RenderType renderLayer, Item... items) {
		for (Item item : items) {
			putItem(item, renderLayer);
		}
	}

	@Override
	public void putFluid(Fluid fluid, RenderType renderLayer) {
		if (fluid == null) throw new IllegalArgumentException("Request to map null fluid to BlockRenderLayer");
		if (renderLayer == null) throw new IllegalArgumentException("Request to map fluid " + fluid.toString() + " to null BlockRenderLayer");

		fluidHandler.accept(fluid, renderLayer);
	}

	@Override
	public void putFluids(RenderType renderLayer, Fluid... fluids) {
		for (Fluid fluid : fluids) {
			putFluid(fluid, renderLayer);
		}
	}

	private static Map<Block, RenderType> blockRenderLayerMap = new HashMap<>();
	private static Map<Item, RenderType> itemRenderLayerMap = new HashMap<>();
	private static Map<Fluid, RenderType> fluidRenderLayerMap = new HashMap<>();

	//This consumers initially add to the maps above, and then are later set (when initialize is called) to insert straight into the target map.
	private static BiConsumer<Block, RenderType> blockHandler = (b, l) -> blockRenderLayerMap.put(b, l);
	private static BiConsumer<Item, RenderType> itemHandler = (i, l) -> itemRenderLayerMap.put(i, l);
	private static BiConsumer<Fluid, RenderType> fluidHandler = (f, b) -> fluidRenderLayerMap.put(f, b);

	public static void initialize(BiConsumer<Block, RenderType> blockHandlerIn, BiConsumer<Fluid, RenderType> fluidHandlerIn) {
		//Done to handle backwards compat, in previous snapshots Items had their own map for render layers, now the BlockItem is used.
		BiConsumer<Item, RenderType> itemHandlerIn = (item, renderLayer) -> blockHandlerIn.accept(Block.byItem(item), renderLayer);

		//Add all the pre existing render layers
		blockRenderLayerMap.forEach(blockHandlerIn);
		itemRenderLayerMap.forEach(itemHandlerIn);
		fluidRenderLayerMap.forEach(fluidHandlerIn);

		//Set the handlers to directly accept later additions
		blockHandler = blockHandlerIn;
		itemHandler = itemHandlerIn;
		fluidHandler = fluidHandlerIn;
	}
}
