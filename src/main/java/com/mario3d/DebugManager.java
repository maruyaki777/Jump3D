package com.mario3d;
import com.mario3d.Events.GameEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DebugManager {
    public static Queue<GameEvent> events = new ConcurrentLinkedQueue<>();

    public static void debugEventEnqueue(GameEvent event) {
        if (!GameManager.debug_mode) return;
        if (event.eventType == GameEvent.EventType.Collision) return;
        events.add(event);
        int s = events.size();
        if (s > 20) {
            events.poll();
        }
    }
}
