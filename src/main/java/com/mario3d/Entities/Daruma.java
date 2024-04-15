package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Events.SteppedEntityEvent;
import com.mario3d.Models.ModelDaruma;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

public class Daruma extends Entity{
    public Daruma(WorldPosition pos) {
        super("daruma", pos);
        super.repulsion = true;
        super.model = new ModelDaruma(this);
        GameScreen.models.add(model);
        collision = new Collision(this, 0.5, 1);
        physic = new Physic(collision);
    }

    private Physic physic;

    @Override
    public void calc() {
        setAspectMove();
        WorldPosition postmp = physic.calc(pos);
        WorldPosition climb;
        if ((climb = collision.calc_up_stair(pos)) != null) pos = climb;
        else pos = postmp;
        if (pos.y < -100.0) kill();
    }

    private void setAspectMove() {
        final double d = 5;
        if (GameScene.player.alive == false) return;
        WorldPosition playerpos = GameScene.player.pos;
        double paidegree = Math.atan2(playerpos.x - pos.x, playerpos.z - pos.z);
        double degree = Math.toDegrees(paidegree);
        double movedegree = degree - aspect.x;
        double unit = movedegree / Math.abs(movedegree);
        if (Math.abs(movedegree) > Math.abs(360 - Math.abs(movedegree))) {movedegree = 360 - Math.abs(movedegree);unit *= -1;}
        if (Math.abs(movedegree) < d) aspect.set(degree, aspect.y);
        else aspect.add(unit * d, 0);

        double force = 0.03;
        double maxforce = 0.1;
        force *= 1 - (Math.abs(movedegree) / 180);
        double vectorX = Math.sin(Math.toRadians(aspect.x));
        double vectorZ = Math.cos(Math.toRadians(aspect.x));
        physic.addForce(vectorX * force, 0, vectorZ * force, vectorX * maxforce, 0, vectorZ * maxforce);
    }

    @Override
    public void kill() {
        GameScene.world.removeEntity(this);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent)event;
            if (ce.entity == GameScene.player) {
                double distant = Math.sqrt((ce.entity.pos.x - this.pos.x) * (ce.entity.pos.x - this.pos.x) + (ce.entity.pos.z - this.pos.z) * (ce.entity.pos.z - this.pos.z));
                double degree = Math.toDegrees(Math.atan2(ce.entity.pos.y - this.pos.y, distant));
                if (degree >= 50) {
                    this.kill();
                    GameEvent.add(new SteppedEntityEvent(ce.entity, this));
                }
                else GameEvent.add(new MonsterDamageEvent(ce.entity, this, 0));
            }
            else if (ce.entity.repulsion) repulsion_calc(ce.entity, physic);
        }
        else if (event.eventType == GameEvent.EventType.TouchLava) kill();
        else if (event.eventType == GameEvent.EventType.MonsterDamage) {
            MonsterDamageEvent mde = (MonsterDamageEvent) event;
            if (mde.level >= 1) kill();
        }
    }
}
