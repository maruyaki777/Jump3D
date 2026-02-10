package com.mario3d.Displays;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.CubeDisplay.*;
import com.mario3d.CubeDisplay.CubeDisplayData;
import com.mario3d.CubeDisplay.CubeDisplayData.SectionData;
import com.mario3d.Cubes.Cube;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.Player;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;
import com.mario3d.WorldSystem.World.World;

import com.mario3d.Models.Model;
import com.mario3d.Models.ModelDeath;
import com.mario3d.Scenes.GameScene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//エンティティモデルへの描画情報の要請とブロックの描写を行う
//プレイヤーの向いてる方向に合わせるように起点の角度を変える
public class GameScreen implements Window{

    public static WorldPosition playerpos;
    public static WorldPosition playerlook = new WorldPosition(0, 0, 0);
    public static List<Model> models = new CopyOnWriteArrayList<>();
    public static List<ModelDeath> deathmodels = new CopyOnWriteArrayList<>();
    private static Perspective persmode;
    public static double perpos;
    private static final double PLAYER_LOOK_HEIGHT = 1.5;

    public enum Perspective {
        One(0),
        Third1(1),
        Third2(2);
        private final int mode;
        private Perspective(int i) {this.mode = i;}
        public void set(GL2 gl) {
            switch (this.mode) {
                case 0: {
                    GameManager.display.display3D(gl);
                    break;
                }
                case 1: {
                    GameManager.display.display3D(gl, 0, 0, -perpos, 0, 0, 0);
                    break;
                }
                case 2: {
                    GameManager.display.display3D(gl, 0, 0, perpos, 0, 0, 0);
                }
            }
        }

        public void setviewPoint(WorldPosition pos, Aspect asp) {
            //プレイヤーの視点の設定
            switch (this.mode) {
                case 0: {
                    break;
                }
                case 1: {
                    double vectorY = Math.sin(Math.toRadians(asp.y));
                    double len = Math.cos(Math.toRadians(asp.y)) * perpos;
                    double vectorX = Math.sin(Math.toRadians(-asp.x));
                    double vectorZ = Math.cos(Math.toRadians(asp.x));
                    pos.x -= vectorX * len;
                    pos.y -= vectorY * perpos;
                    pos.z -= vectorZ * len;
                    break;
                }
                case 2: {
                    double vectorY = Math.sin(Math.toRadians(asp.y));
                    double len = Math.cos(Math.toRadians(asp.y)) * perpos;
                    double vectorX = Math.sin(Math.toRadians(-asp.x));
                    double vectorZ = Math.cos(Math.toRadians(asp.x));
                    pos.x += vectorX * len;
                    pos.y += vectorY * perpos;
                    pos.z += vectorZ * len;
                    break;
                }
            }
        }

        public void change() {
            switch (this.mode) {
                case 0: {
                    persmode = Perspective.Third1;
                    break;
                }
                case 1: {
                    persmode = Perspective.Third2;
                    break;
                }
                case 2: {
                    persmode = Perspective.One;
                    break;
                }
            }
        }
    }

    public GameScreen() {
        playerpos = new WorldPosition(0, 0, 0);
        persmode = Perspective.One;
        perpos = 1.0;
    }

    private FPSBufferStream<WorldPosition> WP_fpsbuffer;
    private static boolean bufferloaded = false;
    public static void needload() {bufferloaded = false;}

    private static Perspective pers;

    public static Perspective getpers() {return pers;}
    public static Perspective getrealpers() {return persmode;}

    @Override
    public void display(GL2 gl) {
        World temp_World = GameScene.world;
        Player temp_Player = GameScene.player;
        if (temp_World == null || temp_Player == null) return;
        if (!bufferloaded) {
            WP_fpsbuffer = new FPSBufferStream<WorldPosition>(FPSBufferUtils::WP_calc, ()->{return new WorldPosition(GameScene.player.pos);}, GameManager.fps, new WorldPosition(0, 0, 0));
            bufferloaded = true;
        }
        GameManager.display.display2D(gl);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor3f(temp_World.skycolor.getRed() / 255f, temp_World.skycolor.getGreen() / 255f, temp_World.skycolor.getBlue() / 255f);
        gl.glVertex2f(-1.0f, -1.0f);
        gl.glVertex2f(-1.0f, 1.0f);
        gl.glVertex2f(1.0f, 1.0f);
        gl.glVertex2f(1.0f, -1.0f);
        gl.glEnd();
        if (temp_Player.alive && temp_Player.getGoalBoolean() == false) pers = persmode;
        else pers = Perspective.Third1;
        pers.set(gl);
        WP_fpsbuffer.setLastFPS(GameManager.display.getAverageFPS());
        playerpos = WP_fpsbuffer.read();
        playerlook.x = playerpos.x; playerlook.y = playerpos.y + PLAYER_LOOK_HEIGHT; playerlook.z = playerpos.z;
        Aspect asp;
        if (temp_Player.alive && temp_Player.getGoalBoolean() == false) asp = new Aspect(temp_Player.pasp);
        else {
            asp = new Aspect(temp_Player.aspect);
            asp.y = -45;
        }
        //GameScene.player.aspect.add(1, 0);
        gl.glRotated(asp.x, 0, 1, 0);
        gl.glRotated(asp.y, Math.cos(Math.toRadians(asp.x)), 0, Math.sin(Math.toRadians(asp.x)));

        pers.setviewPoint(playerlook, asp);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        blockview(gl, temp_World);
        modelview(gl);
        gl.glDisable(GL2.GL_DEPTH_TEST);
        gl.glDisable(GL2.GL_BLEND);
        gl.glRotated(-asp.y, Math.cos(Math.toRadians(asp.x)), 0, Math.sin(Math.toRadians(asp.x)));
        gl.glRotated(asp.x, 0, -1, 0);
    }

