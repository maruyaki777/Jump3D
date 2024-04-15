package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.App;
import com.mario3d.Display;
import com.mario3d.GameManager;

import java.awt.geom.Rectangle2D;

import java.awt.Font;

public class TitleScreen implements Window{

    public TitleScreen() {}

    private final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);
    private Rectangle2D rd_copyright = textr.getBounds("Copyright © 2024 Maruyaki. All rights reserved.");
    private Rectangle2D rd_start = textr.getBounds("Press Enter Key");

    private static final double[][] corners = new double[][] {
        { 0.0f, 0.0f, 0.0f },
        { 1.0f, 0.0f, 0.0f },
        { 1.0f, 1.0f, 0.0f },
        { 0.0f, 1.0f, 0.0f },
        { 0.0f, 0.0f, 1.0f },
        { 1.0f, 0.0f, 1.0f },
        { 1.0f, 1.0f, 1.0f },
        { 0.0f, 1.0f, 1.0f }
    };

    private static final int[][] face = new int[][] {
        { 0, 1, 2, 3 },
        { 1, 5, 6, 2 },
        { 5, 4, 7, 6 },
        { 4, 0, 3, 7 },
        { 4, 5, 1, 0 },
        { 3, 2, 6, 7 }
    };

    private static final float texpos[][] = {
        {0.0f, 0.0f},
        {0.0f, 1.0f},
        {1.0f, 1.0f},
        {1.0f, 0.0f}
    };

    private float r = 0;
    private float blink = 0;

    public void display(GL2 gl) {
        GameManager.display.display2D(gl);
        int width = GameManager.display.width;
        int height = GameManager.display.height;
        float rate = (float)height / width;
        Texture tex = Textures.Misc.textures[0];
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor3f(0.5f, 0.5f, 0.5f);
        gl.glVertex2f(-1f, 1f);
        gl.glVertex2f(1f, 1f);
        gl.glColor3f(0.8f, 0.8f, 0.8f);
        gl.glVertex2f(1f, -1f);
        gl.glVertex2f(-1f, -1f);
        gl.glEnd();
        gl.glColor3f(1, 1, 1);
        tex.enable(gl);
        tex.setTexParameterf(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        tex.bind(gl);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-0.8f * rate, 0.2f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-0.8f * rate, 0.8f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(0.8f * rate, 0.8f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(0.8f * rate, 0.2f);
        gl.glEnd();
        tex.disable(gl);
        gl.glDisable(GL2.GL_BLEND);
        //ブロック
        GameManager.display.display3D(gl, 0.5f, 5.0f, 5.0f, 0.5f, 2.0f, 0.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        tex = Textures.Misc.textures[1];
        tex.enable(gl);
        tex.setTexParameterf(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        tex.bind(gl);
        gl.glTranslatef(0.5f, 0.5f, 0.5f);
        gl.glRotatef(r, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(-0.5f, -0.5f, -0.5f);
        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0;i < face.length;i++) {
            gl.glColor4f(1f, 1f, 1f, 1f);
            for (int j = 0;j < face[i].length;j++) {
                gl.glTexCoord2f(texpos[j][0], texpos[j][1]);
                gl.glVertex3dv(corners[face[i][j]], 0);
            }
        }
        gl.glEnd();
        tex.disable(gl);
        gl.glDisable(GL2.GL_DEPTH_TEST);
        int w = (int)(rd_copyright.getWidth());
        float ap = (float)Display.default_width / width;
        textr.beginRendering((int)(width * 4 * ap), (int)(height * 4 * ap));
        textr.draw("Copyright © 2024 Maruyaki. All rights reserved.", (int)(width * 4 * ap) - w - 50, 50);
        textr.endRendering();
        textr.beginRendering((int)(width * 4 * ap), (int)(height * 4 * ap));
        textr.draw(App.version, 50, 50);
        textr.endRendering();
        if (blink < 60) {
            float ap2 = (float)Display.default_height / height;
            w = (int)(rd_start.getWidth());
            textr.beginRendering((int)(width * 3 * ap2), (int)(height * 3 * ap2));
            textr.draw("Press Enter Key", (int)(width * 3 * ap2 / 2) - (w / 2), 1050);
            textr.endRendering();
        }
        r += 0.5f * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
        //System.out.println(GameManager.display.getFPS());
        if (r > 360) r -= 360f;
        blink += 1.0f * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
        if (blink > 90) blink = 0;
    }
}
