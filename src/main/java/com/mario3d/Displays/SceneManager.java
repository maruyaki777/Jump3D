package com.mario3d.Displays;

import com.jogamp.opengl.GL2;
import com.mario3d.GameManager;
import com.mario3d.Scenes.*;

/*
 *画面を管理するクラス。
 シーンによって分ける以外にも画面の優先度の管理も行う。
*/
public class SceneManager {
    private static Window debugWindow = new DebugScreen();
    private static Window[] gameWindows = new Window[] {new GameScreen(), new GameStatScreen(), new ViewTextScreen(), new EscapeScreen(), debugWindow};
    private static Window[] titleWindows = new Window[] {new TitleScreen(), debugWindow};
    private static Window[] downWindows = new Window[] {new DownScreen(), debugWindow};
    private static Window[] endcredits = new Window[] {new EndCreditScreen(), debugWindow};
    private static Window[] slots;
    private static Scene lastScene = null;

    public static void init() {
        slots = titleWindows;
    }

    public static void display(GL2 gl) {
        //最初に実行するウィンドウを選択する。
        if (GameManager.scene == lastScene) {}
        else if (GameManager.scene instanceof GameScene) slots = gameWindows;
        else if (GameManager.scene instanceof TitleScene) slots = titleWindows;
        else if (GameManager.scene instanceof DownScene) slots = downWindows;
        else if (GameManager.scene instanceof EndCreditsScene) slots = endcredits;
        else return;
        lastScene = GameManager.scene;
        for (int i = 0;i < slots.length;i++) {
            if (slots[i] == null) continue;
            gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();
            slots[i].display(gl);
        }
    }
}
