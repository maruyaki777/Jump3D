package com.mario3d.Events;

import com.mario3d.WorldSystem.WorldPosition;

public class GoalEvent extends GameEvent{
    public final WorldPosition setpos;
    public final double downdistant;
    public GoalEvent(GameEventListener destination, WorldPosition setpos, double downdistant) {
        super(GameEvent.EventType.Goal, destination);
        this.setpos = setpos;
        this.downdistant = downdistant;
    }
}
