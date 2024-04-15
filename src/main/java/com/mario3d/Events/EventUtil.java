package com.mario3d.Events;

import com.mario3d.Scenes.GameScene;

public class EventUtil {
    protected static void compulsionKillEvent(GameEvent event) {
        if (event.eventType != GameEvent.EventType.Kill) return;
        KillEvent ke = (KillEvent) event;
        ke.entity.kill();
        GameScene.world.removeEntity(ke.entity);
    }
}
