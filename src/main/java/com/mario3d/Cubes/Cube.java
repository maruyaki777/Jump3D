package com.mario3d.Cubes;

import com.mario3d.Events.GameEvent;
import com.mario3d.Events.GameEventListener;
import com.mario3d.WorldSystem.WorldPosition;
import com.mario3d.Displays.GameScreen.CubeDisplayType;

public abstract class Cube implements GameEventListener{
    public String id;
    public WorldPosition BasePos; //x, y, zすべて最小値
    public double dx;
    public double dy;
    public double dz;

    public WorldPosition[] corners;
    public int[][] face;/*-x -y -z x y z*/

    public CubeDisplayType displayType = CubeDisplayType.COMMON;

    /*親クラス
    引数は新しくインスタンスの作成推奨*/
    protected Cube(WorldPosition pos1, WorldPosition pos2) {
        double tmp;
        corners = new WorldPosition[8];
        face = new int[6][4];
        if (pos1.x > pos2.x) {
            tmp = pos1.x;
            pos1.x = pos2.x;
            pos2.x = tmp;
        }
        if (pos1.y > pos2.y) {
            tmp = pos1.y;
            pos1.y = pos2.y;
            pos2.y = tmp;
        }
        if (pos1.z > pos2.z) {
            tmp = pos1.z;
            pos1.z = pos2.z;
            pos2.z = tmp;
        }
        BasePos = new WorldPosition(pos1);
        dx = pos2.x - pos1.x;
        dy = pos2.y - pos1.y;
        dz = pos2.z - pos1.z;
        int xi = 0; int xj = 0;
        int yi = 0; int yj = 0;
        int zi = 0; int zj = 0;
        for (int i = 0;i < corners.length;i++) {
            corners[i] = new WorldPosition(0, 0, 0);
            if (i % 2 == 0) {corners[i].x = pos1.x; face[0][xi++] = i;} else {corners[i].x = pos2.x; face[3][xj++] = i;}
            if (i % 4 <= 1) {corners[i].y = pos1.y; face[1][yi++] = i;} else {corners[i].y = pos2.y; face[4][yj++] = i;}
            if (i <= 3) {corners[i].z = pos1.z; face[2][zi++] = i;} else {corners[i].z = pos2.z; face[5][zj++] = i;}
        }
    }

    public static void main(String[] args) {
        Cube c = new Dirt(new WorldPosition(0, -0.5, 0), new WorldPosition(1, 0, 1));
        System.out.printf("corners\n");
        for (int i = 0;i < c.corners.length;i++) System.out.printf("{%f, %f, %f}\n", c.corners[i].x, c.corners[i].y, c.corners[i].z);
        System.out.printf("face\n");
        for (int i = 0;i < c.face.length;i++) System.out.printf("{%d, %d, %d, %d}\n", c.face[i][0], c.face[i][1], c.face[i][3], c.face[i][2]);
    }

    @Override
    public void onEvent(GameEvent event) {}
}
