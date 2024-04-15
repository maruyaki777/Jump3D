package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.mario3d.Display;
import com.mario3d.GameManager;
import com.mario3d.Scenes.GameScene;

import java.awt.Font;

public class GameStatScreen implements Window{
    private final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);
    private final int time_width = (int)textr.getBounds("Time 000").getWidth();
    @Override
    public void display(GL2 gl) {
        int width = GameManager.display.width;
        int height = GameManager.display.height;
        float aph = (float)Display.default_height / height;
        int x = (int)(width * 2 * aph);
        int y = (int)(height * 2 * aph);
        int time = (GameScene.getTime() + 39) / 40;
        String strtime = String.valueOf(time / 100) + String.valueOf((time / 10) % 10) + String.valueOf(time % 10);
        GameManager.display.display2D(gl);
        textr.setColor(1, 1, 1, 1);
        textr.beginRendering(x, y);
        textr.draw("R ×" + String.valueOf(GameScene.Remaining), 30, 1500);
        textr.endRendering();
        if (time > 100) textr.setColor(1, 1, 1, 1);
        else if (time > 30) textr.setColor(1, 0.5f, 0, 1);
        else textr.setColor(1, 0, 0, 1);
        textr.beginRendering(x, y);
        textr.draw("Time " + strtime, x - time_width - 30, 1500);
        textr.endRendering();
    }
}
