package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Display;
import com.mario3d.GameManager;
import com.mario3d.Scenes.GameScene;

import java.awt.geom.Rectangle2D;

import java.awt.Font;

public class DownScreen implements Window{

    private final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);
    private Rectangle2D rd_remaining = textr.getBounds("×");

    public void display(GL2 gl) {
        GameManager.display.display2D(gl);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor3f(0, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-1, -1);
        gl.glVertex2f(-1, 1);
        gl.glVertex2f(1, 1);
        gl.glVertex2f(1, -1);
        gl.glEnd();
        Texture tex = Textures.Misc.textures[2];
        tex.enable(gl);
        tex.setTexParameterf(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        tex.bind(gl);
        float ap = (float)GameManager.display.height / GameManager.display.width;
        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-0.3f * ap, -0.15f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(-0.0f * ap, -0.15f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(-0.0f * ap, 0.15f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-0.3f * ap, 0.15f);
        gl.glEnd();
        tex.disable(gl);
        gl.glDisable(GL2.GL_BLEND);
        float ap2 = (float)Display.default_height / GameManager.display.height;
        int w = (int)(rd_remaining.getHeight());
        textr.beginRendering((int)(GameManager.display.width * 2 * ap2), (int)(GameManager.display.height * 2 * ap2));
        textr.draw("×"+String.valueOf(GameScene.getRemaining()), (int)(GameManager.display.width * ap2), (int)(GameManager.display.height * ap2)-(w / 2));
        textr.endRendering();
    }
}
