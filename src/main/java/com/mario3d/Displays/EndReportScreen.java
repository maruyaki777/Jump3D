package com.mario3d.Displays;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.mario3d.Display;
import com.mario3d.GameManager;
import com.mario3d.Scenes.GameScene;

public class EndReportScreen implements Window {
	
	private static final TextRenderer textr = new TextRenderer(new Font("PixelMplus12", Font.PLAIN, 100), false);

	@Override
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
        final int width = GameManager.display.width;
        final int height = GameManager.display.height;
        final float apw = (float)Display.default_width / width;
        final int x = (int)(width * 4 * apw);
        final int y = (int)(height * 4 * apw);
        long timeplayed = GameScene.getPlayTime();
        int hour = (int)(timeplayed / 144000);
        int minute = (int)((timeplayed / 2400) % 60);
        int second = (int)((timeplayed / 40) % 60);
        int handredmillsec = (int)((timeplayed / 4) % 10);
        String displaytimestr = "Game Time: " + ((hour > 0) ? String.valueOf(hour) + "h " : "") + ((hour > 0 || minute > 0) ? String.valueOf(minute) + "m " : "") + String.valueOf(second) + "." + String.valueOf(handredmillsec) + "s";
        Rectangle2D rect = textr.getBounds(displaytimestr);
        int time_width = (int)rect.getWidth();
        int time_height = (int)rect.getHeight();
        String displayremainingstr = "Remaining: " + String.valueOf(GameScene.getRemaining());
        rect = textr.getBounds(displayremainingstr);
        int remaining_width = (int)rect.getWidth();
        int remaining_height = (int)rect.getHeight();
        int up = (time_height + remaining_height + y / 10) / 2;
        String escapestr = "Press ESC to close";
        int escape_width = (int)textr.getBounds(escapestr).getWidth();
        textr.beginRendering(x, y);
        textr.draw(displaytimestr, x / 2 - time_width / 2, y / 2 + up);
        textr.draw(displayremainingstr, x / 2 - remaining_width / 2, y / 2 + up - y / 10);
        textr.setColor(1f, 1f, 0.2f, 1f);
        textr.draw(escapestr, x / 2 - escape_width / 2, y / 2 + up - y * 3 / 10);
        textr.endRendering();
        textr.setColor(1, 1, 1, 1);
	}

}
