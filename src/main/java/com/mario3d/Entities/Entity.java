package com.mario3d.Entities;

import com.mario3d.CubeEntity.*;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.GameEventListener;
import com.mario3d.Models.Model;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

public abstract class Entity implements GameEventListener{
    public String id;
    public Model model;
    public WorldPosition pos;
    public Aspect aspect;
    public int[] tag;
    public Collision collision;
    public boolean repulsion = false;
    public boolean loaded = false;

    protected Entity(String id, WorldPosition pos) {
        this.id = id;
        this.pos = pos;
        this.aspect = new Aspect(0, 0);
        this.collision = null;
        this.model = null;
        this.tag = null;
    }

    abstract public void calc();
    abstract public void kill();

    @Override
    public void onEvent(GameEvent event) {}
    
    private double loadrange = 30;
    public double getloadrange() {return loadrange;}
    
    public void setloadrange(double range) {loadrange = range;}

    public static Entity NewEntity(Entity entity) {
        return NewEntity(entity.id, entity.pos, entity.tag);
    }

    public static Entity NewEntity(String id, WorldPosition pos, int[] tag) {
        Entity entity = null;
        pos = new WorldPosition(pos);
        switch (id) {
            case "": {
                return null;
            }
            case "test": {
                entity = new Test(pos);
                break;
            }
            case "player": {
                entity = new Player(pos);
                break;
            }
            case "sat": {
                entity = new SAT(pos);
                break;
            }
            case "lift": {
                entity = new Lift(pos, tag);
                break;
            }
            case "messagebox": {
                entity = new MessageBox(pos, tag);
                break;
            }
            case "goal": {
                entity = new Goal(pos);
                break;
            }
            case "daruma": {
                entity = new Daruma(pos);
                break;
            }
            case "engineer": {
                entity = new Engineer(pos);
                break;
            }
            case "faller": {
                entity = new Faller(pos);
                break;
            }
            case "turtle": {
                entity = new Turtle(pos);
                break;
            }
            case "trapmessagebox": {
                entity = new TrapMessageBox(pos);
                break;
            }
            case "trapfloor": {
                entity = new TrapFloor(pos, tag);
                break;
            }
            case "spawner": {
            	entity = new Spawner(pos, tag);
            	break;
            }
            case "signalpoint": {
            	entity = new SignalPoint(pos, tag);
            	break;
            }
            case "checkpoint": {
            	entity = new CheckPoint(pos, tag);
            	break;
            }
            case "maneki": {
            	entity = new Maneki(pos);
            	break;
            }
        }
        entity.tag = tag.clone();
        return entity;
    }

    public void repulsion_calc(Entity e, Physic physic) {
        final double force = 0.044;
        double x = this.pos.x - e.pos.x;
        double z = this.pos.z - e.pos.z;
        double distant = Math.sqrt((x * x) + (z * z));
        x /= distant;
        z /= distant;
        if (distant != 0 && x != Double.NaN && z != Double.NaN) physic.addForce(x * force, 0, z * force);
    }
}
