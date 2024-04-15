package com.mario3d.Models.ModelTex;

public class ModelTexArray {
    public static int[][][][] set(ModelCubeTex ... objects) {
        int[][][][] result = new int[objects.length][][][];
        for (int i = 0;i < objects.length;i++) {
            ModelCubeTex m = objects[i];
            if (m == null) throw new IllegalArgumentException("it is null");
            result[i] = create(m);
        }
        return result;
    }

    private static int[][][] create(ModelCubeTex o) {
        int[][][] result = new int[6][][];
        //-x -y -z x y z
        result[0] = quad(o.TexBaseX, o.TexBaseY, o.z, o.y, 1);
        result[1] = quad(o.TexBaseX+o.x+o.z, o.TexBaseY+o.y, o.x, o.z, 0);
        result[2] = quad(o.TexBaseX+(2 * o.x)+(2*o.z), o.TexBaseY, -o.x, o.y, 0);
        result[3] = quad(o.TexBaseX+o.x+(2*o.z), o.TexBaseY, -o.z, o.y, 1);
        result[4] = quad(o.TexBaseX+o.z, o.TexBaseY+o.y+o.z, o.x, -o.z, 0);
        result[5] = quad(o.TexBaseX+o.z, o.TexBaseY, o.x, o.y, 0);
        return result;
    }

    private static int[][] quad(int x, int y, int LR, int UD, int pattern) {
        int[][] result = new int[4][];
        if (pattern == 0) {
            result[0] = new int[] {x, y};
            result[1] = new int[] {x+LR, y};
            result[2] = new int[] {x+LR, y+UD};
            result[3] = new int[] {x, y+UD};
        }
        else if (pattern == 1) {
            result[0] = new int[] {x, y};
            result[1] = new int[] {x, y+UD};
            result[2] = new int[] {x+LR, y+UD};
            result[3] = new int[] {x+LR, y};
        }
        else throw new IllegalArgumentException("not exist pattern");
        return result;
    }
}
