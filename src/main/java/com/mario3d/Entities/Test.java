package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Models.test;
import com.mario3d.WorldSystem.WorldPosition;

public class Test extends Entity{
    
    //-1
    public Test(WorldPosition pos) {
        super("test", pos);
        super.model = new test(new WorldPosition(pos));
        GameScreen.models.add(model);
    }

    @Override
    public void calc() {}

    @Override
    public void kill() {}
}
