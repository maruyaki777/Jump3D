package com.mario3d.Entities;

import com.mario3d.Events.SignalEvent;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.WorldPosition;

public class SignalPoint extends Entity{
	private final int destination;
	private final int dis;
	/*
	 * tag int配列
	 * 0 -> 宛先
	 * 1 -> 検知範囲
	 */
	public SignalPoint(WorldPosition pos, int[] tag) {
		super("signalpoint", pos);
		this.destination = tag[0];
		this.dis = tag[1];
	}
	
	@Override
	public void calc() {
		WorldPosition playerpos = GameScene.player.pos;
		if (Math.abs(playerpos.x - pos.x) < dis && Math.abs(playerpos.z - pos.z) < dis) {
			SignalEvent.sendSignals(destination);
			kill();
		}
	}
	
	@Override
	public void kill() {GameScene.world.removeEntity(this);}
}
