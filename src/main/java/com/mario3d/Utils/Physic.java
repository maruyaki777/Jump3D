package com.mario3d.Utils;

import com.mario3d.WorldSystem.WorldPosition;

public class Physic {

    public double[] force;
    public double[] speed;
    public Collision collision;

    public Physic() {
        force = new double[3];
        speed = new double[3];
        this.collision = null;
    }
    
    public Physic(Collision collision) {
        force = new double[3];
        speed = new double[3];
        this.collision = collision;
    }

    public WorldPosition calc(WorldPosition pos) {
        WorldPosition pos2 = new WorldPosition(pos);
        pos2.x += speed[0];
        pos2.y += speed[1];
        pos2.z += speed[2];
        if (collision != null) {
            pos2 = collision.calc(pos, pos2);
            for (int i = 0;i < 6;i++) {
                int r = -1;
                if (i >= 3) r = 1;
                if (collision.touch[i] && speed[i % 3] * r > 0) {speed[i % 3] = 0;}
            }
            if (collision.touch[1] == false) force[1] -= 0.01;
            if (collision.touch[1] == true) {
                speed[0] *= 0.5;
                speed[2] *= 0.5;
            }
            else {
                speed[0] *= 0.8;
                speed[2] *= 0.8;
            }
        }
        for (int i = 0;i < 3;i++) {speed[i] += force[i]; force[i] = 0;}
        return pos2;
    }

    public void addForce(double x, double y, double z) {
        this.force[0] += x;
        this.force[1] += y;
        this.force[2] += z;
    }

    public void addForce(double x, double y, double z, double xMax, double yMax, double zMax) {
        Math.abs(xMax);
        Math.abs(yMax);
        Math.abs(zMax);
        double xMin = xMax;
        double yMin = yMax;
        double zMin = zMax;
        if (x < 0 && speed[0] > xMin) {
            if (speed[0] + x < xMin) force[0] += xMin - speed[0];
            force[0] += x;
        }
        else if (x > 0 && speed[0] < xMax) {
            if (speed[0] + x > xMax) force[0] += xMax - speed[0];
            force[0] += x;
        }
        if (y < 0 && speed[1] > yMin) {
            if (speed[1] + y < yMin) force[1] += yMin - speed[1];
            force[1] += y;
        }
        else if (y > 0 && speed[1] < yMax) {
            if (speed[1] + y > yMax) force[1] += yMax - speed[1];
            force[1] += y;
        }
        if (z < 0 && speed[2] > zMin) {
            if (speed[2] + z < zMin) force[2] += zMin - speed[2];
            force[2] += z;
        }
        else if (z > 0 && speed[2] < zMax) {
            if (speed[2] + z > zMax) force[2] += zMax - speed[2];
            force[2] += z;
        }
    }
}
