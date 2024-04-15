package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.CubeEntity.Goal;
import com.mario3d.Displays.Textures;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelGoal implements Model{
    private static final double[][][] corners = new double[][][] {
        {
            {-0.500000, 0.000000, -0.500000},
            {0.500000, 0.000000, -0.500000},
            {-0.500000, 1.000000, -0.500000},
            {0.500000, 1.000000, -0.500000},
            {-0.500000, 0.000000, 0.500000}, 
            {0.500000, 0.000000, 0.500000},
            {-0.500000, 1.000000, 0.500000},
            {0.500000, 1.000000, 0.500000}
        },
        {
            {-0.550000, 0.600000, -0.550000},
            {0.550000, 0.600000, -0.550000},
            {-0.550000, 1.150000, -0.550000},
            {0.550000, 1.150000, -0.550000},
            {-0.550000, 0.600000, 0.550000},
            {0.550000, 0.600000, 0.550000},
            {-0.550000, 1.150000, 0.550000},
            {0.550000, 1.150000, 0.550000}
        },
        {
            {-0.100000, 1.000000, -0.100000},
            {0.100000, 1.000000, -0.100000},
            {-0.100000, 7.200000, -0.100000},
            {0.100000, 7.200000, -0.100000},
            {-0.100000, 1.000000, 0.100000},
            {0.100000, 1.000000, 0.100000},
            {-0.100000, 7.200000, 0.100000},
            {0.100000, 7.200000, 0.100000}
        },
        {
            {-0.100000, 7.200000, -0.100000},
            {0.100000, 7.200000, -0.100000},
            {-0.100000, 13.400000, -0.100000},
            {0.100000, 13.400000, -0.100000},
            {-0.100000, 7.200000, 0.100000},
            {0.100000, 7.200000, 0.100000},
            {-0.100000, 13.400000, 0.100000},
            {0.100000, 13.400000, 0.100000}
        },
        {
            {-0.150000, 13.250000, -0.150000},
            {0.150000, 13.250000, -0.150000},
            {-0.150000, 13.550000, -0.150000},
            {0.150000, 13.550000, -0.150000},
            {-0.150000, 13.250000, 0.150000},
            {0.150000, 13.250000, 0.150000},
            {-0.150000, 13.550000, 0.150000},
            {0.150000, 13.550000, 0.150000}
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
        new ModelCubeTex(0, 0, 8, 8, 8),
        new ModelCubeTex(0, 22, 8, 4, 8),
        new ModelCubeTex(47, 0, 2, 62, 2),
        new ModelCubeTex(47, 0, 2, 62, 2),
        new ModelCubeTex(1, 50, 6, 6, 6)
    );

    private static final double[] upinvisible = new double[] {0, 0, 0};

    private final Goal entity;

    public ModelGoal(Goal entity) {
        this.entity = entity;
    }

    public void init() {}

    //描画対象の座標
    public WorldPosition setPosition() {
        return entity.pos;
    }

    private final Aspect asp = new Aspect(0, 0);
    //描画対象の角度
    public Aspect setAspect() {
        return asp;
    }

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        fi = -1;
        return 5;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "goal");
    public Texture modelTex() {return texture;}

    private int ti;
    private int fi;

    //テクスチャの切り出す場所 [4][2]
    public int[][] nextFaceTexCut() {return tex[fi][ti];}

    private double[][] result = new double[4][];

    //座標（そのエンティティの相対座標）[4][3]
    public double[][] nextFacePos() {
        if (fi == 1 && ti == 4) {
            for (int i = 0;i < 4;i++) result[i] = upinvisible;
        }
        else for (int i = 0;i < 4;i++) {
            result[i] = corners[fi][face[ti][i]];
        }
        ti++;
        return result;
    }
}
