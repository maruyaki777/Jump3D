package com.mario3d.WorldSystem.WorldLoader;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.mario3d.Cubes.*;
import com.mario3d.Entities.*;
import com.mario3d.WorldSystem.WorldPosition;
import com.mario3d.WorldSystem.World.BasicWorld;
import	java.io.InputStream;

public class Loader {
    public static BasicWorld start(String directory) {
        BasicWorld world = new BasicWorld();
        try (InputStream inputStream = Loader.class.getResourceAsStream(directory);) {
            byte[] bytes = new byte[128];
            byte[] magic = new byte[] {87, 111, 114, 108, 100};
            inputStream.read(bytes, 0, 5);
            for (int i = 0;i < 5;i++) if (bytes[i] != magic[i]) return null;
            inputStream.read(bytes, 0, 4);
            int cube_count = ByteBuffer.wrap(bytes, 0, 4).getInt();
            inputStream.read(bytes, 0, 4);
            int entity_count = ByteBuffer.wrap(bytes, 0, 4).getInt();
            for (int i = 0;i < cube_count;i++) {
                inputStream.read(bytes, 0, 1);
                int id = (int) bytes[0];
                WorldPosition pos1 = new WorldPosition(0, 0, 0);
                WorldPosition pos2 = new WorldPosition(0, 0, 0);
                inputStream.read(bytes, 0, 24);
                pos1.x = ByteBuffer.wrap(bytes, 0, 8).getDouble();
                pos1.y = ByteBuffer.wrap(bytes, 8, 8).getDouble();
                pos1.z = ByteBuffer.wrap(bytes, 16, 8).getDouble();
                inputStream.read(bytes, 0, 24);
                pos2.x = ByteBuffer.wrap(bytes, 0, 8).getDouble();
                pos2.y = ByteBuffer.wrap(bytes, 8, 8).getDouble();
                pos2.z = ByteBuffer.wrap(bytes, 16, 8).getDouble();
                AddCube(world, id, pos1, pos2);
            }
            for (int i = 0;i < entity_count;i++) {
                inputStream.read(bytes, 0, 1);
                int id = (int) bytes[0];
                WorldPosition pos = new WorldPosition(0, 0, 0);
                inputStream.read(bytes, 0, 24);
                pos.x = ByteBuffer.wrap(bytes, 0, 8).getDouble();
                pos.y = ByteBuffer.wrap(bytes, 8, 8).getDouble();
                pos.z = ByteBuffer.wrap(bytes, 16, 8).getDouble();
                inputStream.read(bytes, 0, 1);
                int data_len = (int) bytes[0];
                int[] tag = new int[data_len];
                if (data_len > 0) {
                    inputStream.read(bytes, 0, data_len);
                    for (int j = 0;j < data_len;j++) tag[j] = (int)bytes[j];
                }
                AddEntity(world, id, pos, tag);
            }
            inputStream.close();
        }
        catch (IOException e) {System.err.println(e);}
        return world;
    }

    private static void AddCube(BasicWorld world, int id, WorldPosition p1, WorldPosition p2) {
        Cube cube = null;
        switch (id) {
            case 0:
                return;
            case 1:
                cube = new Dirt(p1, p2);
                break;
            case 2:
                cube = new Lava(p1, p2);
                break;
            case 3:
                cube = new Grass(p1, p2);
                break;
            case 4:
                cube = new Brick(p1, p2);
                break;
        }
        world.addCubeToMap(cube);
        world.cubes.add(cube);
    }

    private static void AddEntity(BasicWorld world, int id, WorldPosition pos, int[] tag) {
        Entity entity = null;
        switch (id) {
            case 0: {
                return;
            }
            case -1: {
                entity = Entity.NewEntity("test", pos, tag);
                break;
            }
            case 1: {
                entity = Entity.NewEntity("player", pos, tag);
                break;
            }
            case 2: {
                entity = Entity.NewEntity("sat", pos, tag);
                break;
            }
            case 3: {
                entity = Entity.NewEntity("lift", pos, tag);
                break;
            }
            case 4: {
                entity = Entity.NewEntity("messagebox", pos, tag);
                break;
            }
            case 5: {
                entity = Entity.NewEntity("daruma", pos, tag);
                break;
            }
            case 6: {
                entity = Entity.NewEntity("engineer", pos, tag);
                break;
            }
            case 7: {
                entity = Entity.NewEntity("faller", pos, tag);
                break;
            }
            case 8: {
                entity = Entity.NewEntity("goal", pos, tag);
                break;
            }
            case 9: {
                entity = Entity.NewEntity("turtle", pos, tag);
                break;
            }
            case 10: {
                entity = Entity.NewEntity("trapmessagebox", pos, tag);
                break;
            }
            case 11: {
                entity = Entity.NewEntity("trapfloor", pos, tag);
                break;
            }
        }
        world.entities.add(entity);
    }
}
