package com.mario3d.WorldSystem;

public class WorldChunkPosition {
    public int x;
    public int z;
    
    public WorldChunkPosition(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public WorldChunkPosition(WorldChunkPosition pos) {
        this.x = pos.x;
        this.z = pos.z;
    }

    protected WorldChunkPosition() {}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof WorldChunkPosition == false) return false;
        WorldChunkPosition pos = (WorldChunkPosition) obj;
        if (this.x == pos.x && this.z == pos.z) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 31 * 11 + x;
        hash = ((hash << 5) - hash) + z;
        return hash;
    }

    public static WorldChunkPosition convertWorldChunkPosition(int[] pos) {
        if (pos.length < 2) return null;
        WorldChunkPosition r = new WorldChunkPosition();
        r.x = pos[0];
        r.z = pos[1];
        return r;
    }

    public boolean inRange(WorldPosition pos) {
        int px = (int)Math.floor(this.x / 10);
        int pz = (int)Math.floor(this.z / 10);
        if (this.x == px && this.z == pz) return true;
        else return false;
    }

    public int[] convertArray() {
        int[] result = new int[2];
        result[0] = this.x;
        result[1] = this.z;
        return result;
    }
}