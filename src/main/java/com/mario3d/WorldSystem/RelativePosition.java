package com.mario3d.WorldSystem;

public class RelativePosition {
    public double x;
    public double y;
    public double z;

    public RelativePosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RelativePosition(RelativePosition pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof RelativePosition == false) return false;
        RelativePosition pos = (RelativePosition) obj;
        if (this.x == pos.x && this.y == pos.y && this.z == pos.z) return true;
        return false;
    }

    @Override
    public int hashCode() {
        long bits = 7L;
        int hash;
        bits = 37L * bits + Double.doubleToLongBits(this.x);
        bits = 37L * bits + Double.doubleToLongBits(this.y);
        bits = 37L * bits + Double.doubleToLongBits(this.z);
        hash = (int) (bits ^ (bits >> 32));
        return hash;
    }

    public WorldPosition getAbsolutePos(WorldPosition pos) {
        pos = new WorldPosition(pos);
        pos.x += this.x;
        pos.y += this.y;
        pos.z += this.z;
        return pos;
    }
}
