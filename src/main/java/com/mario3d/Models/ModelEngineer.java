package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.Engineer;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelDeath.InnerModelDeath;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelEngineer implements Model, InnerModelDeath{
    private static final double[][][] corners = new double[][][] {
        {//体
            {-0.400000, 0.000000, -0.300000},
            {0.400000, 0.000000, -0.300000},
            {-0.400000, 1.400000, -0.300000},
            {0.400000, 1.400000, -0.300000},
            {-0.400000, 0.000000, 0.300000},
            {0.400000, 0.000000, 0.300000},
            {-0.400000, 1.400000, 0.300000},
            {0.400000, 1.400000, 0.300000}
        },
        {//帽子
            {-0.500000, 1.100000, -0.400000},
            {0.500000, 1.100000, -0.400000},
            {-0.500000, 1.200000, -0.400000},
            {0.500000, 1.200000, -0.400000},
            {-0.500000, 1.100000, 0.400000},
            {0.500000, 1.100000, 0.400000},
            {-0.500000, 1.200000, 0.400000},
            {0.500000, 1.200000, 0.400000}
        },
        {//左手
            {0.400000, 0.500000, -0.200000},
            {0.800000, 0.500000, -0.200000},
            {0.400000, 0.900000, -0.200000},
            {0.800000, 0.900000, -0.200000},
            {0.400000, 0.500000, 0.200000},
            {0.800000, 0.500000, 0.200000},
            {0.400000, 0.900000, 0.200000},
            {0.800000, 0.900000, 0.200000}
        },
        {//右手
            {-0.800000, 0.500000, -0.200000},
            {-0.400000, 0.500000, -0.200000},
            {-0.800000, 0.900000, -0.200000},
            {-0.400000, 0.900000, -0.200000},
            {-0.800000, 0.500000, 0.200000},
            {-0.400000, 0.500000, 0.200000},
            {-0.800000, 0.900000, 0.200000},
            {-0.400000, 0.900000, 0.200000}
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
        new ModelCubeTex(0, 44, 8, 14, 6),
        new ModelCubeTex(0, 30, 10, 1, 8),
        new ModelCubeTex(3, 15, 4, 4, 4),
        new ModelCubeTex(3, 15, 4, 4, 4)
    );

    private final Engineer entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;
    private FPSBufferStream<Aspect> Asp_fpsbuffer;

    public ModelEngineer(Engineer entity) {
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
        WP_fpsbuffer.setLastFPS(GameManager.display.getFPS());
        Asp_fpsbuffer.setLastFPS(GameManager.display.getFPS());
        return 4;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "engineer");
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
