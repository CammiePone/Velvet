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
package org.ladysnake.satintestcore;

import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.satinrenderlayer.SatinRenderLayerTest;
import org.ladysnake.satintestcore.common.block.SatinTestBlocks;
import org.ladysnake.satintestcore.common.item.SatinTestItems;

public class SatinTestCore implements MainEntryPoint {
    public static final String MOD_ID = "velvettestcore";
	public static final RegistryHandler<EntityType<?>> ENTITY_TYPES = RegistryHandler.create(Registries.ENTITY_TYPE, SatinRenderLayerTest.MOD_ID);
	RegistryService registryService = RegistryService.get();

	public static final @NotNull RegistrySupplier<EntityType<IronGolem>> ILLUSION_GOLEM = SatinTestCore.ENTITY_TYPES.register("illusion_golem", () -> EntityType.Builder.of(IronGolem::new, MobCategory.CREATURE).sized(EntityType.IRON_GOLEM.getWidth(), EntityType.IRON_GOLEM.getHeight()).build(null));
	public static final @NotNull RegistrySupplier<EntityType<WitherBoss>> RAINBOW_WITHER = SatinTestCore.ENTITY_TYPES.register("rainbow_wither", () -> EntityType.Builder.of((EntityType<WitherBoss> entityType, Level level) -> {
		WitherBoss witherBoss = new WitherBoss(entityType, level);
		witherBoss.setNoAi(true);
		return witherBoss;
	}, MobCategory.MONSTER).sized(EntityType.WITHER.getWidth(), EntityType.WITHER.getHeight()).build(null));

	@Override
	public void onInitialize(ModContainer mod) {
		SatinTestBlocks.init();
		SatinTestItems.init();
		ENTITY_TYPES.accept(registryService);
	}
}
