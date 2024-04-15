package com.mario3d.Events;

import com.mario3d.Entities.Entity;
import com.mario3d.WorldSystem.WorldPosition;

public class CollisionEvent extends GameEvent{

    public final Entity entity;
    public final WorldPosition pos;
    
    //エンティティがぶつかってきた時に出す。
    public CollisionEvent(GameEventListener destination, Entity entity, WorldPosition pos) {
        super(GameEvent.EventType.Collision, destination);
        this.entity = entity;
        this.pos = pos;
    }
}
