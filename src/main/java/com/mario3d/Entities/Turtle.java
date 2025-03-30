package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Events.SteppedEntityEvent;
import com.mario3d.Models.ModelTurtle;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

public class Turtle extends Entity{
    public Turtle(WorldPosition pos) {
        super("turtle", pos);
        super.repulsion = true;
        super.model = new ModelTurtle(this);
        collision = new Collision(this, 0.5, 1);
        physic = new Physic(collision);
        modecalc = this::calcDefault;
        mode = 0;
        GameScreen.models.add(model);
    }

    private Physic physic;
    private Runnable modecalc;
    private int mode;
    public int getmode() {return mode;}

    @Override
    public void calc() {
        if (cooldown > 0) cooldown--;
        modecalc.run();
    }

    private void calcDefault() {
        setAspectMove();
        WorldPosition postmp = physic.calc(pos);
        WorldPosition climb;
        if ((climb = collision.calc_up_stair(pos)) != null) pos = climb;
        else pos = postmp;
        if (pos.y < -100.0) kill();
    }

    private void calcStop() {
        pos = physic.calc(pos);
    }

    private void calcStored() {
        StoredMove();
        WorldPosition postmp = physic.calc(pos);
        WorldPosition climb;
        if ((climb = collision.calc_up_stair(pos)) != null) pos = climb;
        else pos = postmp;
        changeAspect();
    }

    @Override
    public void kill() {
        GameScene.world.removeEntity(this);
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

        double force = 0.0165;
        force *= 1 - (Math.abs(movedegree) / 180);
        double vectorX = Math.sin(Math.toRadians(aspect.x));
        double vectorZ = Math.cos(Math.toRadians(aspect.x));
        physic.addForce(vectorX * force, 0, vectorZ * force);
    }

    private void StoredMove() {
        double force = 0.13;
        double vectorX = Math.sin(Math.toRadians(aspect.x));
        double vectorZ = Math.cos(Math.toRadians(aspect.x));
        physic.addForce(vectorX * force, 0, vectorZ * force);
    }

    private void changeAspect() {
        if (collision.touch[0]) aspect.set(Math.abs(aspect.x), aspect.y);
        if (collision.touch[3]) aspect.set(Math.abs(aspect.x) * -1, aspect.y);
        if (collision.touch[2]) {
            aspect.add(90, 0);
            aspect.set(Math.abs(aspect.x), aspect.y);
            aspect.add(-90, 0);
        }
        if (collision.touch[5]) {
            aspect.add(90, 0);
            aspect.set(Math.abs(aspect.x) * -1, aspect.y);
            aspect.add(-90, 0);
        }
    }

    private int cooldown = 0;

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent) event;
            if (ce.entity == GameScene.player) {
                double distant = Math.sqrt((ce.entity.pos.x - this.pos.x) * (ce.entity.pos.x - this.pos.x) + (ce.entity.pos.z - this.pos.z) * (ce.entity.pos.z - this.pos.z));
                double degree = Math.toDegrees(Math.atan2(ce.entity.pos.y - this.pos.y, distant));
                if (mode == 1) {
                    if (cooldown <= 0) {
                        cooldown = 15;
                        mode = 2;
                        modecalc = this::calcStored;
                        WorldPosition playerpos = ce.entity.pos;
                        if (pos.x - playerpos.x == pos.z - playerpos.z) return;
                        double degreeasp = Math.toDegrees(Math.atan2(pos.x - playerpos.x, pos.z - playerpos.z));
                        aspect.set(degreeasp, 0);
                    }
                }
                else if (degree >= 50) {
                    if (cooldown <= 0) {
                        cooldown = 15;
                        mode = 1;
                        modecalc = this::calcStop;
                        GameEvent.add(new SteppedEntityEvent(ce.entity, null));
                    }
                }
                else if (mode == 2) {if (cooldown <= 0) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 1));}
                else GameEvent.add(new MonsterDamageEvent(ce.entity, this, 0));
            }
            else if (mode == 2) {
                GameEvent.add(new MonsterDamageEvent(ce.entity, this, 1));
            }
        }
        else if (event.eventType == GameEvent.EventType.TouchLava) kill();
        else if (event.eventType == GameEvent.EventType.MonsterDamage) {
            MonsterDamageEvent mde = (MonsterDamageEvent) event;
            if (mde.level >= 1) kill();
        }
    }
}
