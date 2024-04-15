package com.mario3d.WorldSystem;

import java.lang.Math;

public class WorldPosition {
    public double x;
    public double y;
    public double z;

    public WorldPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WorldPosition(WorldPosition pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    protected WorldPosition() {}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof WorldPosition == false) return false;
        WorldPosition pos = (WorldPosition) obj;
        if (this.x == pos.x && this.y == pos.y && this.z == pos.z) return true;
        return false;
    }

    @Override
    public int hashCode() {
        long bits = 7L;
        int hash;
        bits = 31L * bits + Double.doubleToLongBits(this.x);
        bits = 31L * bits + Double.doubleToLongBits(this.y);
        bits = 31L * bits + Double.doubleToLongBits(this.z);
        hash = (int) (bits ^ (bits >> 32));
        return hash;
    }

    public static WorldPosition convertWorldPosition(double[] pos) {
        if (pos.length < 3) return null;
        WorldPosition r = new WorldPosition();
        r.x = pos[0];
        r.y = pos[1];
        r.z = pos[2];
        return r;
    }

    public double[] distanceXYZ(WorldPosition pos) {
        double[] result = new double[3];
        result[0] = this.x - pos.x;
        result[1] = this.y - pos.y;
        result[2] = this.z - pos.z;
        return result;
    }
    
    public double distance(WorldPosition pos) {
        double[] t = distanceXYZ(pos);
        double d = Math.sqrt((t[0] * t[0]) + (t[1] * t[1]) + (t[2] * t[2]));
        return d;
    }

    public double[] convertArray() {
        double[] result = new double[3];
        result[0] = x;
        result[1] = y;
        result[2] = z;
        return result;
    }

    public WorldChunkPosition getChunkPosition() {
        WorldChunkPosition wcp = new WorldChunkPosition();
        wcp.x = (int)Math.floor(this.x / 10);
        wcp.z = (int)Math.floor(this.z / 10);
        return wcp;
    }

    public void getChunkPosition(WorldChunkPosition wcp) {
        wcp.x = (int)Math.floor(this.x / 10);
        wcp.z = (int)Math.floor(this.z / 10);
        return;
    }
}