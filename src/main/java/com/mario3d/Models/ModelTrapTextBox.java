package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.CubeEntity.TrapMessageBox;
import com.mario3d.Displays.Textures;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelTrapTextBox implements Model{
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
        new ModelCubeTex(0, 0, 16, 16, 16)
    );

        private final TrapMessageBox entity;

    public ModelTrapTextBox(TrapMessageBox entity) {
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
        return 1;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "messagebox");
    private static Texture texture_trap = Textures.Loader.getTexture("entity", "trapmessagebox");
    public Texture modelTex() {
        if (entity.touched) return texture_trap;
        return texture;
    }

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
