package org.ladysnake.satintestcore.event;

import dev.upcraft.sparkweave.api.event.Event;
import dev.upcraft.sparkweave.event.EventFactoryImpl;
import net.minecraft.client.Minecraft;

public interface EndClientTickEvent {
	Event<EndClientTickEvent> EVENT = EventFactoryImpl.create(EndClientTickEvent.class,
		(listeners) -> minecraft -> {
			for(EndClientTickEvent handler : listeners) {
				handler.onEndTick(minecraft);
			}
		});

	void onEndTick(Minecraft minecraft);
}