    private void blockview(GL2 gl, World world) {
        SectionData sd = new SectionData();
        for (Cube c : world.cubes) {
            CubeDisplayData cdd = c.displayType.cdd;
            int time = cdd.sectiontime(c);
            for (int i = 0;i < time;i++) {
                if (playerlook.x <= c.BasePos.x) blockview(gl, cdd.section(sd, 0),  0);
                else if (playerlook.x >= c.BasePos.x + c.dx) blockview(gl, cdd.section(sd, 3), 3);
                if (playerlook.y <= c.BasePos.y ) blockview(gl, cdd.section(sd, 1), 1);
                else if (playerlook.y >= c.BasePos.y + c.dy) blockview(gl, cdd.section(sd, 4), 4);
                if (playerlook.z <= c.BasePos.z) blockview(gl, cdd.section(sd, 2), 2);
                else if (playerlook.z >= c.BasePos.z + c.dz) blockview(gl, cdd.section(sd, 5), 5);
                cdd.nextSection();
            }
        }
    }

    public static enum CubeDisplayType {
        COMMON(0, new Common()),
        GRASS(1, new Grass());
        public final CubeDisplayData cdd;
        private CubeDisplayType(int type, CubeDisplayData cdd) {
            this.cdd = cdd;
        }
    }

    private WorldPosition wp1 = new WorldPosition(0, 0, 0);
    private WorldPosition wp2 = new WorldPosition(0, 0, 0);
    private void blockview(GL2 gl, SectionData sd, int facenum) {
        Texture t = sd.getTex(facenum);
        t.enable(gl);
        t.bind(gl);
        t.setTexParameterf(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        t.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER,GL2.GL_NEAREST_MIPMAP_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_LOD_BIAS, 1.0f);
        wp1.x = -playerpos.x;
        wp1.y = -playerpos.y - PLAYER_LOOK_HEIGHT;
        wp1.z = -playerpos.z;
        wp2.x = -playerpos.x;
        wp2.y = -playerpos.y - PLAYER_LOOK_HEIGHT;
        wp2.z = -playerpos.z;
        float wide = (float)sd.texture_wide;
        switch (facenum % 3) {
            case 0: {
                double x = -playerpos.x;
                if (facenum == 0) x += sd.basepos[0]; else x += sd.basepos[0] + sd.dpos[0];
                wp1.y += sd.basepos[1]; wp2.y += sd.basepos[1] + sd.dpos[1];
                wp1.z += sd.basepos[2]; wp2.z += sd.basepos[2] + sd.dpos[2];
                float ty = (float)((wp2.y - wp1.y) / wide), tx = (float)((wp2.z - wp1.z) / wide);
                gl.glBegin(GL2.GL_POLYGON);
                gl.glColor4f(1f, 1f, 1f, 1f);
                gl.glTexCoord2f(0, 0);
                gl.glVertex3d(x, wp1.y, wp1.z);
                gl.glTexCoord2f(0, ty);
                gl.glVertex3d(x, wp2.y, wp1.z);
                gl.glTexCoord2f(tx, ty);
                gl.glVertex3d(x, wp2.y, wp2.z);
                gl.glTexCoord2f(tx, 0);
                gl.glVertex3d(x, wp1.y, wp2.z);
                gl.glEnd();
                break;
            }
            case 1: {
                double y = -playerpos.y - PLAYER_LOOK_HEIGHT;
                if (facenum == 1) y += sd.basepos[1]; else y += sd.basepos[1] + sd.dpos[1];
                wp1.x += sd.basepos[0]; wp2.x += sd.basepos[0] + sd.dpos[0];
                wp1.z += sd.basepos[2]; wp2.z += sd.basepos[2] + sd.dpos[2];
                float ty = (float)((wp2.z - wp1.z) / wide), tx = (float)((wp2.x - wp1.x) / wide);
                gl.glBegin(GL2.GL_POLYGON);
                gl.glColor4f(1f, 1f, 1f, 1f);
                gl.glTexCoord2f(0, 0);
                gl.glVertex3d(wp1.x, y, wp1.z);
                gl.glTexCoord2f(0, ty);
                gl.glVertex3d(wp1.x, y, wp2.z);
                gl.glTexCoord2f(tx, ty);
                gl.glVertex3d(wp2.x, y, wp2.z);
                gl.glTexCoord2f(tx, 0);
                gl.glVertex3d(wp2.x, y, wp1.z);
                gl.glEnd();
                break;
            }
            case 2: {
                double z = -playerpos.z;
                if (facenum == 2) z += sd.basepos[2]; else z += sd.basepos[2] + sd.dpos[2];
                wp1.x += sd.basepos[0]; wp2.x += sd.basepos[0] + sd.dpos[0];
                wp1.y += sd.basepos[1]; wp2.y += sd.basepos[1] + sd.dpos[1];
                float ty = (float)((wp2.y - wp1.y) / wide), tx = (float)((wp2.x - wp1.x) / wide);
                gl.glBegin(GL2.GL_POLYGON);
                gl.glColor4f(1f, 1f, 1f, 1f);
                gl.glTexCoord2f(0, 0);
                gl.glVertex3d(wp1.x, wp1.y, z);
                gl.glTexCoord2f(0, ty);
                gl.glVertex3d(wp1.x, wp2.y, z);
                gl.glTexCoord2f(tx, ty);
                gl.glVertex3d(wp2.x, wp2.y, z);
                gl.glTexCoord2f(tx, 0);
                gl.glVertex3d(wp2.x, wp1.y, z);
                gl.glEnd();
                break;
            }
        }
        t.disable(gl);
        return;
    }

