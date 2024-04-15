package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.Daruma;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.WorldSystem.WorldPosition;
import com.mario3d.Models.ModelDeath.InnerModelDeath;

public class ModelDaruma implements Model, InnerModelDeath{
    private static final double[][][] corners = new double[][][] {
        {//胴体
            {-0.500000, 0.200000, -0.350000},
            {0.500000, 0.200000, -0.350000},
            {-0.500000, 0.900000, -0.350000},
            {0.500000, 0.900000, -0.350000},
            {-0.500000, 0.200000, 0.350000},
            {0.500000, 0.200000, 0.350000},
            {-0.500000, 0.900000, 0.350000},
            {0.500000, 0.900000, 0.350000}
        },
        {//左足
            {0.200000, 0.000000, -0.100000},
            {0.400000, 0.000000, -0.100000},
            {0.200000, 0.200000, -0.100000},
            {0.400000, 0.200000, -0.100000},
            {0.200000, 0.000000, 0.100000},
            {0.400000, 0.000000, 0.100000},
            {0.200000, 0.200000, 0.100000},
            {0.400000, 0.200000, 0.100000}
        },
        {//右足
            {-0.400000, 0.000000, -0.100000},
            {-0.200000, 0.000000, -0.100000},
            {-0.400000, 0.200000, -0.100000},
            {-0.200000, 0.200000, -0.100000},
            {-0.400000, 0.000000, 0.100000},
            {-0.200000, 0.000000, 0.100000},
            {-0.400000, 0.200000, 0.100000},
            {-0.200000, 0.200000, 0.100000}
        },
        {//頭
            {-0.400000, 0.900000, -0.250000},
            {0.400000, 0.900000, -0.250000},
            {-0.400000, 1.300000, -0.250000},
            {0.400000, 1.300000, -0.250000},
            {-0.400000, 0.900000, 0.250000},
            {0.400000, 0.900000, 0.250000},
            {-0.400000, 1.300000, 0.250000},
            {0.400000, 1.300000, 0.250000}
        },
        {//左耳
            {0.200000, 1.300000, -0.100000},
            {0.300000, 1.300000, -0.100000},
            {0.200000, 1.400000, -0.100000},
            {0.300000, 1.400000, -0.100000},
            {0.200000, 1.300000, 0.100000},
            {0.300000, 1.300000, 0.100000},
            {0.200000, 1.400000, 0.100000},
            {0.300000, 1.400000, 0.100000}
        },
        {//右耳
            {-0.300000, 1.300000, -0.100000},
            {-0.200000, 1.300000, -0.100000},
            {-0.300000, 1.400000, -0.100000},
            {-0.200000, 1.400000, -0.100000},
            {-0.300000, 1.300000, 0.100000},
            {-0.200000, 1.300000, 0.100000},
            {-0.300000, 1.400000, 0.100000},
            {-0.200000, 1.400000, 0.100000}
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
        new ModelCubeTex(4, 29, 10, 7, 7),
        new ModelCubeTex(8, 20, 2, 2, 2),
        new ModelCubeTex(8, 20, 2, 2, 2),
        new ModelCubeTex(8, 48, 8, 4, 5),
        new ModelCubeTex(20, 20, 1, 1, 2),
        new ModelCubeTex(20, 20, 1, 1, 2)
    );

    private final Daruma entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;
    private FPSBufferStream<Aspect> Asp_fpsbuffer;

    public ModelDaruma(Daruma entity) {
        this.entity = entity;
        WP_fpsbuffer = new FPSBufferStream<WorldPosition>(FPSBufferUtils::WP_calc, ()->{return this.entity.pos;}, GameManager.fps, new WorldPosition(entity.pos));
        Asp_fpsbuffer = new FPSBufferStream<Aspect>(FPSBufferUtils::Asp_calc, ()->{return this.entity.aspect;}, GameManager.fps, new Aspect(entity.aspect));
    }

    public void init() {}

    //描画対象の座標
    public WorldPosition setPosition() {
        //return entity.pos;
        return WP_fpsbuffer.read();
    }

    //描画対象の角度
    public Aspect setAspect() {
        //return entity.aspect;
        return Asp_fpsbuffer.read();
    }

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        fi = -1;
        WP_fpsbuffer.setLastFPS(GameManager.fps);
        Asp_fpsbuffer.setLastFPS(GameManager.fps);
        return 6;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "daruma");
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
