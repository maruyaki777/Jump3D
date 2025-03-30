package com.mario3d.Models;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.CubeEntity.TrapFloor;
import com.mario3d.Displays.Textures;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.WorldSystem.WorldPosition;

public class ModelTrapFloor implements Model{
    private static final double[][] corners = new double[][] {
        {0.000000, -0.500000, 0.000000},
        {1.000000, -0.500000, 0.000000},
        {0.000000, 0.000000, 0.000000},
        {1.000000, 0.000000, 0.000000},
        {0.000000, -0.500000, 1.000000},
        {1.000000, -0.500000, 1.000000},
        {0.000000, 0.000000, 1.000000},
        {1.000000, 0.000000, 1.000000}
    };

    private static final int[][] face = new int[][] {
        {0, 2, 6, 4},//-x
        {0, 1, 5, 4},//-y
        {0, 1, 3, 2},//-z
        {1, 3, 7, 5},//x
        {2, 3, 7, 6},//y
        {4, 5, 7, 6}//z
    };

    private final int[][] tex;
    private final int[][] tex_face;
    private final int x_floor;

    private TrapFloor entity;
    private TF_tex type;
    public ModelTrapFloor(TrapFloor entity, int[] tag) {
        this.entity = entity;
        this.needCube = tag[0] * tag[1];
        x_floor = tag[0];
        switch (tag[2]) {
            case 0: {
                type = TF_tex.Grass;
                break;
            }
            case 1: {
                type = TF_tex.Dirt;
                break;
            }
            case 2: {
                type = TF_tex.Brick;
                break;
            }
            case 3: {
            	type = TF_tex.Lava;
            	break;
            }
            case 4: {
            	type = TF_tex.OldDirt;
            	break;
            }
        }
        tex = type.tex;
        tex_face = type.tex_face;
    }

    @Override
    public void init() {}

    @Override
    public WorldPosition setPosition() {
        return entity.pos;
    }

    private final int needCube;

    @Override
    public Aspect setAspect() {return aspect0;}

    private int ti;
    private int fi;
    @Override
    public int getCubeCount() {
        fi = -1;
        return needCube;
    }
    @Override
    public void nextCube() {
        ti = 0;
        fi++;
        currentbasepos[0] = (fi % x_floor) + entity.BasePos.x;
        currentbasepos[1] = (fi / x_floor) + entity.BasePos.z;
    }
    private double[] currentbasepos = new double[2];
    @Override
    public Texture modelTex() {return type.texture;}
    @Override
    public int[][] nextFaceTexCut() {
    	if (ti == 1 || ti == 4) return tex;
    	else return tex_face;
    }

    private double[][] result = new double[4][];
    @Override
    public double[][] nextFacePos() {
        for (int i = 0;i < result.length;i++) {
            result[i] = corners[face[ti][i]].clone();
            result[i][0] += currentbasepos[0];
            result[i][2] += currentbasepos[1];
        }
        ti++;
        return result;
    }

    private enum TF_tex {
        Grass("grass"),
        Dirt("dirt"),
        Brick("brick"),
    	Lava("lava"),
    	OldDirt("ground");

        public final Texture texture;
        public final int[][] tex;
        public final int[][] tex_face;
        private TF_tex(String name) {
            texture = Textures.Loader.getTexture("block", name);
            int x = texture.getWidth(), y = texture.getHeight();
            this.tex = new int[][] {{0, 0}, {x, 0}, {x, y}, {0, y}};
            this.tex_face = new int[][] {{0, y/2},{x, y/2}, {x, y}, {0, y}};
        }
    }
}
