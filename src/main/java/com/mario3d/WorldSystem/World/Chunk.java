package com.mario3d.WorldSystem.World;

import com.mario3d.WorldSystem.WorldChunkPosition;

import java.util.ArrayList;
import java.util.List;
import com.mario3d.Cubes.*;

public class Chunk {
    public final WorldChunkPosition pos;
    public List<Cube> container;
    
    public Chunk(WorldChunkPosition position) {
        this.pos = new WorldChunkPosition(position);
        container = new ArrayList<>();
    }
}
