package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelExplosion;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldPosition;

public class Explosion extends Entity{
	
	public static final int default_lifetime = 7;
	public static final double damage_range = 7.0;
	
	public Explosion(WorldPosition pos) {
		super("explosion", pos);
		this.side = 1;
		super.model = new ModelExplosion(this);
		setloadrange(Integer.MAX_VALUE);
		lifetime = 0;
		extinct = false;
		GameScreen.models.add(model);
	}
	
	private boolean extinct;
	
	/*0-5 -x -y -z x y z*/
	public Explosion(WorldPosition pos, int side) {
		super("explosion", pos);
		this.side = side;
		super.model = new ModelExplosion(this);
		lifetime = 0;
		extinct = false;
		GameScreen.models.add(model);
	}
	
	private int lifetime;
	private final int side;
	
	@Override
	public void calc() {
		if (lifetime == 0) {
			for (Entity e : GameScene.world.entities) {
				if (e.pos.distance(pos) < damage_range) GameEvent.add(new MonsterDamageEvent(e, this, 2));
			}
		}
		lifetime++;
		if (lifetime > default_lifetime) {
			kill();
		}
	}
	
	@Override
	public void kill() {extinct = true;GameScene.world.removeEntity(this);}
	
	public int getSide() {return side;}
	
	public boolean isExtinct() {return extinct;}
}
