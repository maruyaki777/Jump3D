package com.mario3d.Cubes;

import com.mario3d.WorldSystem.WorldPosition;

public class Brick extends Cube{
    public Brick(WorldPosition pos1, WorldPosition pos2) {
        super(pos1, pos2);
        super.id = "brick";
    }
}
