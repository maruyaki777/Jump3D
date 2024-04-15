package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.GameManager;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Displays.Textures;
import com.mario3d.Displays.FPSBuffers.FPSBufferStream;
import com.mario3d.Displays.FPSBuffers.FPSBufferUtils;
import com.mario3d.Displays.GameScreen.Perspective;
import com.mario3d.Entities.Player;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Models.ModelTex.ModelCubeTex;
import com.mario3d.Models.ModelTex.ModelTexArray;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;
import com.mario3d.Models.ModelDeath.InnerModelDeath;

public class ModelPlayer implements Model, InnerModelDeath{

    private static final double[][][] corners = new double[][][] {
        {//頭
            {-0.400000, 1.600000, -0.400000},
            {0.400000, 1.600000, -0.400000},
            {-0.400000, 2.200000, -0.400000},
            {0.400000, 2.200000, -0.400000},
            {-0.400000, 1.600000, 0.400000},
            {0.400000, 1.600000, 0.400000},
            {-0.400000, 2.200000, 0.400000},
            {0.400000, 2.200000, 0.400000}
        },
        {//帽子
            {-0.450000, 2.000000, -0.450000},
            {0.450000, 2.000000, -0.450000},
            {-0.450000, 2.400000, -0.450000},
            {0.450000, 2.400000, -0.450000},
            {-0.450000, 2.000000, 0.450000},
            {0.450000, 2.000000, 0.450000},
            {-0.450000, 2.400000, 0.450000},
            {0.450000, 2.400000, 0.450000}
        },
        {//帽子のつば
            {-0.450000, 2.000000, 0.450000},
            {0.450000, 2.000000, 0.450000},
            {-0.450000, 2.100000, 0.450000},
            {0.450000, 2.100000, 0.450000},
            {-0.450000, 2.000000, 0.850000},
            {0.450000, 2.000000, 0.850000},
            {-0.450000, 2.100000, 0.850000},
            {0.450000, 2.100000, 0.850000}
        },
        {//胴体
            {-0.300000, 0.800000, -0.200000},
            {0.300000, 0.800000, -0.200000},
            {-0.300000, 1.600000, -0.200000},
            {0.300000, 1.600000, -0.200000},
            {-0.300000, 0.800000, 0.200000},
            {0.300000, 0.800000, 0.200000},
            {-0.300000, 1.600000, 0.200000},
            {0.300000, 1.600000, 0.200000}
        }
    };
    private static final double[][][] hand = new double[][][] {
        {//腕
            {-0.150000, -0.700000, -0.150000},
            {0.150000, -0.700000, -0.150000},
            {-0.150000, 0.100000, -0.150000},
            {0.150000, 0.100000, -0.150000},
            {-0.150000, -0.700000, 0.150000},
            {0.150000, -0.700000, 0.150000},
            {-0.150000, 0.100000, 0.150000},
            {0.150000, 0.100000, 0.150000}
        },
        {//脚
            {-0.150000, -0.800000, -0.150000},
            {0.150000, -0.800000, -0.150000},
            {-0.150000, 0.000000, -0.150000},
            {0.150000, 0.000000, -0.150000},
            {-0.150000, -0.800000, 0.150000},
            {0.150000, -0.800000, 0.150000},
            {-0.150000, 0.000000, 0.150000},
            {0.150000, 0.000000, 0.150000}
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
        new ModelCubeTex(0, 50, 8, 6, 8),
        new ModelCubeTex(28, 37, 9, 4, 9),
        new ModelCubeTex(38, 59, 9, 1, 4),
        new ModelCubeTex(0, 37, 6, 8, 4),
        new ModelCubeTex(0, 25, 3, 8, 3),
        new ModelCubeTex(0, 25, 3, 8, 3),
        new ModelCubeTex(16, 25, 3, 8, 3),
        new ModelCubeTex(16, 25, 3, 8, 3)
    );

    private final Player entity;
    private FPSBufferStream<WorldPosition> WP_fpsbuffer;
    private FPSBufferStream<Aspect> Asp_fpsbuffer;

    public ModelPlayer(Player entity) {
        this.entity = entity;
        this.oldpos = new WorldPosition(entity.pos);
        WP_fpsbuffer = new FPSBufferStream<WorldPosition>(FPSBufferUtils::WP_calc, this::getpos, GameManager.fps, new WorldPosition(entity.pos));
        Asp_fpsbuffer = new FPSBufferStream<Aspect>(FPSBufferUtils::Asp_calc, this::getAsp, GameManager.fps, new Aspect(entity.aspect));
    }
    private WorldPosition getpos() {return entity.pos;}
    private Aspect getAsp() {return entity.aspect;}

    public void init() {

    }

    //描画対象の座標
    public WorldPosition setPosition() {
        //return entity.pos;
        return (latestpos = WP_fpsbuffer.read());
    }

    //描画対象の角度
    public Aspect setAspect() {
        //return new Aspect(-entity.aspect.x, 0);
        return new Aspect(-Asp_fpsbuffer.read().x, 0);
    }

    private double handpos = 0;
    private WorldPosition oldpos;
    private WorldPosition latestpos;
    //何回nextCubeを呼び出せばいいか 位置変更の指示の役割もある
    public int getCubeCount() {
        WP_fpsbuffer.setLastFPS(GameManager.fps);
        Asp_fpsbuffer.setLastFPS(GameManager.fps);
        if (GameScene.state == GameScene.State.GAME) {
            if (Math.abs(oldpos.x - latestpos.x) > 0.05 * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f)) || Math.abs(oldpos.z - latestpos.z) > 0.05 * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f)))
                handpos = calchandaspect(handpos);
            else handpos = calchandaspectstop(handpos);
            handleftaspect.set(handleftaspect.x, handpos);
            handrightaspect.set(handrightaspect.x, -handpos);
            oldpos.x = latestpos.x; oldpos.y = latestpos.y; oldpos.z = latestpos.z;
        }
        if (GameScreen.getpers() == Perspective.One) return 0;
        fi = -1;
        return 8;
    }

    private int unit_hand = 1;
    private double calchandaspect(double handpos) {
        final double x = 8 * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
        handpos += unit_hand * x;
        if (handpos > 45) {
            unit_hand = -1;
            handpos = 45;
        }
        else if (handpos < -45) {
            unit_hand = 1;
            handpos = -45;
        }
        return handpos;
    }
    private double calchandaspectstop(double handpos) {
        final double x = 8 * ((float)GameManager.gameTick / (GameManager.display.getFPS() + 0.1f));
        double hand = handpos;
        if (hand > 0) hand -= x;
        else if (hand < 0) hand += x;
        if ((int)(hand / Math.abs(hand)) != (int)(handpos / Math.abs(handpos))) hand = 0;
        return hand;
    }

    //次のCubeに移行
    public void nextCube() {
        fi++;
        ti = 0;
    }

    //テクスチャ
    private static Texture texture = Textures.Loader.getTexture("entity", "player");
    public Texture modelTex() {return texture;}

    private int ti;
    private int fi;

    //テクスチャの切り出す場所 [4][2]
    public int[][] nextFaceTexCut() {return tex[fi][ti];}

    private double[][] result = new double[4][];

    //座標（そのエンティティの相対座標）[4][3]
    public double[][] nextFacePos() {
        if (fi <= 3) for (int i = 0;i < 4;i++) {
            result[i] = corners[fi][face[ti][i]];
        }
        else if (fi >= 4 && fi <= 5) for (int i = 0;i < 4;i++) {
            result[i] = hand[0][face[ti][i]];
        }
        else if (fi >= 6 && fi <= 7) for (int i = 0;i < 4;i++) {
            result[i] = hand[1][face[ti][i]];
        }
        ti++;
        return result;
    }

    private RelativePosition rp = new RelativePosition(0, 0, 0);
    @Override
    public RelativePosition setCubePos() {
        rp.x = 0; rp.y = 0; rp.z = 0;
        switch (fi) {
            case 4: {
                rp.x = 0.45;
                rp.y = 1.5;
                rp.z = 0;
                return rp;
            }
            case 5: {
                rp.x = -0.45;
                rp.y = 1.5;
                rp.z = 0;
                return rp;
            }
            case 6: {
                rp.x = 0.15;
                rp.y = 0.8;
                rp.z = 0;
                return rp;
            }
            case 7: {
                rp.x = -0.15;
                rp.y = 0.8;
                rp.z = 0;
                return rp;
            }
        }
        return null;
    }
    Aspect handleftaspect = new Aspect(0, 0);
    Aspect handrightaspect = new Aspect(0, 0);
    Aspect defaultaspect = new Aspect(0, 0);
    @Override
    public Aspect setCubeAspect() {
        if (fi <= 3) return defaultaspect;
        else if (fi == 4) return handleftaspect;
        else if (fi == 5) return handrightaspect;
        else if (fi == 6) return handrightaspect;
        else if (fi == 7) return handleftaspect;
        else return null;
    }
}
