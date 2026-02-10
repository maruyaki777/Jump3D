package com.mario3d.CubeEntity;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Models.ModelLift;
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
        new WorldPosition(pos);
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
    }
}
