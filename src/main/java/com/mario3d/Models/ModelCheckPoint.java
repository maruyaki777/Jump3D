package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Displays.Textures;
import com.mario3d.Entities.CheckPoint;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelCheckPoint implements Model{
	private static final double[][][] corners = new double[][][] {
		{//土台
			{-0.300000, 0.000000, -0.300000},
			{0.300000, 0.000000, -0.300000},
			{-0.300000, 0.100000, -0.300000},
			{0.300000, 0.100000, -0.300000},
			{-0.300000, 0.000000, 0.300000},
			{0.300000, 0.000000, 0.300000},
			{-0.300000, 0.100000, 0.300000},
			{0.300000, 0.100000, 0.300000}
		},
		{//棒
			{-0.100000, 0.100000, -0.100000},
			{0.100000, 0.100000, -0.100000},
			{-0.100000, 1.600000, -0.100000},
			{0.100000, 1.600000, -0.100000},
			{-0.100000, 0.100000, 0.100000},
			{0.100000, 0.100000, 0.100000},
			{-0.100000, 1.600000, 0.100000},
			{0.100000, 1.600000, 0.100000}
		},
		{//頭
			{-0.300000, 1.600000, -0.300000},
			{0.300000, 1.600000, -0.300000},
			{-0.300000, 2.200000, -0.300000},
			{0.300000, 2.200000, -0.300000},
			{-0.300000, 1.600000, 0.300000},
			{0.300000, 1.600000, 0.300000},
			{-0.300000, 2.200000, 0.300000},
			{0.300000, 2.200000, 0.300000}
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
    	new ModelCubeTex(0, 0, 6, 1, 6),
    	new ModelCubeTex(24, 0, 2, 15, 2),
    	new ModelCubeTex(0, 8, 6, 6, 6),
    	new ModelCubeTex(0, 20, 6, 6, 6)
    );
    
    private final CheckPoint entity;
    
    public ModelCheckPoint(CheckPoint entity) {
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
        return 3;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "checkpoint");
    public Texture modelTex() {return texture;}

    private int ti;
    private int fi;

    //テクスチャの切り出す場所 [4][2]
    public int[][] nextFaceTexCut() {
    	if (fi < 2) return tex[fi][ti];
    	else if (GameScene.getCheckPointPriority() < entity.priority) return tex[2][ti];
    	else return tex[3][ti];
    }

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
