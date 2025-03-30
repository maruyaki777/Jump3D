package com.mario3d.Entities;

import com.mario3d.GameManager;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelSAT;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

public class SAT extends Entity{

    Physic physic;
    
    public SAT(WorldPosition pos) {
        super("sat", pos);
        super.model = new ModelSAT(this);
        super.repulsion = true;
        GameScreen.models.add(model);
        collision = new Collision(this, 0.8, 1.0);
        physic = new Physic(collision);
        physic.setAirResistance(0.15);
        physic.setGroundResistance(0.25);
    }

    @Override
    public void calc() {
        setAspectMove();
        WorldPosition postmp = physic.calc(pos);
        WorldPosition climb;
        if ((climb = collision.calc_up_stair(pos)) != null) pos = climb;
        else pos = postmp;
        if (pos.y < -100.0) kill();
    }

    @Override
    public void kill() {GameScene.world.removeEntity(this);}

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent) event;
            if (ce.entity == GameScene.player) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 0));
            else if (ce.entity.repulsion) repulsion_calc(ce.entity, physic);
        }
        else if (event.eventType == GameEvent.EventType.TouchLava) kill();
        else if (event.eventType == GameEvent.EventType.MonsterDamage) {
            MonsterDamageEvent mde = (MonsterDamageEvent) event;
            if (mde.level >= 2) kill();
            else GameEvent.add(new MonsterDamageEvent(mde.entity, this, 1));
        }
    }

    private boolean old_space = false;

    private void setAspectMove() {
        if (!GameScene.player.alive) return;
        WorldPosition ppos = GameScene.player.pos;
        double paidegree = Math.atan2((ppos.x - pos.x), (double)(ppos.z - pos.z));
        double degree = Math.toDegrees(paidegree);
        double t = Math.abs(degree - aspect.x);
        int d = 6;
        double degreeA = t;
        if (t > Math.abs(360 - t)) {d *= -1;degreeA = Math.abs(360 - t);}
        if (degree - aspect.x < 0) d *= -1;
        if (t < 3) aspect.set(degree, 0);
        else aspect.add(d, 0);

        double force = 0.065;
        force *= 1 - ((double)degreeA / 180);
        double vectorX = Math.sin(Math.toRadians(aspect.x));
        double vectorZ = Math.cos(Math.toRadians(aspect.x));
        physic.addForce(vectorX * force, 0, vectorZ * force);
        if (GameManager.mk.getKeyStateVK_Space() && !old_space && collision.touch[1]) physic.addForce(0, 0.3, 0);
        old_space = GameManager.mk.getKeyStateVK_Space();
    }
}
