package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public interface Model {
    public static Aspect aspect0 = new Aspect(0, 0);
    public static final double[] defaultcolor = new double[] {1, 1, 1};
    public static final double[] defaultdamagecolor = new double[] {1, 0, 0};
    public void init();

    //描画対象の座標
    public WorldPosition setPosition();

    //描画対象の角度
    public Aspect setAspect();

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount();

    //次のCubeに移行
    public void nextCube();

    default public RelativePosition setCubePos() {return null;}
    default public Aspect setCubeAspect() {return null;}

    //テクスチャ
    public Texture modelTex();

    //テクスチャの切り出す場所 [4][2]
    public int[][] nextFaceTexCut();

    //座標（そのエンティティの相対座標）[4][3]
    public double[][] nextFacePos();
    
    //色 [3]
    default public double[] color() {
    	return defaultcolor;
    }
}
