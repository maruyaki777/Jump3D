package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Displays.Textures;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.WorldSystem.WorldPosition;

public class test implements Model{

    public double[][] a = new double[][] {
        /*{-1, -1, -1},
        {-1, -1, 1},
        {-1, 1, -1},
        {-1, 1, 1},
        {1, -1, -1},
        {1, -1, 1},
        {1, 1, -1},
        {1, 1, 1}*/
        { 0.0f, 0.0f, 0.0f }, /* A */
            { 1.0f, 0.0f, 0.0f }, /* B */
            { 1.0f, 1.0f, 0.0f }, /* C */
            { 0.0f, 1.0f, 0.0f }, /* D */
            { 0.0f, 0.0f, 1.0f }, /* E */
            { 1.0f, 0.0f, 1.0f }, /* F */
            { 1.0f, 1.0f, 1.0f }, /* G */
            { 0.0f, 1.0f, 1.0f } /* H */
    };
    public int[][] face = new int[][] {
        /*{0, 1, 3, 2},
        {4, 5, 7, 6},
        {0, 1, 5, 4},
        {2, 3, 7, 6},
        {0, 2, 6, 4},
        {1, 3, 7, 5}*/
        { 0, 1, 2, 3 }, // A-B-C-D を結ぶ面
            { 1, 5, 6, 2 }, // B-F-G-C を結ぶ面
            { 5, 4, 7, 6 }, // F-E-H-G を結ぶ面
            { 4, 0, 3, 7 }, // E-A-D-H を結ぶ面
            { 4, 5, 1, 0 }, // E-F-B-A を結ぶ面
            { 3, 2, 6, 7 } // D-C-G-H を結ぶ面
    };

    private WorldPosition wp;
    private static int[][] t = new int[][] {
        {0, 0},
        {0, 12},
        {12, 12},
        {12, 0}
    };

    private Aspect asp;

    public test(WorldPosition wp) {
        this.wp = wp;
        asp = new Aspect(0, 0);
    }

    public void init() {
        
    }

    public WorldPosition setPosition() {
        return wp;
    }

    public Aspect setAspect() {
        return asp;
    }

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        x = 0;
        y = -1;
        asp.add(0.2, 0);
        return 1;
    }

    //次のCubeに移行
    public void nextCube() {

    }

    private int y;

    //テクスチャ
    public Texture modelTex() {
        y++;
        if (y == 2) return Textures.Block.textures[1];
        else return Textures.Block.textures[0];
    }

    //テクスチャの切り出す場所 [4][2]
    public int[][] nextFaceTexCut() {
        return t;
    }

    private int x;

    //座標（そのエンティティの相対座標）[4][3]
    public double[][] nextFacePos() {
        double[][] result = new double[4][];
        for (int i = 0;i < 4;i++) {
            result[i] = a[face[x][i]];
        }
        x++;
        return result;
    }
}
