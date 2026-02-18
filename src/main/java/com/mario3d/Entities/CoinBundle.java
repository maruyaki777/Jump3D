package com.mario3d.Entities;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Entities.Utils.PhysicPort;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Models.ModelCoinBundle;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

public class CoinBundle extends Entity implements PhysicPort {
	
	private static final double throwforce = 0.06;
	private static final double kickforce = 0.13;
	private static final int delete_time = 800;
	
	private Physic physic;
	
	public CoinBundle(WorldPosition pos, Aspect aspect) {
		super("coinbundle", pos);
		super.model = new ModelCoinBundle(this);
		collision = new Collision(this, 1, 1);
		super.aspect = aspect;
		setloadrange(Integer.MAX_VALUE);
		physic = new Physic(collision);
		modecalc = this::calcThrow;
		kicked = false;
		double aspradian = Math.toRadians(aspect.x);
		this.moveX = Math.sin(aspradian) * throwforce;
		this.moveZ = Math.cos(aspradian) * throwforce;
		cooldown = 0;
		time = 0;
		physic.addForce(0, 0.3, 0);
		GameScreen.models.add(model);
	}
	
	private boolean kicked;
	private Runnable modecalc;
	private int mode; /*0: 通常 1: 停止 2: 蹴られた状態*/
	private int cooldown;
	private int time;
	
	@Override
	public void calc() {
		if (cooldown >= 0) cooldown--;
		this.modecalc.run();
	}
	
	private void calcStop() {
		physiccalc();
		time++;
		if (time > delete_time) kill();
	}
	
	private double moveX;
	private double moveZ;
	private void calcThrow() {
		if (collision.touch[0] || collision.touch[3]) moveX = 0;
		if (collision.touch[2] || collision.touch[5]) moveZ = 0;
		physic.addForce(moveX, 0, moveZ);
		physiccalc();
		if (collision.touch[1]) {
			modecalc = this::calcStop;
			mode = 1;
		}
	}
	
	private void calcKick() {
		physic.addForce(moveX, 0, moveZ);
		physiccalc();
		if (isSideTouched()) kill();
	}
	
	private void physiccalc() {
		WorldPosition postmp = physic.calc(pos);
        WorldPosition climb;
        if ((climb = collision.calc_up_stair(pos)) != null) pos = climb;
        else pos = postmp;
	}
	
	private boolean isSideTouched() {
		if (collision.touch[0] || collision.touch[2] || collision.touch[3] || collision.touch[5]) return true;
		return false;
	}
	
	@Override
	public void kill() {
		GameScene.world.removeEntity(this);
	}
	
	@Override
	public void onEvent(GameEvent event) {
		if (event.eventType == GameEvent.EventType.Collision) {
			CollisionEvent ce = (CollisionEvent) event;
			if (ce.entity == GameScene.player && mode == 1) {
				mode = 2;
				modecalc = this::calcKick;
				WorldPosition playerpos = ce.entity.pos;
	            if (pos.x - playerpos.x == pos.z - playerpos.z) return;
	            double radianasp = Math.atan2(pos.x - playerpos.x, pos.z - playerpos.z);
	            double degreeasp = Math.toDegrees(radianasp);
	            aspect.set(degreeasp, 0);
	    		this.moveX = Math.sin(radianasp) * kickforce;
	    		this.moveZ = Math.cos(radianasp) * kickforce;
			}
			else if (mode == 2) {
				if (ce.entity == GameScene.player && cooldown != 0) return;
				GameEvent.add(new MonsterDamageEvent(ce.entity, this, 4));
			}
		}
		else if (event.eventType == GameEvent.EventType.MonsterDamage) {
			MonsterDamageEvent mde = (MonsterDamageEvent) event;
			if (mde.level >= 3) kill();
		}
	}
	
	public boolean isKicked() {
		return kicked;
	}
	public int getMode() {return mode;}
	
	public Physic getPhysic() {return physic;}
}
