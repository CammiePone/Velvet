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

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.ladysnake.satintestcore.common.debugbehavior.DebugBehavior;
import org.ladysnake.satintestcore.init.SatinTestDataComponents;
import org.ladysnake.satintestcore.init.SatinTestDebugBehaviors;

import java.util.Comparator;
import java.util.List;

public class DebugItem extends Item {

    public DebugItem(Item.Properties settings) {
        super(settings.component(SatinTestDataComponents.DEBUG_BEHAVIOR.get(), SatinTestDebugBehaviors.BASIC.holder()));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
	    ItemStack stack = player.getItemInHand(hand);
	    Holder<DebugBehavior> currentDebugBehavior = stack.get(SatinTestDataComponents.DEBUG_BEHAVIOR.get());
        if (player.isCrouching() && !world.isClientSide()) {
            if (SatinTestDebugBehaviors.DEBUG_BEHAVIORS_REGISTRY.size() > 1) {

				List<Holder<DebugBehavior>> all = SatinTestDebugBehaviors.DEBUG_BEHAVIORS_REGISTRY.asLookup().listElements().sorted(Comparator.comparing(it -> it.key().location())).map(it -> (Holder<DebugBehavior>) it).toList();
				int currentIndex = all.indexOf(currentDebugBehavior);
				int newIndex = (currentIndex + 1) % all.size();

				Holder<DebugBehavior> newDebugBehavior = all.get(newIndex);
				stack.set(SatinTestDataComponents.DEBUG_BEHAVIOR.get(), newDebugBehavior);

                player.displayClientMessage(Component.translatable("message.velvettestcore.debug_behavior_switched", newDebugBehavior.value().getName()), true);
            }
        } else if (!player.isCrouching()) {
	        //noinspection DataFlowIssue
	        currentDebugBehavior.value().call(world, player, hand);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
