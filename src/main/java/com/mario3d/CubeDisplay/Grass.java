package com.mario3d.CubeDisplay;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Cubes.Cube;
import com.mario3d.Displays.Textures;

public class Grass implements CubeDisplayData{
    private Cube cube;
    @Override
    public int sectiontime(Cube cube) {
        this.cube = cube;
        level = 0;
        executelevel = this::sectionlevel1;
        return 2;
    }
    private int level;
    private SectionRunnable executelevel;

    @Override
    public SectionData section(SectionData data, int facenum) {
        return executelevel.run(data, facenum);
    }

    @Override
    public void nextSection() {
        executelevel = this::sectionlevel2;
        level = 1;
    }

    private SectionData sectionlevel1(SectionData data, int facenum) {
        data.basepos[0] = cube.BasePos.x;
        data.basepos[1] = cube.BasePos.y;
        data.basepos[2] = cube.BasePos.z;
        data.dpos[0] = cube.dx;
        data.dpos[1] = cube.dy - 1.0;
        data.dpos[2] = cube.dz;
        data.textures[0] = getTex(cube.id, facenum);
        data.texture_wide = 1.0;
        return data;
    }

    private SectionData sectionlevel2(SectionData data, int facenum) {
        data.basepos[0] = cube.BasePos.x;
        data.basepos[1] = cube.BasePos.y + cube.dy - 1.0;
        data.basepos[2] = cube.BasePos.z;
        data.dpos[0] = cube.dx;
        data.dpos[1] = 1.0;
        data.dpos[2] = cube.dz;
        data.textures[0] = getTex(cube.id, facenum);
        data.texture_wide = 1.0;
        return data;
    }

    private Texture grass = Textures.Loader.getTexture("block", "grass");
    private Texture grass_face = Textures.Loader.getTexture("block", "grass_face");
    private Texture grass_dirt = Textures.Loader.getTexture("block", "grass_dirt");
    private Texture getTex(String id, int facenum) {
        switch (id) {
            case "grass": {
                if (level == 0) return grass_dirt;
                else {
                    if (facenum % 3 == 0 || facenum % 3 == 2) return grass_face;
                    return grass;
                }
            }
        }
        return Textures.error_png;
    }

    @FunctionalInterface
    private interface SectionRunnable {
        public SectionData run(SectionData data, int facenum);
    }
}