    //モデルの描写は起点をずらして行う
    private float[][] f = new float[4][2];
    private void modelview(GL2 gl) {
        for (Model model : GameScreen.models) {
        	double[] modelcolor = model.color();
            modelview(gl, model, modelcolor[0], modelcolor[1], modelcolor[2], 1);
        }
        for (ModelDeath model : GameScreen.deathmodels) {
            modelview(gl, model.getThisModel(), model.red, model.green, model.blue, model.fadeout());
        }
    }

    private void modelview(GL2 gl, Model model, double r, double g, double b, double alpha) {
        RelativePosition cubepos;
        Aspect cubeasp;
        WorldPosition pos = model.setPosition();
        Aspect asp = model.setAspect();
        double ax = asp.x, ay = asp.y;
        double tx = pos.x - playerpos.x,   ty = pos.y - playerpos.y - PLAYER_LOOK_HEIGHT,   tz = pos.z - playerpos.z;
        int len = model.getCubeCount();
        if (len == 0) return;
        gl.glTranslated(tx, ty, tz);
        gl.glRotated(ax, 0, 1, 0);
        //gl.glRotated(-ay, Math.cos(Math.toRadians(ax)), 0, Math.sin(Math.toRadians(ax)));
        gl.glRotated(-ay, 1, 0, 0);
        Texture t = model.modelTex();
        int w = t.getWidth(); int h = t.getHeight();
        t.setTexParameterf(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        t.enable(gl);
        t.bind(gl);
        for (int i = 0; i < len;i++) {
            model.nextCube();
            boolean changecubepos = false;
            double acx = 0, acy = 0;
            //Cubeごとの回転
            if ((cubepos = model.setCubePos()) != null && (cubeasp = model.setCubeAspect()) != null) {
                acx = cubeasp.x; acy = cubeasp.y;
                changecubepos = true;
                gl.glTranslated(cubepos.x, cubepos.y, cubepos.z);
                gl.glRotated(acx, 0, 1, 0);
                gl.glRotated(acy, Math.cos(Math.toRadians(acx)), 0, Math.sin(Math.toRadians(acx)));
            };
            gl.glColor4d(r, g, b, alpha);
            gl.glBegin(GL2.GL_QUADS);
            for (int j = 0;j < 6;j++) {
                int[][] fi = model.nextFaceTexCut();
                for (int l = 0; l < f.length;l++) {
                    f[l][0] = (float)fi[l][0] / w;
                    f[l][1] = (float)fi[l][1] / h;
                }
                double[][] points = model.nextFacePos();
                modelview(gl, points, f);
            }
            gl.glEnd();
            if (changecubepos) {
                gl.glRotated(-acy, Math.cos(Math.toRadians(acx)), 0, Math.sin(Math.toRadians(acx)));
                gl.glRotated(acx, 0, -1, 0);
                gl.glTranslated(-cubepos.x, -cubepos.y, -cubepos.z);
            }
        }
        t.disable(gl);
        //gl.glRotated(ay, Math.cos(Math.toRadians(ax)), 0, Math.sin(Math.toRadians(ax)));
        gl.glRotated(ay, 1, 0, 0);
        gl.glRotated(ax, 0, -1, 0);
        gl.glTranslated(-tx, -ty, -tz);
    }

    private void modelview(GL2 gl, double[][] points, float[][] cut) {
        for (int i = 0;i < points.length;i++) {
            gl.glTexCoord2fv(cut[i], 0);
            gl.glVertex3dv(points[i], 0);
        }
    }
}
