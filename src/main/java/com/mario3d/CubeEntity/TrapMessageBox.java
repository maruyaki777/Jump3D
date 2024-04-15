package com.mario3d.CubeEntity;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelTrapTextBox;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public class TrapMessageBox extends CubeEntity{
    public TrapMessageBox(WorldPosition pos) {
        super("trapmessagebox", pos, new RelativePosition(-0.5, 0.0, -0.5), new RelativePosition(0.5, 1.0, 0.5));
        super.model = new ModelTrapTextBox(this);
        GameScreen.models.add(model);
    }

    public boolean touched = false;

    @Override
    public void calc() {}

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType != GameEvent.EventType.Collision) return;
        CollisionEvent ce = (CollisionEvent) event;
        if (touched) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 1));
        if (ce.entity.id.equals("player") == false) return;
        GameEvent.add(new MonsterDamageEvent(ce.entity, this, 1));
        touched = true;
    }
}
