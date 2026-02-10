package com.mario3d.Entities;

import com.mario3d.CubeEntity.CubeEntity;
import com.mario3d.CubeEntity.InvisibleCubeEntityByEntity;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelFaller;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public class Faller extends Entity{
    public Faller(WorldPosition pos) {
        super("faller", pos);
        super.model = new ModelFaller(this);
        collision = new Collision(this, 2, 4);
        height = pos.y;
        GameScreen.models.add(model);
    }

    private int mode = 0;
    private Runnable modecalc = this::common;
    private double height;
    private CubeEntity cubeentity;

    @Override
    public void calc() {
        modecalc.run();
    }

    @Override
    public void kill() {}

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent) event;
            if (mode == 1) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 2));
        }
        else if (event.eventType == GameEvent.EventType.MonsterDamage) {
            MonsterDamageEvent mde = (MonsterDamageEvent) event;
            if (mde.level >= 2 && mde.entity.id != "faller") kill();
        }
    }

    //0
    private void common() {
        if (cubeentity == null) cubeentity_make();
        if (GameScene.player.pos.y < this.pos.y &&
            GameScene.player.pos.x < this.pos.x + collision.dx && GameScene.player.pos.x > this.pos.x - collision.dx &&
            GameScene.player.pos.z < this.pos.z + collision.dx && GameScene.player.pos.z > this.pos.z - collision.dx) {
            
            modecalc = this::falling;
            mode = 1;
            cubeentity_remove();
        }
    }


    //1
    private void falling() {
        final double speed = 0.5;
        WorldPosition wp = new WorldPosition(pos.x, pos.y - speed, pos.z);
        wp = collision.calc(pos, wp);
        if (collision.touch[1]) {
            freezetime = 0;
            modecalc = this::freezing;
            mode = 2;
            cubeentity_make();
        }
        pos = wp;
    }

    //2
    private int freezetime;
    private void freezing() {
        if (freezetime > 40) {modecalc = this::raising;mode = 3;cubeentity_remove();}
        freezetime++;
    }

    //3
    private void raising() {
        final double speed = 0.05;
        WorldPosition wp = new WorldPosition(pos.x, pos.y + speed, pos.z);
        wp = collision.calc(pos, wp);
        if (collision.touch[4]) {
            modecalc = this::common;
            mode = 0;
            cubeentity_make();
        }
        else if (wp.y > height) {
            wp.y = height;
            modecalc = this::common;
            mode = 0;
            cubeentity_make();
        }
        pos = wp;
    }

    private void cubeentity_make() {
        cubeentity = new InvisibleCubeEntityByEntity(new WorldPosition(pos), new RelativePosition(-2.0, 0, -2.0), new RelativePosition(2.0, 4.0, 2.0));
        GameScene.world.addEntity(cubeentity);
    }

    private void cubeentity_remove() {
        if (cubeentity == null) return;
        cubeentity.kill();
        cubeentity = null;
    }
}
