package com.mario3d.CubeEntity;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Entity;
import com.mario3d.Entities.Utils.PhysicPort;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.SelfCollisionEvent;
import com.mario3d.Models.ModelLift;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public class Lift extends CubeEntity{

    private WorldPosition startpos;
    private WorldPosition endpos;
    private double[] vector;

    public Lift(WorldPosition pos, int[] tag) {
        super("lift", pos, new RelativePosition(-1.0, -0.5, -1.0), new RelativePosition(1.0, 0.0, 1.0));
        super.model = new ModelLift(this);
        GameScreen.models.add(model);
        collision = new Collision(this, 1, -0.5);
        startpos = new WorldPosition(pos);
        endpos = new WorldPosition(pos.x + tag[0], pos.y + tag[1], pos.z + tag[2]);
        double dtx = endpos.x - startpos.x;
        double dty = endpos.y - startpos.y;
        double dtz = endpos.z - startpos.z;
        double distant = Math.sqrt((dtx*dtx) + (dty*dty) + (dtz*dtz));
        vector = new double[] {dtx / distant, dty / distant, dtz / distant};
    }

    private double speed = 0.05;
    @Override
    public void calc() {
        oldpos = pos;
        pos = new WorldPosition(pos);
        pos.x += speed * vector[0];
        pos.y += speed * vector[1];
        pos.z += speed * vector[2];
        if (speed > 0 && ((endpos.x - pos.x)*vector[0] < 0 || (endpos.y - pos.y)*vector[1] < 0 || (endpos.z - pos.z)*vector[2] < 0)) {
            speed = -speed;
            pos.x = endpos.x; pos.y = endpos.y; pos.z = endpos.z;
        }
        else if (speed < 0 && ((startpos.x - pos.x)*vector[0] > 0 || (startpos.y - pos.y)*vector[1] > 0 || (startpos.z - pos.z)*vector[2] > 0)) {
            speed = -speed;
            pos.x = startpos.x; pos.y = startpos.y; pos.z = startpos.z;
        }
        collision.calc(oldpos, pos);
    }
    
    @Override
    public void onEvent(GameEvent event) {
    	Entity ceentity = null;
    	if (event.eventType == GameEvent.EventType.Collision) {
    		System.out.println("aaaaa");
    		CollisionEvent ce = (CollisionEvent) event;
    		ceentity = ce.entity;
    	}
    	else if (event.eventType == GameEvent.EventType.SelfCollision) {
    		System.out.println("bbbbb");
    		SelfCollisionEvent sce = (SelfCollisionEvent) event;
    		ceentity = sce.entity;
    	}
    	if (ceentity instanceof PhysicPort) collision: {
    		System.out.println("ccc");
			WorldPosition epos = ceentity.pos;
			if (epos.y >= pos.y) break collision;
			Collision cecollision = ceentity.collision;
			Physic cephysic = ((PhysicPort)ceentity).getPhysic();
			double xdivide = Math.abs(pos.x - epos.x);
			double zdivide = Math.abs(pos.z - epos.z);
			double dv = 1 + cecollision.dx;
			int unit = (speed == 0) ? 0 : (speed < 0) ? -1 : 1;
			if (xdivide <= dv || zdivide <= dv) {
				if (xdivide > zdivide) {
					//cephysic.addForce(speed * vector[0] + speed * Math.abs(dv - xdivide) * 3 - cephysic.speed[0], 0, 0);
					cephysic.speed[0] = speed * vector[0];
					//ceentity.pos.x += unit * Math.abs(dv - xdivide);
				}
				else {
					//cephysic.addForce(0, 0, speed * vector[2] + speed * Math.abs(dv - zdivide) * 3 - cephysic.speed[2]);
					cephysic.speed[2] = speed * vector[2] + unit * Math.abs(dv - zdivide);
					//ceentity.pos.z += unit * Math.abs(dv - zdivide);
				}
			}
		}
    }
}
