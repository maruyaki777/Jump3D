package com.mario3d.Entities;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import com.mario3d.GameManager;
import com.mario3d.Displays.GameScreen;
import com.mario3d.Entities.Utils.Aspect;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.MonsterDamageEvent;
import com.mario3d.Events.SignalEvent;
import com.mario3d.Models.ModelManeki;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Collision;
import com.mario3d.Utils.Physic;
import com.mario3d.WorldSystem.WorldPosition;

//ボス
public class Maneki extends Entity{
	private static final int drop_coin_interval = 4;
	private int hp;
	public Physic physic;
	private Random random;

	public Maneki(WorldPosition pos) {
		super("maneki", pos);
		super.repulsion = true;
		this.hp = 4; //初期HP　これが0になったらキルする
		super.model = new ModelManeki(this);
		setloadrange(100);
		collision = new Collision(this, 1.2, 2.8);
		physic = new Physic(collision);
		GameScreen.models.add(model);
		random = new Random(System.currentTimeMillis());
		move_time = 0;
		attack_time = 0;
		drop_coin = drop_coin_interval + 1;
		damagecooldown = 0;
		rechoose_attackaction();
		rechoose_moveaction();
	}
	private int move_time;
	private int attack_time;
	private int drop_coin;
	
	private Runnable[] moveactionlist = new Runnable[] {this::goPlayer, this::stop, this::goBack, this::jump, this::goLeft, this::goRight};
	private Runnable[] attackactionlist = new Runnable[] {this::engineerattack, this::summon, this::rush_forward, this::stop_attack, this::fireball};
	private Runnable moveaction;
	private Runnable attackaction;
	
	private void rechoose_moveaction() {
		moveaction = moveactionlist[random.nextInt(Integer.MAX_VALUE) % moveactionlist.length];
	}
	
	private void rechoose_attackaction() {
		attackaction = attackactionlist[random.nextInt(Integer.MAX_VALUE) % attackactionlist.length];
		drop_coin--;
		if (drop_coin == 0) {
			drop_coin = drop_coin_interval;
			drop_coinbundle();
		}
	}
	
	@Override
	public void calc() {
		if (damagecooldown > 0) damagecooldown--;
		if (GameScene.player.alive && !isDied()) {
			moveaction.run();
			attackaction.run();
		}
		else if (isDied()) {
			calc_dead();
		}
		if (!isDied()) {
			WorldPosition postmp = physic.calc(pos);
	        WorldPosition climb;
	        if ((climb = collision.calc_up_stair(pos)) != null) pos = climb;
	        else pos = postmp;
		}
		checkEntity();
	}
	private final static long survivetime = GameManager.gameTick * 10;
	private void checkEntity() {
		long now_time;
		if (!summoned.isEmpty() && summoned_time.peek() + survivetime <= (now_time = GameManager.getLatestTick())) {
			do {
				summoned.poll().kill();
				summoned_time.poll();
			} while (!summoned.isEmpty() && summoned_time.peek() + survivetime <= now_time);
		}
		return;
	}
	
	private void goPlayer() {
		move_time++;
		setAspectMove(1);
		if (move_time > 80) {
			move_time = 0;
			rechoose_moveaction();
		}
	}
	
	private void goLeft() {
		move_time++;
		setAspectMove(1, -90);
		if (move_time > 60) {
			move_time = 0;
			rechoose_moveaction();
		}
	}
	
	private void goRight() {
		move_time++;
		setAspectMove(1, 90);
		if (move_time > 60) {
			move_time = 0;
			rechoose_moveaction();
		}
	}
	
	private void stop() {
		move_time++;
		setAspectMove(0);
		if (move_time > 100) {
			move_time = 0;
			rechoose_moveaction();
		}
	}
	
	private void nothing() {}
	
	private void goBack() {
		move_time++;
		setAspectMove(-1);
		if (move_time > 60) {
			move_time = 0;
			rechoose_moveaction();
		}
	}
	
	private void jump() {
		move_time++;
		if (move_time > 10) {
			move_time = 0;
			if (collision.touch[1]) physic.addForce(0, 0.3, 0);
			rechoose_moveaction();
		}
	}
	
