package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelDeath implements Model{

    private Model model;
    private double fade = 1.0;

    public final double red;
    public final double green;
    public final double blue;
    public final double alpha_per;

    public ModelDeath(Model model, double r, double g, double b) {
    	this(model, r, g, b, 0.02);
    }
    
    public ModelDeath(Model model, double r, double g, double b, double alpha_per) {
    	this.model = model;
    	this.red = r;
    	this.green = g;
    	this.blue = b;
    	this.alpha_per = alpha_per;
    }

    public double fadeout() {
        if (GameScene.state != GameScene.State.GAME) return fade;
        fade -= alpha_per * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
        if (fade < 0) {
            fade = 0;
            GameScreen.deathmodels.remove(this);
        }
        return fade;
    }

    public void init() {model.init();}

    //描画対象の座標
    public WorldPosition setPosition() {return model.setPosition();}

    //描画対象の角度
    public Aspect setAspect() {return model.setAspect();}

    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {return model.getCubeCount();}

    //次のCubeに移行
    public void nextCube() {model.nextCube();}

    //テクスチャ
    public Texture modelTex() {return model.modelTex();}

    //テクスチャの切り出す場所 [4][2]
    public int[][] nextFaceTexCut() {return model.nextFaceTexCut();}

    //座標（そのエンティティの相対座標）[4][3]
    public double[][] nextFacePos() {return model.nextFacePos();}

    public Model getThisModel() {return model;}

    /**
     * これを継承していた場合はキルされた後一時的に残る。
     */
    public interface InnerModelDeath {
        default public boolean deathMotionEnable() {return true;}
    }
}
