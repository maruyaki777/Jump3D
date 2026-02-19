package com.mario3d.Scenes;

import com.jogamp.newt.event.KeyEvent;
import com.mario3d.GameManager;

public class TitleScene implements Scene, KeyAction{
    
    public TitleScene() {}

    private Scene next;

    @Override
    public void init() {
        next = null;
    }

    @Override
    public Scene execute() {
        return next;
    }

    @Override
    public void onKey(short KeyCode) {
        switch (KeyCode) {
            case KeyEvent.VK_ESCAPE: {
                GameManager.sysexit();
                break;
            }
            case KeyEvent.VK_ENTER: {
            	GameScene.resetProfile();
                next = GameManager.scene_slot[2];
                break;
            }
            case KeyEvent.VK_0: {
            	EndCreditsScene.setReportStatus(false);
                next = GameManager.scene_slot[3];
                break;
            }
        }
    }
} 
