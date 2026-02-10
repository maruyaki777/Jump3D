package com.mario3d.CubeDisplay;

import com.jogamp.opengl.util.texture.Texture;
import com.mario3d.Cubes.Cube;

public interface CubeDisplayData {
    public int sectiontime(Cube cube);
    /*一つの処理が終わったときに呼び出される */
    public void nextSection();
    public SectionData section(SectionData data, int facenum);

    public static class SectionData {
        public final Texture[] textures = new Texture[6];
        public final double[] basepos = new double[3];
        public final double[] dpos = new double[3];
        public double texture_wide;
        public Texture getTex(int facenum) {
            Texture t;
            if ((t = textures[facenum]) == null) {
                t = textures[0];
            }
            return t;
        }
    }
}