	private void rush_forward() {
		if (attack_time == 0) moveaction = this::nothing;
		move_time = 0;
		attack_time++;
		if (attack_time > 120) {
			attack_time = 0;
			rechoose_attackaction();
			rechoose_moveaction();
		}
		else if (attack_time > 60) {
	        double force = 0.13104;
	        double vectorX = Math.sin(Math.toRadians(aspect.x));
	        double vectorZ = Math.cos(Math.toRadians(aspect.x));
	        physic.addForce(vectorX * force, 0, vectorZ * force);
		}
		else {
			setAspectMove(0);
		}
	}
	
	private void stop_attack() {
		attack_time++;
		if (attack_time > 60) {
			attack_time = 0;
			rechoose_attackaction();
		}
	}
	
	private void engineerattack() {
		attack_time++;
		final double playertargetheight = 1.2;
        final double entityfrom = 0.5;
        final int throw_aspect_interval = 10;
        if (GameScene.player.alive) {
            if ((attack_time == 40 || attack_time == 50 || attack_time == 60)) {
                double distant = Math.sqrt(((GameScene.player.pos.x - this.pos.x) * (GameScene.player.pos.x - this.pos.x)) + ((GameScene.player.pos.z - this.pos.z) * (GameScene.player.pos.z - this.pos.z)));
                double distantY = GameScene.player.pos.y + playertargetheight - this.pos.y - entityfrom;
                double angleY;
                if (distant != 0 && distantY != 0) {
                    angleY = Math.toDegrees(Math.atan2(distantY, distant));
                }
                else angleY = 0;
                Aspect toolasp = new Aspect(aspect);
                toolasp.set(toolasp.x, angleY);
                WorldPosition toolpos = new WorldPosition(pos);
                toolpos.y += entityfrom;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
                toolasp.x -= throw_aspect_interval;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
                toolasp.x -= throw_aspect_interval;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
                toolasp.x += throw_aspect_interval * 3;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
                toolasp.x += throw_aspect_interval;
                GameScene.world.addEntity(new Tool(new WorldPosition(toolpos), new Aspect(toolasp)));
            }
        }
        if (attack_time >= 60) {
        	attack_time = 0;
        	rechoose_attackaction();
        }
	}
	private static final NewEntity[] constructors = new NewEntity[] {SAT::new, Daruma::new, Engineer::new};
	private Queue<Entity> summoned = new LinkedList<>();
	private Queue<Long> summoned_time = new LinkedList<>();
	private void summon() {
		attack_time++;
		if (attack_time == 60) {
			Entity newen = new Turtle(new WorldPosition(pos));
			newen.setloadrange(100);
			GameScene.world.addEntity(newen);
			summoned.add(newen);
			summoned_time.add(GameManager.getLatestTick());
		}
		else if (attack_time == 90) {
			Entity newen = constructors[random.nextInt(constructors.length)].get(new WorldPosition(pos));
			newen.setloadrange(100);
			GameScene.world.addEntity(newen);
			summoned.add(newen);
			summoned_time.add(GameManager.getLatestTick());
			if (random.nextInt(5) >= 3) {
				attack_time = 60;
			}
			else {
				attack_time = 0;
				rechoose_attackaction();
			}
		}
	}
	
	private void fireball() {
		attack_time++;
		final double playertargetheight = 0.1;
        final double entityfrom = 2.0;
		if (GameScene.player.alive && (attack_time == 40 || attack_time == 55 || attack_time == 70)) {
			double distant = Math.sqrt(((GameScene.player.pos.x - this.pos.x) * (GameScene.player.pos.x - this.pos.x)) + ((GameScene.player.pos.z - this.pos.z) * (GameScene.player.pos.z - this.pos.z)));
			double distantY = GameScene.player.pos.y + playertargetheight - this.pos.y - entityfrom;
            double angleY;
            if (distant != 0 && distantY != 0) {
                angleY = Math.toDegrees(Math.atan2(distantY, distant));
            }
            else angleY = 0;
            Aspect fireasp = new Aspect(aspect);
            fireasp.set(fireasp.x, angleY);
            WorldPosition wp = new WorldPosition(pos);
            wp.y += entityfrom;
            GameScene.world.addEntity(new FireBall(wp, fireasp));
		}
		if (attack_time >= 70) {
			attack_time = 0;
			rechoose_attackaction();
		}
	}
	
