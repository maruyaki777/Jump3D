package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.mario3d.DebugManager;
import com.mario3d.Display;
import com.mario3d.GameManager;
import com.mario3d.Events.GameEvent;
import com.mario3d.Scenes.GameScene;

import java.awt.Font;

public class DebugScreen implements Window{
    private static final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);
    private static final int downperslot = 80;

    @Override
    public void display(GL2 gl) {
        int down_slots = 1;
        if (!GameManager.debug_mode) return;
        GameManager.display.display2D(gl);
        int width = GameManager.display.width;
        int height = GameManager.display.height;
        float ap = (float)Display.default_width / width;
        textr.beginRendering((int)(width * 4 * ap), (int)(height * 4 * ap));
        textr.draw("FPS: " + String.valueOf(GameManager.fps), 0, height * 4 - downperslot * down_slots++);
        if (GameScene.world != null)
        textr.draw("E: "+String.valueOf(GameScene.world.entities.size())+"  M: "+String.valueOf(GameScreen.models.size()+GameScreen.deathmodels.size()), 0, height*4-downperslot*down_slots++);
        textr.draw("EventQueue:", 0, height * 4 - downperslot * down_slots++);
        for (GameEvent event : DebugManager.events) {
            textr.draw("    " + event.eventType.toString(), 0, height * 4 - downperslot * down_slots++);
        }
        textr.endRendering();
    }
}
