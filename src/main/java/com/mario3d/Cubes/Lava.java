package com.mario3d.Cubes;

import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.TouchLavaEvent;
import com.mario3d.WorldSystem.WorldPosition;

public class Lava extends Cube{
    public Lava(WorldPosition pos1, WorldPosition pos2) {
        super(pos1, pos2);
        super.id = "lava";
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent) event;
            GameEvent.add(new TouchLavaEvent(ce.entity));
        }
    }
}
