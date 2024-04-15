package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.mario3d.Display;
import com.mario3d.GameManager;
import com.mario3d.Scenes.GameScene;

import java.awt.geom.Rectangle2D;

import java.awt.Font;

public class EscapeScreen implements Window{
    
    public EscapeScreen() {}

    private final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);
    private final int keyESC_width = (int)textr.getBounds("[ESC] Back to Game").getWidth();
    private final int key1_width = (int)textr.getBounds("[1] Commit Suicide (Death)").getWidth();
    private final int key2_width = (int)textr.getBounds("[2] End Game").getWidth();
    
    @Override
    public void display(GL2 gl) {
        if (GameScene.state != GameScene.State.POSE) return;
        GameManager.display.display2D(gl);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor4f(0, 0, 0, 0.8f);
        gl.glVertex2f(-1, -1);
        gl.glVertex2f(-1, 1);
        gl.glVertex2f(1, 1);
        gl.glVertex2f(1, -1);
        gl.glEnd();
        gl.glDisable(GL2.GL_BLEND);
        GameManager.display.display3D(gl, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f);
        Rectangle2D rd = textr.getBounds("Game Menu");
        float w = (float)rd.getWidth();
        textr.begin3DRendering();
        textr.draw3D("Game Menu", -w/2000/*-0.22f*/, 0.3f, -1, 0.001f);
        textr.end3DRendering();
        /*TextRenderer textr = new TextRenderer(new Font("Tahoma", Font.PLAIN, 80), true);
        textr.beginRendering(GameManager.display.width, GameManager.display.height);
        textr.draw("Game Menu", GameManager.display.width / 2, GameManager.display.height / 2);
        textr.end3DRendering();*/
        int width = GameManager.display.width;
        int height = GameManager.display.height;
        float aph = (float)Display.default_height / height;
        int x = (int)(width * 3 * aph);
        int y = (int)(height * 3 * aph);
        GameManager.display.display2D(gl);
        textr.beginRendering(x, y);
        textr.draw("[ESC] Back to Game", x / 2 - (keyESC_width / 2), 1100);
        textr.draw("[1] Commit Suicide (Death)", x / 2 - (key1_width / 2), 900);
        textr.draw("[2] End Game", x / 2 - (key2_width / 2), 700);
        textr.endRendering();
    }
}
