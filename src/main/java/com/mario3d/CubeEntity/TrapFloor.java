package com.mario3d.CubeEntity;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Models.ModelTrapFloor;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public class TrapFloor extends CubeEntity{
    public TrapFloor(WorldPosition pos, int[] tag) {
        super("trapfloor", pos, new RelativePosition((double)tag[0] / -2, -0.5, (double)tag[1] / -2), new RelativePosition((double)tag[0] / 2, 0, (double)tag[1] / 2));
        super.model = new ModelTrapFloor(this, tag);
        GameScreen.models.add(model);
    }

    @Override
    public void calc() {}

    @Override
    public void kill() {
        GameScene.world.removeEntity(this);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType == GameEvent.EventType.Collision) {
            CollisionEvent ce = (CollisionEvent) event;
            if (ce.entity == GameScene.player) {
                kill();
            }
        }
    }
}
