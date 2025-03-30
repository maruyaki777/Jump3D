package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelFireBall;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.WorldSystem.WorldPosition;

public class FireBall extends Entity{
	
	private static final double fireballspeed = 0.5;
	private static final int default_lifetime = 200;

	public FireBall(WorldPosition pos, Aspect asp) {
		super("fireball", pos);
		super.aspect = asp;
		super.model = new ModelFireBall(this);
		setloadrange(Integer.MAX_VALUE);
		collision = new Collision(this, 0.6, 1.2);
		//力の方向の計算
		double xradian = Math.toRadians(aspect.x);
		double yradian = Math.toRadians(aspect.y);
		xspeed = Math.sin(xradian) * Math.cos(yradian) * fireballspeed;
		yspeed = Math.sin(yradian) * fireballspeed;
		zspeed = Math.cos(xradian) * Math.cos(yradian) * fireballspeed;
		lifetime = 0;
		GameScreen.models.add(model);
	}
	
	private double xspeed;
	private double yspeed;
	private double zspeed;
	private int lifetime;
	
	@Override
	public void calc() {
		WorldPosition wp = new WorldPosition(pos.x + xspeed, pos.y + yspeed, pos.z + zspeed);
		pos = collision.calc(pos, wp);
		for (int i = 0;i < 6;i++) {
			if (collision.touch[i]) {
				explosive(i);
				break;
			}
		}
		if (lifetime > default_lifetime) kill();
		lifetime++;
	}
	
	private void explosive(int side) {
		GameScene.world.addEntity(new Explosion(new WorldPosition(pos), side));
		kill();
	}
	
	@Override
	public void kill() {GameScene.world.removeEntity(this);}
	
	@Override
	public void onEvent(GameEvent event) {
		if (event.eventType == GameEvent.EventType.Collision) {
			CollisionEvent ce = (CollisionEvent) event;
			if (ce.entity == GameScene.player) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 0));
		}
	}
}
