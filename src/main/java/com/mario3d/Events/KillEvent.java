package com.mario3d.Events;

import com.mario3d.Entities.Entity;

public class KillEvent extends GameEvent{
    public Entity entity;
    public KillEvent(Entity entity) {
        super(GameEvent.EventType.Kill, EventUtil::compulsionKillEvent);
        this.entity = entity;
    }
}
