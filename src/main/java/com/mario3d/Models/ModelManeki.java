package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Entities.Maneki;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelDeath.InnerModelDeath;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelManeki implements Model, InnerModelDeath{
	private static final double[][][] corners = new double[][][] {
		{//左脚
			{0.450000, 0.000000, -0.600000},
			{1.350000, 0.000000, -0.600000},
			{0.450000, 0.900000, -0.600000},
			{1.350000, 0.900000, -0.600000},
			{0.450000, 0.000000, 0.600000},
			{1.350000, 0.000000, 0.600000},
			{0.450000, 0.900000, 0.600000},
			{1.350000, 0.900000, 0.600000}
		},
		{//右脚
			{-1.350000, 0.000000, -0.600000},
			{-0.450000, 0.000000, -0.600000},
			{-1.350000, 0.900000, -0.600000},
			{-0.450000, 0.900000, -0.600000},
			{-1.350000, 0.000000, 0.600000},
			{-0.450000, 0.000000, 0.600000},
			{-1.350000, 0.900000, 0.600000},
			{-0.450000, 0.900000, 0.600000}
		},
		{//胴体
			{-1.200000, 0.300000, -0.900000},
			{1.200000, 0.300000, -0.900000},
			{-1.200000, 2.100000, -0.900000},
			{1.200000, 2.100000, -0.900000},
			{-1.200000, 0.300000, 0.900000},
			{1.200000, 0.300000, 0.900000},
			{-1.200000, 2.100000, 0.900000},
			{1.200000, 2.100000, 0.900000}
		},
		{//頭
			{-0.900000, 2.100000, -0.600000},
			{0.900000, 2.100000, -0.600000},
			{-0.900000, 3.300000, -0.600000},
			{0.900000, 3.300000, -0.600000},
			{-0.900000, 2.100000, 0.600000},
			{0.900000, 2.100000, 0.600000},
			{-0.900000, 3.300000, 0.600000},
			{0.900000, 3.300000, 0.600000}
		},
		{//口
			{-0.400000, 2.200000, 0.600000},
			{0.400000, 2.200000, 0.600000},
			{-0.400000, 2.700000, 0.600000},
			{0.400000, 2.700000, 0.600000},
			{-0.400000, 2.200000, 0.800000},
			{0.400000, 2.200000, 0.800000},
			{-0.400000, 2.700000, 0.800000},
			{0.400000, 2.700000, 0.800000}
		},
		{//左手
			{0.600000, 1.200000, 0.000000},
			{1.500000, 1.200000, 0.000000},
			{0.600000, 1.800000, 0.000000},
			{1.500000, 1.800000, 0.000000},
			{0.600000, 1.200000, 1.200000},
			{1.500000, 1.200000, 1.200000},
			{0.600000, 1.800000, 1.200000},
			{1.500000, 1.800000, 1.200000}
		},
		{//右手
			{-1.800000, 1.500000, 0.000000},
			{-1.200000, 1.500000, 0.000000},
			{-1.800000, 3.000000, 0.000000},
			{-1.200000, 3.000000, 0.000000},
			{-1.800000, 1.500000, 0.600000},
			{-1.200000, 1.500000, 0.600000},
			{-1.800000, 3.000000, 0.600000},
			{-1.200000, 3.000000, 0.600000}
		},
		{//左耳
			{0.400000, 3.300000, 0.400000},
			{0.800000, 3.300000, 0.400000},
			{0.400000, 3.700000, 0.400000},
			{0.800000, 3.700000, 0.400000},
			{0.400000, 3.300000, 0.500000},
			{0.800000, 3.300000, 0.500000},
			{0.400000, 3.700000, 0.500000},
			{0.800000, 3.700000, 0.500000}
		},
		{//右耳
			{-0.800000, 3.300000, 0.400000},
			{-0.400000, 3.300000, 0.400000},
			{-0.800000, 3.700000, 0.400000},
			{-0.400000, 3.700000, 0.400000},
			{-0.800000, 3.300000, 0.500000},
			{-0.400000, 3.300000, 0.500000},
			{-0.800000, 3.700000, 0.500000},
			{-0.400000, 3.700000, 0.500000},
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
    		new ModelCubeTex(0, 36, 9, 9, 12),
    		new ModelCubeTex(0, 36, 9, 9, 12),
    		new ModelCubeTex(0, 0, 24, 18, 18),
    		new ModelCubeTex(0, 64, 18, 12, 12),
    		new ModelCubeTex(2, 57, 8, 5, 2),
    		new ModelCubeTex(49, 57, 9, 6, 12),
    		new ModelCubeTex(42, 36, 6, 15, 6),
    		new ModelCubeTex(22, 57, 4, 4, 1),
    		new ModelCubeTex(22, 57, 4, 4, 1)
    );
    
    private final Maneki entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;
    private FPSBufferStream<Aspect> Asp_fpsbuffer;
    
    public ModelManeki(Maneki entity) {
    	this.entity = entity;
        WP_fpsbuffer = new FPSBufferStream<WorldPosition>(FPSBufferUtils::WP_calc, ()->{return this.entity.pos;}, GameManager.fps, new WorldPosition(entity.pos));
        Asp_fpsbuffer = new FPSBufferStream<Aspect>(FPSBufferUtils::Asp_calc, ()->{return this.entity.aspect;}, GameManager.fps, new Aspect(entity.aspect));
        last_hp = entity.getHP();
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
        return 9;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "maneki");
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
    
    private int last_hp;
    private int red_time;
    private static final int default_red_time = (int) (GameManager.fps * 0.5);
    public double[] color() {
    	if (last_hp > entity.getHP()) {
    		red_time = (int) (default_red_time * (GameManager.fps / (GameManager.display.getFPS() + 0.1f)));
    		last_hp = entity.getHP();
    	}
    	if (red_time > 0) {
    		red_time--;
    		return defaultdamagecolor;
    	}
    	if (entity.isDied()) return defaultdamagecolor;
    	return defaultcolor;
    }
}
