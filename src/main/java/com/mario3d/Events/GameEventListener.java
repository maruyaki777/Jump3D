package com.mario3d.Events;

@FunctionalInterface
public interface GameEventListener {
    public void onEvent(GameEvent event);
}
