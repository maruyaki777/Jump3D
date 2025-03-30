package com.mario3d.Utils;

import com.mario3d.WorldSystem.WorldPosition;

public class Physic {

    public double[] force;
    public double[] speed;
    public Collision collision;
    private double air_resistance = 0.2;
    private double ground_resistance = 0.3;

    public Physic() {
        force = new double[3];
        speed = new double[3];
        runnable = this::addSpeed;
        this.collision = null;
    }
    
    public Physic(Collision collision) {
        force = new double[3];
        speed = new double[3];
        runnable = this::addSpeedCollision;
        this.collision = collision;
    }
    public void setAirResistance(double ar) {air_resistance = ar;}
    public void setGroundResistance(double gr) {ground_resistance = gr;}
    private interface FunctionSpeed {public void run(WorldPosition pos1, WorldPosition pos2);}
    private FunctionSpeed runnable;

    public WorldPosition calc(WorldPosition pos) {
        WorldPosition pos2 = new WorldPosition(pos);
        runnable.run(pos, pos2);
        if (collision != null) {
            pos2 = collision.calc(pos, pos2);
            for (int i = 0;i < 6;i++) {
                int r = -1;
                if (i >= 3) r = 1;
                if (collision.touch[i] && speed[i % 3] * r > 0) {speed[i % 3] = 0;}
            }
            if (collision.touch[1] == false) force[1] -= 0.01;
            if (collision.touch[1] == true) {
                //speed[0] *= 0.5;
                //speed[2] *= 0.5;
            	force[0] += speed[0] * -ground_resistance;
            	force[2] += speed[2] * -ground_resistance;
            }
            else {
                //speed[0] *= 0.8;
                //speed[2] *= 0.8;
            	force[0] += speed[0] * -air_resistance;
            	force[2] += speed[2] * -air_resistance;
            }
        }
        for (int i = 0;i < 3;i++) {speed[i] += force[i]; force[i] = 0;}
        return pos2;
    }

    private void addSpeed(WorldPosition pos1, WorldPosition pos2) {
        pos2.x += speed[0];
        pos2.y += speed[1];
        pos2.z += speed[2];
    }
    private void addSpeedCollision(WorldPosition pos1, WorldPosition pos2) {
        if ((speed[0] < 0 && !collision.touch[0]) || (speed[0] > 0 && !collision.touch[3])) pos2.x += speed[0];
        if ((speed[1] < 0 && !collision.touch[1]) || (speed[1] > 0 && !collision.touch[4])) pos2.y += speed[1];
        if ((speed[2] < 0 && !collision.touch[2]) || (speed[2] > 0 && !collision.touch[5])) pos2.z += speed[2];
    }

    public void addForce(double x, double y, double z) {
        this.force[0] += x;
        this.force[1] += y;
        this.force[2] += z;
    }

    /*public void addForce(double x, double y, double z, double xMax, double yMax, double zMax) {
        xMax = Math.abs(xMax);
        yMax = Math.abs(yMax);
        zMax = Math.abs(zMax);
        double xMin = -xMax;
        double yMin = -yMax;
        double zMin = -zMax;
        double ratio = 1.0;
        
        if (x < 0) {
            //if (speed[0] + x < xMin) ratio = ratio > Math.abs((xMin - speed[0]) / x) ? (xMin - speed[0]) / x) : ratio;
        	if (speed[0] + x < xMin) force[0] += xMin < speed[0] ? x - xMin + speed[0] : 0;
            else force[0] += x;
        }
        else if (x > 0) {
        	//if (speed[0] + x > xMax) ratio = ratio > (xMax - speed[0]) / x ? (xMax - speed[0]) / x : ratio;
            if (speed[0] + x > xMax) force[0] += xMax > speed[0] ? x - xMax + speed[0] : 0;
            else force[0] += x;
        }
        if (y < 0) {
        	//if (speed[1] + y < yMin) ratio = ratio > (yMin - speed[1]) / y ? (yMin - speed[1]) / y : ratio;
            if (speed[1] + y < yMin) force[1] += xMin < speed[1] ? y - yMin + speed[1] : 0;
            else force[1] += y;
        }
        else if (y > 0) {
        	//if (speed[1] + y > yMax) ratio = ratio > (yMax - speed[1]) / y ?(yMax - speed[1]) / y : ratio;
            if (speed[1] + y > yMax) force[1] += xMax > speed[1] ? y - yMax + speed[1] : 0;
            else force[1] += y;
        }
        if (z < 0) {
        	//if (speed[2] + z < zMin) ratio = ratio > (zMin - speed[2]) / z ? (zMin - speed[2]) / z : ratio;
            if (speed[2] + z < zMin) force[2] += xMin < speed[2] ? z - zMin + speed[2] : 0;
            else force[2] += z;
        }
        else if (z > 0) {
        	//if (speed[2] + z > zMax) ratio = ratio > (zMax - speed[2]) / z ? (zMax - speed[2]) / z : ratio;
            if (speed[2] + z > zMax) force[2] += xMax > speed[2] ? z - zMax + speed[2] : 0;
            else force[2] += z;
        }
        //addForce(x * ratio, y * ratio, z * ratio);
        //if (collision.entity.id.equals("player")) System.out.printf("%f %f %f\n", force[0], force[1], force[2]);
    }*/
}
