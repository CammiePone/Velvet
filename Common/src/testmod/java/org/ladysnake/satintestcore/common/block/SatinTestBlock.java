package org.ladysnake.satintestcore.common.block;

import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SatinTestBlock extends Block implements BlockItemProvider {

	public SatinTestBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Item createItem() {
		return new BlockItem(this, new Item.Properties());
	}
}
