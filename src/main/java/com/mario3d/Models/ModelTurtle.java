package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.Turtle;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelDeath.InnerModelDeath;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelTurtle implements Model, InnerModelDeath{
    private double[][][] corners;
    private static final double[][][] corners_default = new double[][][] {
        {//左前脚
            {0.100000, 0.000000, 0.200000},
            {0.300000, 0.000000, 0.200000},
            {0.100000, 0.200000, 0.200000},
            {0.300000, 0.200000, 0.200000},
            {0.100000, 0.000000, 0.400000},
            {0.300000, 0.000000, 0.400000},
            {0.100000, 0.200000, 0.400000},
            {0.300000, 0.200000, 0.400000}
        },
        {//右前脚
            {-0.300000, 0.000000, 0.200000},
            {-0.100000, 0.000000, 0.200000},
            {-0.300000, 0.200000, 0.200000},
            {-0.100000, 0.200000, 0.200000},
            {-0.300000, 0.000000, 0.400000},
            {-0.100000, 0.000000, 0.400000},
            {-0.300000, 0.200000, 0.400000},
            {-0.100000, 0.200000, 0.400000}
        },
        {//左後脚
            {0.100000, 0.000000, -0.400000},
            {0.300000, 0.000000, -0.400000},
            {0.100000, 0.200000, -0.400000},
            {0.300000, 0.200000, -0.400000},
            {0.100000, 0.000000, -0.200000},
            {0.300000, 0.000000, -0.200000},
            {0.100000, 0.200000, -0.200000},
            {0.300000, 0.200000, -0.200000}
        },
        {//右後脚
            {-0.300000, 0.000000, -0.400000},
            {-0.100000, 0.000000, -0.400000},
            {-0.300000, 0.200000, -0.400000},
            {-0.100000, 0.200000, -0.400000},
            {-0.300000, 0.000000, -0.200000},
            {-0.100000, 0.000000, -0.200000},
            {-0.300000, 0.200000, -0.200000},
            {-0.100000, 0.200000, -0.200000}
        },
        {//頭
            {-0.400000, 0.400000, 0.300000},
            {0.400000, 0.400000, 0.300000},
            {-0.400000, 1.600000, 0.300000},
            {0.400000, 1.600000, 0.300000},
            {-0.400000, 0.400000, 0.800000},
            {0.400000, 0.400000, 0.800000},
            {-0.400000, 1.600000, 0.800000},
            {0.400000, 1.600000, 0.800000}
        },
        {//左耳
            {0.200000, 1.600000, 0.500000},
            {0.300000, 1.600000, 0.500000},
            {0.200000, 1.700000, 0.500000},
            {0.300000, 1.700000, 0.500000},
            {0.200000, 1.600000, 0.700000},
            {0.300000, 1.600000, 0.700000},
            {0.200000, 1.700000, 0.700000},
            {0.300000, 1.700000, 0.700000}
        },
        {//右耳
            {-0.300000, 1.600000, 0.500000},
            {-0.200000, 1.600000, 0.500000},
            {-0.300000, 1.700000, 0.500000},
            {-0.200000, 1.700000, 0.500000},
            {-0.300000, 1.600000, 0.700000},
            {-0.200000, 1.600000, 0.700000},
            {-0.300000, 1.700000, 0.700000},
            {-0.200000, 1.700000, 0.700000}
        },
        {//甲羅メイン
            {-0.500000, 0.200000, -0.500000},
            {0.500000, 0.200000, -0.500000},
            {-0.500000, 1.000000, -0.500000},
            {0.500000, 1.000000, -0.500000},
            {-0.500000, 0.200000, 0.500000},
            {0.500000, 0.200000, 0.500000},
            {-0.500000, 1.000000, 0.500000},
            {0.500000, 1.000000, 0.500000}
        },
        {//甲羅白
            {-0.600000, 0.550000, -0.600000},
            {0.600000, 0.550000, -0.600000},
            {-0.600000, 0.650000, -0.600000},
            {0.600000, 0.650000, -0.600000},
            {-0.600000, 0.550000, 0.600000},
            {0.600000, 0.550000, 0.600000},
            {-0.600000, 0.650000, 0.600000},
            {0.600000, 0.650000, 0.600000}
        }
    };

    private static final double[][][] corners_in = new double[][][] {
        {
            {-0.500000, 0.000000, -0.500000},
            {0.500000, 0.000000, -0.500000},
            {-0.500000, 0.800000, -0.500000},
            {0.500000, 0.800000, -0.500000},
            {-0.500000, 0.000000, 0.500000},
            {0.500000, 0.000000, 0.500000},
            {-0.500000, 0.800000, 0.500000},
            {0.500000, 0.800000, 0.500000}
        },
        {
            {-0.600000, 0.350000, -0.600000},
            {0.600000, 0.350000, -0.600000},
            {-0.600000, 0.450000, -0.600000},
            {0.600000, 0.450000, -0.600000},
            {-0.600000, 0.350000, 0.600000},
            {0.600000, 0.350000, 0.600000},
            {-0.600000, 0.450000, 0.600000},
            {0.600000, 0.450000, 0.600000}
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

    private int[][][][] tex;
    private static final int[][][][] tex_default = ModelTexArray.set(
        new ModelCubeTex(29, 19, 2, 2, 2),
        new ModelCubeTex(29, 19, 2, 2, 2),
        new ModelCubeTex(29, 19, 2, 2, 2),
        new ModelCubeTex(29, 19, 2, 2, 2),
        new ModelCubeTex(0, 18, 8, 12, 5),
        new ModelCubeTex(30, 32, 1, 1, 2),
        new ModelCubeTex(30, 32, 1, 1, 2),
        new ModelCubeTex(0, 0, 10, 8, 10),
        new ModelCubeTex(0, 35, 12, 1, 12)
    );
    private static final int[][][][] tex_in = ModelTexArray.set(
        new ModelCubeTex(0, 0, 10, 8, 10),
        new ModelCubeTex(0, 35, 12, 1, 12)
    );

    private final Turtle entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;
    private FPSBufferStream<Aspect> Asp_fpsbuffer;
    
    public ModelTurtle(Turtle entity) {
        this.entity = entity;
        this.mode = 0;
        innerAspect = new Aspect(0, 0);
        WP_fpsbuffer = new FPSBufferStream<WorldPosition>(FPSBufferUtils::WP_calc, ()->{return this.entity.pos;}, GameManager.fps, new WorldPosition(entity.pos));
        Asp_fpsbuffer = new FPSBufferStream<Aspect>(FPSBufferUtils::Asp_calc, ()->{return this.entity.aspect;}, GameManager.fps, new Aspect(entity.aspect));
    }
    public boolean deathMotionEnable() {
        if (mode == 0) return true;
        else return false;
    }
    public void init() {}

    //描画対象の座標
    public WorldPosition setPosition() {
        return WP_fpsbuffer.read();
    }
    //描画対象の角度
    public Aspect setAspect() {
        if (mode == 0) return Asp_fpsbuffer.read();
        else {
            if (mode == 2 && GameScene.state == GameScene.State.GAME) innerAspect.add(16 * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f)), 0);
            return innerAspect;
        }
    }

    private int mode;
    private Aspect innerAspect;
    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        fi = -1;
        if (entity.getmode() != this.mode) innerAspect = new Aspect(entity.aspect);
        if ((this.mode = entity.getmode()) == 0) {corners = corners_default; tex = tex_default;}
        else {corners = corners_in; tex = tex_in;}
        return corners.length;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "turtle");
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
