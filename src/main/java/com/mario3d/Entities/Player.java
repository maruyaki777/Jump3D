package com.mario3d.Entities;

import com.mario3d.GameManager;
import com.mario3d.MouseKeyboard;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.GoalEvent;
import com.mario3d.Models.ModelPlayer;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

public class Player extends Entity{
    public Aspect pasp;
    public Physic physic;
    public boolean alive = true;
    
    public Player(WorldPosition pos) {
        super("player", pos);
        super.model = new ModelPlayer(this);
        GameScreen.models.add(model);
        pasp = new Aspect(0, 0);
        collision = new Collision(this, 0.5, 2);
        physic = new Physic(collision);
        physic.setGroundResistance(0.35);
        GameScene.player = this;
    }

    private Runnable modecalc = this::calcDefault;
    @Override
    public void calc() {
        modecalc.run();
    }

    @Override
    public void kill() {
        if (alive == false) return;
        GameScene.world.removeEntity(this);
        alive = false;
    }
    
    private void calcDefault() {
        KeyInput();
        //System.out.printf("%f %f %f\n", physic.force[0], physic.force[1], physic.force[2]);
        WorldPosition postmp = physic.calc(pos);
        WorldPosition climb;
        if ((climb = collision.calc_up_stair(pos)) != null) pos = climb;
        else pos = postmp;
        aspect = new Aspect(this.pasp);
        if (pos.y < -100.0) kill();
    }

    private boolean goaled = false;
    public boolean getGoalBoolean() {return goaled;}
    private int goaltime = 0;
    private void calcGoal() {
        if (goaltime > goal_downtime) {
            GameScene.clearCurrentCourse();
            return;
        }
        else goaltime++;
        pos.y -= downspeed;
    }

    private boolean old_space = false;
    private boolean doublejump = true;

    private void KeyInput() {
        MouseKeyboard mk = GameManager.mk;
        final double force;
        if ((mk.getKeyStateVK_W() || mk.getKeyStateVK_S()) && (mk.getKeyStateVK_A() || mk.getKeyStateVK_D())) {
            force = 0.0427;
        }
        else {
            force = 0.0605;
        }
        double xforce = 0;
        double zforce = 0;
        if (mk.getKeyStateVK_W()) {
            double vectorX = Math.sin(Math.toRadians(-aspect.x));
            double vectorZ = Math.cos(Math.toRadians(aspect.x));
            xforce += vectorX * force;
            zforce += vectorZ * force;
        }
        if (mk.getKeyStateVK_A()) {
            double vectorX = Math.sin(Math.toRadians(-aspect.x - 270));
            double vectorZ = Math.cos(Math.toRadians(aspect.x + 270));
            xforce += vectorX * force;
            zforce += vectorZ * force;
        }
        if (mk.getKeyStateVK_S()) {
            double vectorX = Math.sin(Math.toRadians(-aspect.x - 180));
            double vectorZ = Math.cos(Math.toRadians(aspect.x + 180));
            xforce += vectorX * force;
            zforce += vectorZ * force;
        }
        if (mk.getKeyStateVK_D()) {
            double vectorX = Math.sin(Math.toRadians(-aspect.x - 90));
            double vectorZ = Math.cos(Math.toRadians(aspect.x + 90));
            xforce += vectorX * force;
            zforce += vectorZ * force;
        }
        /*if (cforce > 0) {
        	xforce /= cforce;
            zforce /= cforce;
            xmax /= cforce;
            zmax /= cforce;
        }*/
        //System.out.printf("%f %f %f\n", xforce, 0.0, zforce);
        physic.addForce(xforce, 0.0, zforce);
        if (collision.touch[1]) doublejump = true;
        if (mk.getKeyStateVK_Space() && !old_space) {
            if (collision.touch[1]) physic.addForce(0, 0.2, 0);
            else if (doublejump) {
                if (physic.speed[1] < 0) physic.speed[1] = 0;
                physic.addForce(0, 0.15, 0);
                doublejump = false;
            }
        }
        old_space = mk.getKeyStateVK_Space();
    }

    private double goal_downtime;
    private final double downspeed = 0.1;
    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.MonsterDamage) {
            kill();
        }
        else if (event.eventType == GameEvent.EventType.Goal && goaled == false) {
            GoalEvent ge = (GoalEvent) event;
            this.pos = ge.setpos;
            goal_downtime = ge.downdistant / downspeed;
            modecalc = this::calcGoal;
            goaled = true;
        }
        else if (event.eventType == GameEvent.EventType.SteppedEntity) {
            if (GameManager.mk.getKeyStateVK_Space()) {
                physic.speed[1] = 0;
                physic.addForce(0, 0.25, 0);
            }
            else {
                physic.speed[1] = 0;
                physic.addForce(0, 0.10, 0);
            }
        }
        else if (event.eventType == GameEvent.EventType.TouchLava) kill();
    }
}
