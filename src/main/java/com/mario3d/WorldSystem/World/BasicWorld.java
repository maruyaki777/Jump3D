package com.mario3d.WorldSystem.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.mario3d.CubeEntity.CubeEntity;
import com.mario3d.Cubes.Cube;
import com.mario3d.Entities.Entity;
import com.mario3d.WorldSystem.WorldChunkPosition;
import com.mario3d.WorldSystem.WorldPosition;


//死んだときにリセットする元の場所
public class BasicWorld {
    public HashMap<WorldChunkPosition, Chunk> map;
    public List<Cube> cubes;
    public List<Entity> entities;
    public List<CubeEntity> cubeentities;

    //インスタンス化は同時に一個まで
    public BasicWorld() {
        map = new HashMap<>();
        cubes = new LinkedList<>();
        entities = new LinkedList<>();
    }

    public void addCubeToMap(Cube cube) {
        WorldPosition wp = cube.BasePos;
        WorldChunkPosition wcp = wp.getChunkPosition();
        WorldChunkPosition wcpx = new WorldPosition(wp.x + cube.dx, wp.y, wp.z + cube.dz).getChunkPosition();
        for (int i = 0;i <= wcpx.x - wcp.x;i++) {
            WorldChunkPosition wcpr = new WorldChunkPosition(wcp);
            wcpr.x += i;
            for (int j = 0;j <= wcpx.z - wcp.z;j++) {
                Chunk chunk = map.get(wcpr);
                if (chunk == null) {
                    chunk = new Chunk(wcpr);
                    map.put(new WorldChunkPosition(wcpr), chunk);
                }
                chunk.container.add(cube);
                wcpr.z++;
            }
        }
    }
}
