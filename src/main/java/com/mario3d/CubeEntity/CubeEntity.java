package com.mario3d.CubeEntity;

import com.mario3d.Entities.Entity;
import com.mario3d.Events.GameEvent;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public abstract class CubeEntity extends Entity{

    public String cubeentity_id;
    public RelativePosition BasePos;
    public double dx;
    public double dy;
    public double dz;
    public RelativePosition[] corners;
    public WorldPosition oldpos;
    public int[][] face;/*-x -y -z x y z*/
    
    //通常のCubeとは違い、動かすことができる当たり判定。
    protected CubeEntity(String id, WorldPosition WorldPos, RelativePosition pos1, RelativePosition pos2) {
        super(id, WorldPos);
        oldpos = new WorldPosition(WorldPos);
        cubeentity_id = id;
        
        double tmp;
        corners = new RelativePosition[8];
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
        this.BasePos = new RelativePosition(pos1);
        dx = pos2.x - pos1.x;
        dy = pos2.y - pos1.y;
        dz = pos2.z - pos1.z;
        int xi = 0; int xj = 0;
        int yi = 0; int yj = 0;
        int zi = 0; int zj = 0;
        for (int i = 0;i < corners.length;i++) {
            corners[i] = new RelativePosition(0, 0, 0);
            if (i % 2 == 0) {corners[i].x = pos1.x; face[0][xi++] = i;} else {corners[i].x = pos2.x; face[3][xj++] = i;}
            if (i % 4 <= 1) {corners[i].y = pos1.y; face[1][yi++] = i;} else {corners[i].y = pos2.y; face[4][yj++] = i;}
            if (i <= 3) {corners[i].z = pos1.z; face[2][zi++] = i;} else {corners[i].z = pos2.z; face[5][zj++] = i;}
        }
    }

    @Override
    abstract public void calc();

    @Override
    public void kill() {}

    @Override
    public void onEvent(GameEvent event) {}
}
