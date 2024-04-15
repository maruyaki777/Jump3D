package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.Faller;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelDeath.InnerModelDeath;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelFaller implements Model, InnerModelDeath{
    private static final double[][][] corners = new double[][][] {
        {//胴体
            {-2.000000, 0.200000, -2.000000},
            {2.000000, 0.200000, -2.000000},
            {-2.000000, 4.200000, -2.000000},
            {2.000000, 4.200000, -2.000000},
            {-2.000000, 0.200000, 2.000000},
            {2.000000, 0.200000, 2.000000},
            {-2.000000, 4.200000, 2.000000},
            {2.000000, 4.200000, 2.000000}
        },
        {//胴体の周りの奴
            {-2.200000, 0.000000, -1.800000},
            {2.200000, 0.000000, -1.800000},
            {-2.200000, 4.400000, -1.800000},
            {2.200000, 4.400000, -1.800000},
            {-2.200000, 0.000000, 1.800000},
            {2.200000, 0.000000, 1.800000},
            {-2.200000, 4.400000, 1.800000},
            {2.200000, 4.400000, 1.800000}
        },
        {//胴体に巻いてるもの
            {-2.100000, 2.700000, -2.100000},
            {2.100000, 2.700000, -2.100000},
            {-2.100000, 3.700000, -2.100000},
            {2.100000, 3.700000, -2.100000},
            {-2.100000, 2.700000, 2.100000},
            {2.100000, 2.700000, 2.100000},
            {-2.100000, 3.700000, 2.100000},
            {2.100000, 3.700000, 2.100000}
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
        new ModelCubeTex(0, 26, 16, 16, 16),
        new ModelCubeTex(0, 0, 12, 12, 8),
        new ModelCubeTex(51, 0, 18, 4, 18)
    );

    private final Faller entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;
    private FPSBufferStream<Aspect> Asp_fpsbuffer;
    
    public ModelFaller(Faller entity) {
        this.entity = entity;
        WP_fpsbuffer = new FPSBufferStream<WorldPosition>(FPSBufferUtils::WP_calc, ()->{return this.entity.pos;}, GameManager.fps, new WorldPosition(entity.pos));
        Asp_fpsbuffer = new FPSBufferStream<Aspect>(FPSBufferUtils::Asp_calc, ()->{return this.entity.aspect;}, GameManager.fps, new Aspect(entity.aspect));
    }

    public void init() {}

        //描画対象の座標
    public WorldPosition setPosition() {
        return WP_fpsbuffer.read();
    }

    //描画対象の角度
    public Aspect setAspect() {
        return Asp_fpsbuffer.read();
    }

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        fi = -1;
        return 3;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "faller");
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
