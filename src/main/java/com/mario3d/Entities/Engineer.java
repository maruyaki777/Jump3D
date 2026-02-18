package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Entities.Utils.PhysicPort;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelEngineer;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

public class Engineer extends Entity implements PhysicPort {
    public Engineer(WorldPosition pos) {
        super("engineer", pos);
        super.model = new ModelEngineer(this);
        collision = new Collision(this, 0.5, 1.3);
        physic = new Physic(collision);
        GameScreen.models.add(model);
        this.time = 0;
        super.repulsion = true;
    }

    private Physic physic;
    private int time;
    private static final int interval = 150;

    @Override
    public void calc() {
        setAspect();
        pos = physic.calc(pos);
        //攻撃
        final double playertargetheight = 1.2;
        final double entityfrom = 0.5;
        final int throw_aspect_interval = 10;
        if (GameScene.player.alive) {
            if (time > interval && this.pos.distance(GameScene.player.pos) < 20) {
                double distant = Math.sqrt(((GameScene.player.pos.x - this.pos.x) * (GameScene.player.pos.x - this.pos.x)) + ((GameScene.player.pos.z - this.pos.z) * (GameScene.player.pos.z - this.pos.z)));
                double distantY = GameScene.player.pos.y + playertargetheight - this.pos.y - entityfrom;
                double angleY;
                if (distant != 0 && distantY != 0) {
                    angleY = Math.toDegrees(Math.atan2(distantY, distant));
                }
                else angleY = 0;
                Aspect toolasp = new Aspect(aspect);
                toolasp.set(toolasp.x, angleY);
                WorldPosition toolpos = new WorldPosition(pos);
                toolpos.y += entityfrom;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
                toolasp.x -= throw_aspect_interval;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
                toolasp.x += throw_aspect_interval * 2;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
                time = 0;
            }
            else if (time <= interval) time++;
        }
    }

    @Override
    public void kill() {GameScene.world.removeEntity(this);}

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent) event;
            if (ce.entity == GameScene.player) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 0));
        }
        else if (event.eventType == GameEvent.EventType.TouchLava) kill();
        else if (event.eventType == GameEvent.EventType.MonsterDamage) {
            MonsterDamageEvent mde = (MonsterDamageEvent) event;
            if (mde.level >= 1) kill();
        }
    }

    private void setAspect() {
        final double d = 3;
        if (GameScene.player.alive == false) return;
        WorldPosition playerpos = GameScene.player.pos;
        double paidegree = Math.atan2(playerpos.x - pos.x, playerpos.z - pos.z);
        double degree = Math.toDegrees(paidegree);
        double movedegree = degree - aspect.x;
        double unit = movedegree / Math.abs(movedegree);
        if (Math.abs(movedegree) > Math.abs(360 - Math.abs(movedegree))) {movedegree = 360 - Math.abs(movedegree);unit *= -1;}
        if (Math.abs(movedegree) < d) aspect.set(degree, aspect.y);
        else aspect.add(unit * d, 0);
    }
    
    @Override
    public Physic getPhysic() {return physic;}
}
