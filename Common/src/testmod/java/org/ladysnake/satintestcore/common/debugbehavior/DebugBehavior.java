package org.ladysnake.satintestcore.common.debugbehavior;

import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.ladysnake.satintestcore.SatinTestCore;
import org.ladysnake.satintestcore.init.SatinTestDebugBehaviors;

public abstract class DebugBehavior {

	public Component getName() {
		return Component.translatable(this.getTranslationId());
	}

	public String getTranslationId() {
		return Util.makeDescriptionId("%s.debug_behavior".formatted(SatinTestCore.MOD_ID), SatinTestDebugBehaviors.DEBUG_BEHAVIORS_REGISTRY.getKey(this));
	}

	public abstract void call(Level level, Player player, InteractionHand hand);

	public static abstract class Client extends DebugBehavior {

		@Override
		public final void call(Level level, Player player, InteractionHand hand) {
			if(level.isClientSide()) {
				this.clientAction((ClientLevel) level, (AbstractClientPlayer) player, hand);
			}
		}

		public abstract void clientAction(ClientLevel level, AbstractClientPlayer player, InteractionHand hand);

	}

	public static abstract class Server extends DebugBehavior {

		@Override
		public void call(Level level, Player player, InteractionHand hand) {
			if (!level.isClientSide()) {
				this.serverAction((ServerLevel) level, (ServerPlayer) player, hand);
			}
		}

		public abstract void serverAction(ServerLevel level, ServerPlayer player, InteractionHand hand);
	}
}
