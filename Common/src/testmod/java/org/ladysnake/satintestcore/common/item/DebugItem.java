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

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class DebugItem extends Item {
    private int debugMode;
    private final List<DebugMode> debugModes = new ArrayList<>();

    public DebugItem(Item.Properties settings) {
        super(settings);
    }

    public void registerDebugMode(String name, DebugCallback callback) {
        this.debugModes.add(new DebugMode(name, callback));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.isCrouching() && !world.isClientSide()) {
            if (this.debugModes.size() > 1) {
                debugMode = (debugMode + 1) % this.debugModes.size();
                player.displayClientMessage(Component.translatable("Switched mode to %s", this.debugModes.get(debugMode).name()), true);
            }
        } else if (!player.isCrouching()) {
            this.debugModes.get(debugMode).callback().use(world, player, hand);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
