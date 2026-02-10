package com.mario3d.WorldSystem.WorldLoader;

import com.mario3d.WorldSystem.World.BasicWorld;
import com.mario3d.WorldSystem.World.World;

public class CourseSelector {
    private static int old_level = -1;
    private static BasicWorld basicWorld;
    private static final int maxlevel = 2;
    public static World selector(int level) {
        if (old_level == level) return new World(basicWorld);
        basicWorld = Loader.start("/worlds/"+String.valueOf(level));
        //basicWorld = Loader.start("/worlds/test");
        old_level = level;
        return new World(basicWorld);
    }

    public static boolean existWorld(int level) {
        return (level >= 0 && level <= maxlevel);
    }
}
