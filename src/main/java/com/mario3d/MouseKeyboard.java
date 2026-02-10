package com.mario3d;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Scenes.KeyAction;

public class MouseKeyboard implements MouseListener, KeyListener{
    private final GLWindow glWindow;

    public MouseKeyboard(GLWindow glWindow) {
        this.glWindow = glWindow;
    }
    //キーボード関係
    @Override
    public void keyPressed(KeyEvent e) {
        if ((InputEvent.AUTOREPEAT_MASK & e.getModifiers()) == 0) {
            KeyAction k = GameManager.keyaction;
            if (k != null) k.onKey(e.getKeyCode());
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W: {K_w = true;break;}
                case KeyEvent.VK_A: {K_a = true;break;}
                case KeyEvent.VK_S: {K_s = true;break;}
                case KeyEvent.VK_D: {K_d = true;break;}
                case KeyEvent.VK_SPACE: {K_space = true;break;}
                case KeyEvent.VK_F12: {GameManager.sysexit();break;}
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if ((InputEvent.AUTOREPEAT_MASK & e.getModifiers()) == 0) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W: {K_w = false;break;}
                case KeyEvent.VK_A: {K_a = false;break;}
                case KeyEvent.VK_S: {K_s = false;break;}
                case KeyEvent.VK_D: {K_d = false;break;}
                case KeyEvent.VK_SPACE: {K_space = false;break;}
            }
        }
    }

    //ゲームで使うキー
    private boolean K_w = false;
    private boolean K_a = false;
    private boolean K_s = false;
    private boolean K_d = false;
    private boolean K_space = false;

    public boolean getKeyStateVK_W() {return K_w;}
    public boolean getKeyStateVK_A() {return K_a;}
    public boolean getKeyStateVK_S() {return K_s;}
    public boolean getKeyStateVK_D() {return K_d;}
    public boolean getKeyStateVK_Space() {return K_space;}


    //マウス関係
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    
    public boolean lock = false;

    @Override
    public void mouseMoved(MouseEvent e) {
        if (GameManager.display == null) return;
        if (GameScene.state == GameScene.State.GAME) {
            double x = ((double)e.getX() - GameManager.display.width / 2) / 5;
            double y = ((double)e.getY() - GameManager.display.height / 2) / 5;
            //System.out.printf("%d  %d\n", e.getX() - GameManager.display.width / 2, e.getY() - GameManager.display.height / 2);
            GameScene.player.pasp.add(x, -y);
        }
        if (lock) glWindow.warpPointer(GameManager.display.width/2, GameManager.display.height/2);
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        if (lock) {
            GameScreen.perpos += e.getRotation()[1] * -0.5;
            if (GameScreen.perpos < 1.0) GameScreen.perpos = 1.0;
            else if (GameScreen.perpos > 5.0) GameScreen.perpos = 5.0;
        }
    }



    public void dolock() {
        lock = true;
        glWindow.setPointerVisible(false);
        glWindow.warpPointer(GameManager.display.width/2, GameManager.display.height/2);
    }

    public void dounlock() {
        lock = false;
        glWindow.setPointerVisible(true);
    }
}
