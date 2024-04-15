package com.mario3d.Displays.FPSBuffers;

import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.WorldSystem.WorldPosition;

public class FPSBufferUtils {
    public static WorldPosition WP_calc(WorldPosition older, WorldPosition newer, WorldPosition result, float ratio) {
        result.x = (newer.x - older.x) * ratio + older.x;
        result.y = (newer.y - older.y) * ratio + older.y;
        result.z = (newer.z - older.z) * ratio + older.z;
        return result;
    }

    public static Aspect Asp_calc(Aspect older, Aspect newer, Aspect result, float ratio) {
        double width = newer.x - older.x;
        double unit = width / Math.abs(width);
        if (Math.abs(width) <= 180) {
            result.x = (width) * ratio + older.x;
            result.y = (width) * ratio + older.y;
        }
        else {
            result.x = older.x;
            result.add((180 - Math.abs(newer.x) + 180 - Math.abs(older.x)) * -unit * ratio, 0);
            result.y = (width) * ratio + older.y;
        }
        return result;
    }
}
