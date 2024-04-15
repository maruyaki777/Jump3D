package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelTool;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.WorldSystem.WorldPosition;

public class Tool extends Entity{
    public Tool(WorldPosition pos, Aspect aspect) {
        super("tool", pos);
        super.model = new ModelTool(this);
        this.aspect = aspect;
        collision = new Collision(this, 0.25, 0.5);
        GameScreen.models.add(model);
    }

    private int lifetime = 0;
    private static final int default_lifetime = 120;

    @Override
    public void calc() {
        final double speed = 0.5;
        double radx = Math.toRadians(aspect.x);
        double rady = Math.toRadians(aspect.y);
        double cosrady = Math.cos(rady);
        double z = Math.cos(radx) * cosrady * speed;
        double x = Math.sin(radx) * cosrady * speed;
        double y = Math.sin(rady) * speed;
        WorldPosition wp = new WorldPosition(pos.x + x, pos.y + y, pos.z + z);
        collision.calc(this.pos, wp);
        for (int i = 0;i < collision.touch.length;i++) {
            if (collision.touch[i]) {
                kill();
                break;
            }
        }
        this.pos = wp;
        if (lifetime > default_lifetime) kill();
        lifetime++;
        return;
    }

    @Override
    public void kill() {GameScene.world.removeEntity(this);}

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent) event;
            if (ce.entity == GameScene.player) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 0));
        }
        else if (event.eventType == GameEvent.EventType.MonsterDamage) {
            MonsterDamageEvent mde = (MonsterDamageEvent) event;
            if (mde.level >= 1) kill();
            else GameEvent.add(new MonsterDamageEvent(mde.entity, this, 1));
        }
    }
}
