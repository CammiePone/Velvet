/*
 * Satin
 * Copyright (C) 2019-2024 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package org.ladysnake.satintestcore.common.item;

import org.ladysnake.satintestcore.SatinTestCore;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.ladysnake.satintestcore.common.block.SatinTestBlocks;

public class SatinTestItems {
    public static final DebugItem DEBUG_ITEM = new DebugItem(new Item.Properties());
    public static final BlockItem DEBUG_BLOCK = new BlockItem(SatinTestBlocks.DEBUG_BLOCK, new Item.Properties());

    public static void init() {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(SatinTestCore.MOD_ID, "debug_item"), DEBUG_ITEM);
        Registry.register(BuiltInRegistries.ITEM, BuiltInRegistries.BLOCK.getKey(SatinTestBlocks.DEBUG_BLOCK), DEBUG_BLOCK);
    }
}