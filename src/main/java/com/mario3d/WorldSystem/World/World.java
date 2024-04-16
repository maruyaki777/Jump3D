package com.mario3d.WorldSystem.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Queue;

import com.mario3d.CubeEntity.CubeEntity;
import com.mario3d.Cubes.Cube;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Entity;
import com.mario3d.Models.ModelDeath;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldChunkPosition;
import com.mario3d.WorldSystem.WorldPosition;

public class World {
    public HashMap<WorldChunkPosition, Chunk> map;
    public List<Cube> cubes;
    public List<Entity> entities;
    public List<CubeEntity> cubeentities;
    public Queue<Entity> killEntities = new LinkedList<>();

    //インスタンス化は同時に一個まで　ここでモデルを消す
    public World(BasicWorld bw) {
        map = bw.map;
        cubes = bw.cubes;
        entities = new LinkedList<>();
        GameScreen.models = new CopyOnWriteArrayList<>();
        GameScreen.deathmodels = new CopyOnWriteArrayList<>();
        for (Entity entity : bw.entities) {
            entities.add(Entity.NewEntity(entity));
        }
    }

    protected World() {
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

    public void EntityCalc() {
        for (Entity e : entities) {
            if (calcornot(e)) e.calc();
            if (e.pos.y < -100) e.kill();
        }
    }

    private boolean calcornot(Entity e) {
        final double borderload = 30;
        if (e.loaded) return true;
        WorldPosition playerpos = GameScene.player.pos;
        if (Math.abs(playerpos.x - e.pos.x) < borderload && Math.abs(playerpos.z - e.pos.z) < borderload) {e.loaded = true; return true;}
        else return false;
    }

    private Queue<Entity> addentity = new LinkedList<>();
    public void addEntity(Entity entity) {
        addentity.add(entity);
    }

    public void removeEntity(Entity entity) {
        killEntities.add(entity);
    }

    public void addEntities() {
        int newentity = addentity.size();
        for (int i = 0;i < newentity;i++) {
            this.entities.add(addentity.poll());
        }
    }

    //エンティティキル
    public void killEntities() {
        int s = killEntities.size();
        for (int i = 0;i < s;i++) {
            Entity entity = killEntities.poll();
            if (entities.remove(entity) && entity.model != null) {
                GameScreen.models.remove(entity.model);
                if (entity.model instanceof ModelDeath.InnerModelDeath && ((ModelDeath.InnerModelDeath) entity.model).deathMotionEnable()) {
                    GameScreen.deathmodels.add(new ModelDeath(entity.model, 1, 0.1, 0.1));
                }
            }
        }
    }
}
