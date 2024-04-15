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

    private TrapFloor entity;
    private TF_tex type;
    public ModelTrapFloor(TrapFloor entity) {
        this.entity = entity;
        this.needCube = entity.tag[0] * entity.tag[1];
        switch (entity.tag[2]) {
            case 0: {
                type = TF_tex.Grass;
                break;
            }
            case 1: {
                type = TF_tex.Dirt;
                break;
            }
        }
        tex = type.tex;
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

    private int fi;
    @Override
    public int getCubeCount() {
        fi = -1;
        return needCube;
    }
    @Override
    public void nextCube() {}
    @Override
    public Texture modelTex() {return type.texture;}
    @Override
    public int[][] nextFaceTexCut() {return null;}

    @Override
    public double[][] nextFacePos() {
        return null;
    }

    private enum TF_tex {
        Grass("grass"),
        Dirt("dirt");

        public final Texture texture;
        public final int[][] tex;
        private TF_tex(String name) {
            texture = Textures.Loader.getTexture("block", name);
            int x = texture.getWidth(), y = texture.getHeight();
            this.tex = new int[][] {{0, 0}, {x, 0}, {x, y}, {0, y}};
        }
    }
}
