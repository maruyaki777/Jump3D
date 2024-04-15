package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.Tool;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelTool implements Model{

    private Tool entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;

    public ModelTool(Tool entity) {
        this.entity = entity;
        WP_fpsbuffer = new FPSBufferStream<WorldPosition>(FPSBufferUtils::WP_calc, ()->{return this.entity.pos;}, GameManager.fps, new WorldPosition(entity.pos));
    }

    public void init() {}

    //描画対象の座標
    public WorldPosition setPosition() {
        return WP_fpsbuffer.read();
    }

    private Aspect asp = new Aspect(0, 0);

    //描画対象の角度
    public Aspect setAspect() {
        return this.asp;
    }

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        if (GameScene.state == GameScene.State.GAME) asp.add(10 * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f)), 0);
        return 1;
    }

    private int where;

    //次のCubeに移行
    public void nextCube() {where = 0;}

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "engineer");
    public Texture modelTex() {
        return texture;
    }

    //テクスチャの切り出す場所 [4][2]
    private int[][] texp = new int[][] {
        {26, 1},
        {26, 18},
        {43, 18},
        {43, 1}
    };
    private int[][] texp_no = new int[][] {
        {0, 0},
        {0, 0},
        {0, 0},
        {0, 0}
    };
    public int[][] nextFaceTexCut() {
        if (where == 0) {
            return texp;
        }
        else return texp_no;
    }

    //座標（そのエンティティの相対座標）[4][3]
    private double[][] posp = new double[][] {
        {-0.85, 0, -0.85},
        {-0.85, 0, 0.85},
        {0.85, 0, 0.85},
        {0.85, 0, -0.85}
    };
    public double[][] nextFacePos() {
        return posp;
    }
}
