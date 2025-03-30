package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Models.ModelCheckPoint;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.WorldSystem.WorldPosition;

public class CheckPoint extends Entity{
	public final int priority;
	public CheckPoint(WorldPosition pos, int[] tag) {
		super("checkpoint", pos);
		super.model = new ModelCheckPoint(this);
		this.priority = tag[0];
		super.collision = new Collision(this, 0.3, 1.5);
		GameScreen.models.add(model);
	}
	
	@Override
	public void calc() {}
	
	@Override
	public void kill() {}
	
	@Override
	public void onEvent(GameEvent event) {
		if (GameScene.getCheckPointPriority() >= priority) return;
		if (event.eventType == GameEvent.EventType.Collision) {
			CollisionEvent ce = (CollisionEvent) event;
			if (ce.entity != GameScene.player) return;
			GameScene.setCheckPoint(pos, priority);
		}
	}
}
