package org.ladysnake.satintestcore.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import org.ladysnake.satintestcore.SatinTestCore;

public class SatinTestEntities {

	public static final RegistryHandler<EntityType<?>> ENTITY_TYPES = RegistryHandler.create(Registries.ENTITY_TYPE, SatinTestCore.MOD_ID);
	public static final RegistrySupplier<EntityType<WitherBoss>> RAINBOW_WITHER = ENTITY_TYPES.register("rainbow_wither", () -> EntityType.Builder.of((EntityType<WitherBoss> entityType, Level level) -> {
		WitherBoss witherBoss = new WitherBoss(entityType, level);
		witherBoss.setNoAi(true);
		return witherBoss;
	}, MobCategory.MONSTER).sized(EntityType.WITHER.getWidth(), EntityType.WITHER.getHeight()).build(SatinTestCore.id("rainbow_wither").toString()));
	public static final RegistrySupplier<EntityType<IronGolem>> ILLUSION_GOLEM = ENTITY_TYPES.register("illusion_golem", () -> EntityType.Builder.of(IronGolem::new, MobCategory.CREATURE).sized(EntityType.IRON_GOLEM.getWidth(), EntityType.IRON_GOLEM.getHeight()).build(SatinTestCore.id("illusion_golem").toString()));
}
