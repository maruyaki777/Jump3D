package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Display;
import com.mario3d.GameManager;

import java.awt.Font;

public class EndCreditScreen implements Window{
    private static int scroll;
    private static float scroll_float;
    public static void resetscroll() {scroll = 0;scroll_float = 0;finished = false;}

    private static final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);
    private static final int startpos = -2000;
    private static final int downperslot = 100;
    public static boolean finished;

    private static String[] EndCreditString;
    private static int[] textwidthsby2;
    public static int setEndCredit(String[] texts) {
        textwidthsby2 = null;
        EndCreditString = texts;
        return 0;
    }

    private static void setEndCredit() {
        if (EndCreditString == null) return;
        textwidthsby2 = new int[EndCreditString.length];
        for (int i = 0;i < EndCreditString.length;i++) {
            if (EndCreditString[i] == null) {
                EndCreditString[i] = "";
                continue;
            }
            textwidthsby2[i] = (int)(textr.getBounds(EndCreditString[i]).getWidth() / 2);
        }
    }

    @Override
    public void display(GL2 gl) {
        int[] textwidthsby2;
        if ((textwidthsby2 = EndCreditScreen.textwidthsby2) == null) {setEndCredit();return;}
        GameManager.display.display2D(gl);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor4f(0, 0, 0, 1);
        gl.glVertex2f(-1, -1);
        gl.glVertex2f(-1, 1);
        gl.glVertex2f(1, 1);
        gl.glVertex2f(1, -1);
        gl.glEnd();
        final int width = GameManager.display.width;
        final int height = GameManager.display.height;
        final float apw = (float)Display.default_width / width;
        final int x = (int)(width * 4 * apw);
        final int y = (int)(height * 4 * apw);
        final int widthby2 = x / 2;
        float rate = (float)width / height;
        Texture tex = Textures.Misc.textures[0];
        gl.glColor3f(1, 1, 1);
        tex.enable(gl);
        tex.setTexParameterf(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        tex.bind(gl);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-0.6f, -0.45f * rate - 1.1f + ((float)scroll * 2 / y));
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-0.6f, 0f * rate - 1.1f + ((float)scroll * 2 / y));
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(0.6f, 0f * rate - 1.1f + ((float)scroll * 2 / y));
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(0.6f, -0.45f * rate - 1.1f + ((float)scroll * 2 / y));
        gl.glEnd();
        tex.disable(gl);
        textr.beginRendering(x, y);
        for (int i = 0;i < EndCreditString.length;i++) {
            textr.draw(EndCreditString[i], widthby2 - textwidthsby2[i], startpos+scroll-(downperslot * i));
        }
        textr.endRendering();
        scroll_float += 1.5f * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
        scroll = (int)scroll_float;
        if (startpos+scroll-(downperslot * EndCreditString.length) > Display.default_height * 4) finished = true;
    }
}
