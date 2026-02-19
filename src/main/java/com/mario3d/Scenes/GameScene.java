package com.mario3d.Scenes;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

import com.jogamp.newt.event.KeyEvent;
import com.mario3d.GameManager;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Displays.ViewTextScreen;
import com.mario3d.Entities.Player;
import com.mario3d.Events.GameEvent;
import com.mario3d.Profile.Profile;
import com.mario3d.Profile.ProfileManager;
import com.mario3d.WorldSystem.WorldPosition;
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
    public static final int default_time = 500;
    private static int time;
    
    private static Profile profile = null;

    private static Scene nextscene;
    private static int changetime;

    public GameScene() {}

    @Override
    public void init() {
        GameScene.world = CourseSelector.selector(profile.courseid);
        state = State.GAME;
        GameManager.mk.dolock();
        nextscene = null;
        changetime = 0;
        time = GameScene.world.world_time * 40;
        if (profile.checkpoint != null) player.pos = new WorldPosition(profile.checkpoint.pos);//チェックポイント
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
                if (changetime > 120) {nextscene = GameManager.scene_slot[2];countdeath();}
            }
            if (time > 0) time--;
            else player.kill();
            profile.timeplayed++;
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
                if (!player.alive) countdeath();
                if (ProfileManager.existFile()) ProfileManager.save(profile);
                need_reset = true;
                break;
            }
        }
    }
    
    private static boolean need_reset = false;
    
    @Override
    public void finish() {
    	if (need_reset) {
    		GameScene.world = null;
    		need_reset = false;
    	}
    }
    
    public static void countdeath() {
    	profile.remaining--;
    	profile.deathtime++;
    }

    //外部から中のprivateの操作
    public static void resetRemaining() {profile.remaining = default_Remaining;}
    public static int getRemaining() {return profile.remaining;}
    public static int getTime() {return time;}
    public static void resetCourseid() {profile.courseid = 0;profile.checkpoint = null;}
    public static int getCourseid() {return profile.courseid;}
    public static long getPlayTime() {return profile.timeplayed;}
    public static void resetProfile() {
    	profile = ProfileManager.load();
    	if (profile == null) profile = new Profile(default_Remaining, 0);
    }
    public static void clearCurrentCourse() {
        if (nextscene == null) profile.courseid++;
        if (CourseSelector.existWorld(profile.courseid)) nextscene = GameManager.scene_slot[2];
        else {nextscene = GameManager.scene_slot[3];GameManager.mk.dounlock();ProfileManager.outputResult(profile);ProfileManager.reset();GameScene.need_reset = true;}
        profile.checkpoint = null;
    }
    public static void setCheckPoint(WorldPosition pos, int priority) {
    	if (profile.checkpoint == null) profile.checkpoint = new CheckPoint(new WorldPosition(pos), priority);
    	else if (profile.checkpoint.priority <= priority) profile.checkpoint = new CheckPoint(new WorldPosition(pos), priority);
    }
    public static int getCheckPointPriority() {if (profile.checkpoint != null) return profile.checkpoint.priority; else return Integer.MIN_VALUE;}

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
    
    public static class CheckPoint{
    	public final WorldPosition pos;
    	public final int priority;
    	
    	public CheckPoint(WorldPosition pos, int priority) {
    		this.pos = pos;
    		this.priority = priority;
    	}
    }
}
