package com.mario3d.Events;

import com.mario3d.Entities.Entity;

public class SteppedEntityEvent extends GameEvent{
    public final Entity killed;
    public SteppedEntityEvent(GameEventListener destination, Entity killed) {
        super(GameEvent.EventType.SteppedEntity, destination);
        this.killed = killed;
    }    
}
