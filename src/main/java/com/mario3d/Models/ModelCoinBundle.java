package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.CoinBundle;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelCoinBundle implements Model{
    private static final double[][][] corners = new double[][][] {
        {
        	{-0.900000, 0.000000, -0.900000},
        	{0.900000, 0.000000, -0.900000},
        	{-0.900000, 0.900000, -0.900000},
        	{0.900000, 0.900000, -0.900000},
        	{-0.900000, 0.000000, 0.900000},
        	{0.900000, 0.000000, 0.900000},
        	{-0.900000, 0.900000, 0.900000},
        	{0.900000, 0.900000, 0.900000}
        }
    };

    private static final int[][] face = new int[][] {
        {0, 2, 6, 4},//-x
        {0, 1, 5, 4},//-y
        {0, 1, 3, 2},//-z
        {1, 3, 7, 5},//x
        {2, 3, 7, 6},//y
        {4, 5, 7, 6}//z
    };

    private static final int[][][][] tex = ModelTexArray.set(
        new ModelCubeTex(0, 0, 18, 7, 18)
    );

    private final CoinBundle entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;

    public ModelCoinBundle(CoinBundle entity) {
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
        return asp;
    }

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        fi = -1;
        if (entity.getMode() == 2 && GameScene.state == GameScene.State.GAME) asp.add(16 * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f)), 0);
        return 1;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "coinbundle");
    public Texture modelTex() {return texture;}

    private int ti;
    private int fi;

    //テクスチャの切り出す場所 [4][2]
    public int[][] nextFaceTexCut() {return tex[fi][ti];}

    private double[][] result = new double[4][];

    //座標（そのエンティティの相対座標）[4][3]
    public double[][] nextFacePos() {
        for (int i = 0;i < 4;i++) {
            result[i] = corners[fi][face[ti][i]];
        }
        ti++;
        return result;
    }
}
