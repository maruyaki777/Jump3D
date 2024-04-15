package com.mario3d.Cubes;

import com.mario3d.Displays.GameScreen;
import com.mario3d.WorldSystem.WorldPosition;

public class Grass extends Cube{
    public Grass(WorldPosition pos1, WorldPosition pos2) {
        super(pos1, pos2);
        super.id = "grass";
        super.displayType = GameScreen.CubeDisplayType.GRASS;
    }
}
