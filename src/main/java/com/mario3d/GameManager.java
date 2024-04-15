package com.mario3d;

import java.util.TimerTask;

import com.mario3d.Scenes.DownScene;
import com.mario3d.Scenes.EndCreditsScene;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Scenes.Scene;
import com.mario3d.Scenes.KeyAction;
import com.mario3d.Scenes.TitleScene;

import java.util.Timer;

//すべての共通部分の変数を格納したりシーンを実行したりするためのクラス
public class GameManager {
    public static MouseKeyboard mk = null;
    public static Display display = null;
    public static Scene scene;
    public static KeyAction keyaction;
    public static Scene[] scene_slot = new Scene[] {new TitleScene(), new GameScene(), new DownScene(), new EndCreditsScene()};
    public static final int gameTick = 40;
    public static boolean debug_mode = false;

    public static void start() {
        final int slot_num = 0;
        scene_slot[slot_num].init();
        scene = scene_slot[slot_num];
        if (scene instanceof KeyAction) keyaction = (KeyAction)scene;
        else keyaction = null;
        time = 0;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                main();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 30);
    }

    public static void main() {
        int realfps = (int)display.getFPS();
        if (realfps == 0) fps = 1;
        else fps = realfps;
        Scene t = scene.execute();
        if (t != null) {
            t.init();
            if (t instanceof KeyAction) keyaction = (KeyAction)t;
            else keyaction = null;
            scene = t;
        }
        time += 1;
    }

    private static long time;
    public static int fps = Display.inner_fps;
    public static int realfps = Display.inner_fps;

    public static long getLatestTick() {return time;}

    public static void sysexit() {
        display.animator.stop();
        System.exit(0);
    }
}
