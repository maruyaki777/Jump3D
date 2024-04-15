package com.mario3d.Events;

import java.util.Queue;

import com.mario3d.DebugManager;

import java.util.LinkedList;

public abstract class GameEvent {
    public final EventType eventType;
    private final GameEventListener destination;

    protected GameEvent(EventType eventType, GameEventListener destination) {
        this.eventType = eventType;
        this.destination = destination;
    }

    private static Queue<GameEvent> list = new LinkedList<>();
    public static void calc() {
        int s = list.size();
        for (int i = 0;i < s;i++) {
            GameEvent ge = list.poll();
            ge.destination.onEvent(ge);
            DebugManager.debugEventEnqueue(ge);
        }
    }

    public static void add(GameEvent gameEvent) {
        list.add(gameEvent);
    }

    public enum EventType {
        Collision("CollisionEvent"),
        Goal("GoalEvent"),
        SteppedEntity("SteppedEntityEvent"),
        TouchLava("TouchLavaEvent"),
        MonsterDamage("MonsterDamageEvent"),
        Kill("KillEvent");

        private final String name;
        private EventType(String toString) {this.name = toString;}
        @Override
        public String toString() {
            return name;
        }
    }
}
