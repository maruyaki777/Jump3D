package com.mario3d.WorldSystem.WorldLoader;

import com.mario3d.WorldSystem.World.BasicWorld;
import com.mario3d.WorldSystem.World.World;

public class CourseSelector {
    private static int old_level = -1;
    private static BasicWorld basicWorld;
    public static World selector(int level) {
        if (old_level == level) return new World(basicWorld);
        switch (level) {
            case 0:
                basicWorld = Loader.start("/worlds/test/world.dat");
                break;
        }
        old_level = level;
        return new World(basicWorld);
    }
}
