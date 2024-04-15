package com.mario3d.Scenes;

import com.mario3d.GameManager;
import com.mario3d.Displays.GameScreen;

public class DownScene implements Scene{

    public DownScene() {}
    
    private int time;

    @Override
    public void init() {
        time = 0;
    }

    @Override
    public Scene execute() {
        if (time > 60) return GameManager.scene_slot[1];
        time++;
        GameScreen.needload();
        return null;
    }
}
