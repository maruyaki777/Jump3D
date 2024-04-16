package com.mario3d.Scenes;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

import com.jogamp.newt.event.KeyEvent;
import com.mario3d.GameManager;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Displays.ViewTextScreen;
import com.mario3d.Entities.Player;
import com.mario3d.Events.GameEvent;
import com.mario3d.WorldSystem.World.World;
import com.mario3d.WorldSystem.WorldLoader.CourseSelector;

public class GameScene implements Scene, KeyAction{
    public static Player player = null;
    public static World world = null;
    public static State state;
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static Lock w = rwl.writeLock();
    //MKが編集
    private static final int default_Remaining = 3;
    public static int Remaining = default_Remaining;
    private static int time;
    private static int courseid;

    private static Scene nextscene;
    private static int changetime;

    public GameScene() {}

    @Override
    public void init() {
        GameScene.world = CourseSelector.selector(courseid);
        state = State.GAME;
        GameManager.mk.dolock();
        nextscene = null;
        changetime = 0;
        time = 300 * 40;
        //throw new IllegalArgumentException("you cant go there.");
    }

    @Override
    public Scene execute() {
        if (state == State.GAME) {
            world.EntityCalc();
            GameEvent.calc();//イベント処理
            world.addEntities();
            world.killEntities();//エンティティのキル処理
            if (player.alive == false) {
                changetime++;
                if (changetime > 120) {nextscene = GameManager.scene_slot[2];Remaining--;}
            }
            if (time > 0) time--;
            else player.kill();
        }
        return nextscene;
    }

    @Override
    public void onKey(short KeyCode) {
        switch (KeyCode) {
            case KeyEvent.VK_ESCAPE: {
                w.lock();
                GameScene.state.EscapeAction();
                w.unlock();
                break;
            }
            case KeyEvent.VK_F5: {
                if (GameScene.player == null) break;
                if (!GameScene.player.alive) break;
                GameScreen.getrealpers().change();
                break;
            }
            case KeyEvent.VK_1: {
                if (GameScene.state != State.POSE) break;
                if (player.alive) player.kill();
                GameScene.state.EscapeAction();
                break;
            }
            case KeyEvent.VK_2: {
                if (GameScene.state != State.POSE) break;
                nextscene = GameManager.scene_slot[0];
                GameScene.world = null;
                break;
            }
        }
    }

    //外部から中のprivateの操作
    public static void resetRemaining() {Remaining = default_Remaining;}
    public static int getTime() {return time;}
    public static void resetCourseid() {courseid = 0;}
    public static int getCourseid() {return courseid;}
    public static void clearCurrentCourse() {
        if (nextscene == null) courseid++;
        if (CourseSelector.existWorld(courseid)) nextscene = GameManager.scene_slot[2];
        else {nextscene = GameManager.scene_slot[3];GameManager.mk.dounlock();}
    }

    public enum State {
        GAME(0),
        TEXT(1),
        POSE(2);

        private int mode;
        private State(int mode) {
            this.mode = mode;
        }

        public void EscapeAction() {
            switch (mode) {
                case 0: {
                    GameScene.state = State.POSE;
                    GameManager.mk.dounlock();
                    break;
                }
                case 1: {
                    ViewTextScreen.free();
                    GameManager.mk.dolock();
                    break;
                }
                case 2: {
                    GameScene.state = State.GAME;
                    GameManager.mk.dolock();
                    break;
                }
            }
        }
    }
}
