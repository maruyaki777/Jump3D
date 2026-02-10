package com.mario3d.CubeEntity;

import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public class InvisibleCubeEntityByEntity extends CubeEntity{
    public InvisibleCubeEntityByEntity(WorldPosition pos, RelativePosition pos1, RelativePosition pos2) {
        super("icebe", pos, pos1, pos2);
    }

    @Override
    public void calc() {}

    @Override
    public void kill() {
        GameScene.world.removeEntity(this);
    }
}
