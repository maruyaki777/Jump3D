package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.mario3d.Display;
import com.mario3d.GameManager;
import com.mario3d.Scenes.GameScene;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;

public class ViewTextScreen implements Window{
    private static final float upfloatpos = 0.7f;
    private static final float downfloatpos = -0.7f;
    private static final float leftfloatpos = -0.7f;
    private static final float rightfloatpos = 0.7f;
    private final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);

    private static float[] temp = new float[1];

    @Override
    public void display(GL2 gl) {
        if (GameScene.state != GameScene.State.TEXT) return;
        GameManager.display.display2D(gl);
        String[] t = text;
        float ratio = (float)time / final_time;
        float down = (downfloatpos - upfloatpos) * ratio + upfloatpos;
        float left = leftfloatpos * ratio;
        float right = rightfloatpos * ratio;
        gl.glGetFloatv(GL2.GL_LINE_WIDTH, temp, 0);
        float default_line_width = temp[0];
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        //枠内
        gl.glColor4f(0, 0, 0, 0.8f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(left, upfloatpos);
        gl.glVertex2f(right, upfloatpos);
        gl.glVertex2f(right, down);
        gl.glVertex2f(left, down);
        gl.glEnd();
        //線
        gl.glLineWidth(2f);
        gl.glColor4f(1, 1, 1, 1);
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex2f(left, upfloatpos);
        gl.glVertex2f(right, upfloatpos);
        gl.glVertex2f(right, down);
        gl.glVertex2f(left, down);
        gl.glEnd();
        gl.glLineWidth(default_line_width);
        gl.glDisable(GL2.GL_BLEND);
        if (t != null && time < final_time) {
            time += 1.0f * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
            if (time > final_time) time = final_time;
            return;
        }
        else if (t == null) {
            time -= 1.0f * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
            if (time <= 0) {GameScene.state = GameScene.State.GAME;time = 0;}
            return;
        }
        final int down_per_slot = 150;
        int width = GameManager.display.width;
        int height = GameManager.display.height;
        float apw = (float)Display.default_width / width;
        int x = (int)(width * 4 * apw);
        int y = (int)(height * 4 * apw);
        textr.beginRendering(x, y);
        textr.setColor(new Color(1f, 1f, 1f));
        for (int i = 0;i < t.length;i++) {
            int text_width = (int) textr.getBounds(t[i]).getWidth();
            textr.draw(t[i], x / 2 - (text_width / 2), (int)(y / 2 * (upfloatpos - 0.1f)) + (y / 2) - (down_per_slot * i));
        }
        textr.endRendering();
        return;
    }

    private static String[] text = null;
    private static float time;
    private static final int final_time = 30;

    public static void set(String text) {
        StringBuilder str = new StringBuilder(text);
        StringBuilder buf = new StringBuilder();
        List<String> result_list = new ArrayList<>();
        int len = str.length();
        for (int i = 0;i < len;i++) {
            char c = str.charAt(i);
            if (c == '\n') {
                result_list.add(buf.toString());
                buf = new StringBuilder();
            }
            else {
                buf.append(c);
            }
        }
        GameScene.state = GameScene.State.TEXT;
        String[] result = new String[result_list.size()];
        ViewTextScreen.text = result_list.toArray(result);
        ViewTextScreen.time = 0;
        return;
    }

    public static void free() {
        text = null;
        return;
    }
}