	@Override
	public void kill() {
		GameScene.world.removeEntity(this);
		if (tag != null && tag.length > 0) SignalEvent.sendSignals(tag[0]);
		if (summoned == null) return;
		int len = summoned.size();
		for (int i = 0;i < len;i++) {
			summoned.poll().kill();
		}
	}
	
	private void drop_coinbundle() {
		WorldPosition coinbundlepos = new WorldPosition(pos);
		coinbundlepos.y += 2;
		GameScene.world.addEntity(new CoinBundle(coinbundlepos, new Aspect(random.nextInt(360) - 179, 0)));
	}
	
	private void setAspectMove(int direction) {
		setAspectMove(direction, 0);
	}
	
    private void setAspectMove(int direction, double asp) {
        final double d = 5;
        if (GameScene.player.alive == false) return;
        WorldPosition playerpos = GameScene.player.pos;
        double paidegree = Math.atan2(playerpos.x - pos.x, playerpos.z - pos.z);
        double degree = Math.toDegrees(paidegree);
        double movedegree = degree - aspect.x;
        double unit = movedegree / Math.abs(movedegree);
        if (Math.abs(movedegree) > Math.abs(360 - Math.abs(movedegree))) {movedegree = 360 - Math.abs(movedegree);unit *= -1;}
        if (Math.abs(movedegree) < d) aspect.set(degree, aspect.y);
        else aspect.add(unit * d, 0);
        
        if (direction == 0) return;
        double force = 0.0165;
        force *= 1 - (Math.abs(movedegree) / 180);
        double vectorX = Math.sin(Math.toRadians(aspect.x + asp)) * direction;
        double vectorZ = Math.cos(Math.toRadians(aspect.x + asp)) * direction;
        physic.addForce(vectorX * force, 0, vectorZ * force);
    }
    
    private void calc_dead() {
    	move_time++;
    	if (move_time == 1) {
    		int len = summoned.size();
    		for (int i = 0;i < len;i++) {
    			summoned.poll().kill();
    			summoned_time.poll();
    		}
    	}
    	if (move_time >= 80) {
    		//上下に動かす
    		if (move_time % 4 == 0) pos.y += 0.5;
    		else if (move_time % 4 == 2) pos.y -= 0.5;
    	}
    	if (move_time >= 100 && move_time % 20 == 0) {
    		//爆発
    		WorldPosition wp = new WorldPosition(pos);
    		wp.y -= 0.5;
    		GameScene.world.addEntity(new Explosion(new WorldPosition(wp)));
    	}
    	if (move_time > 202) kill();
    }
    
    private int damagecooldown;
    private static final int default_damagecooldown = 20;
	
	@Override
	public void onEvent(GameEvent event) {
		if (event.eventType == GameEvent.EventType.Collision) {
			CollisionEvent ce = (CollisionEvent) event;
			if (ce.entity == GameScene.player) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 3));
			else if (ce.entity.id.equals("coinbundle")) {
				CoinBundle cb = (CoinBundle) ce.entity;
				if (cb.getMode() != 0) GameEvent.add(new MonsterDamageEvent(ce.entity, this, 4));
			}
		}
		else if (event.eventType == GameEvent.EventType.MonsterDamage) {
			MonsterDamageEvent mde = (MonsterDamageEvent) event;
			if (mde.level >= 4) {
				if (damagecooldown == 0) {
					hp--;
					damagecooldown = default_damagecooldown;
				}
				if (hp <= 0) {
					move_time = 0;
				}
			}
			else if (mde.entity.id.equals("turtle")) {
				if (damagecooldown == 0) {
					drop_coinbundle();
					drop_coin = drop_coin_interval;
					damagecooldown = default_damagecooldown;
				}
			}
			GameEvent.add(new MonsterDamageEvent(mde.entity, this, 3));
		}
	}
	
	public int getHP() {return hp;}
	public boolean isDied() {return hp <= 0;}
}

interface NewEntity {
	public Entity get(WorldPosition pos);
}
