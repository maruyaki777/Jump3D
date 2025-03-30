package com.mario3d.Events;

import com.mario3d.Entities.Entity;
import com.mario3d.Entities.Spawner;
import com.mario3d.Scenes.GameScene;

public class SignalEvent extends GameEvent{
	public SignalEvent(GameEventListener destination) {
		super(GameEvent.EventType.Signal, destination);
	}
	
	public static void sendSignals(int destination) {
		for (Entity entity : GameScene.world.entities) {
			switch (entity.id) {
				case "spawner": {
					if (((Spawner) entity).spawnerid == destination) GameEvent.add(new SignalEvent(entity));
				}
			}
		}
	}
}
