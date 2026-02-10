package com.mario3d.Events;

public class TouchLavaEvent extends GameEvent{
    public TouchLavaEvent(GameEventListener destination) {
        super(GameEvent.EventType.TouchLava, destination);
    }
}
