package com.mario3d.CubeDisplay;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Cubes.Cube;
import com.mario3d.Displays.Textures;

public class Common implements CubeDisplayData{
    private Cube cube;
    public int sectiontime(Cube cube) {
        this.cube = cube;
        return 1;
    }

    public SectionData section(SectionData data, int facenum) {
        data.basepos[0] = cube.BasePos.x;
        data.basepos[1] = cube.BasePos.y;
        data.basepos[2] = cube.BasePos.z;
        data.dpos[0] = cube.dx;
        data.dpos[1] = cube.dy;
        data.dpos[2] = cube.dz;
        data.textures[0] = getTex(cube.id);
        data.texture_wide = 1.0;
        return data;
    }

    public void nextSection() {}

    private Texture lava = Textures.Loader.getTexture("block", "lava");
    private Texture dirt = Textures.Loader.getTexture("block", "dirt");
    private Texture brick = Textures.Loader.getTexture("block", "brick");
    private Texture olddirt = Textures.Loader.getTexture("block", "olddirt");
    private Texture getTex(String id) {
        switch (id) {
            case "dirt":
                return dirt;
            case "lava":
                return lava;
            case "brick":
                return brick;
            case "olddirt":
                return olddirt;
        }
        return Textures.error_png;
    }
}
