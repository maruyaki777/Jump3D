package com.mario3d.Entities;

import com.mario3d.Events.GameEvent;
import com.mario3d.Scenes.GameScene;
import com.mario3d.Utils.Identification;
import com.mario3d.WorldSystem.WorldPosition;

public class Spawner extends Entity{
	public final int spawnerid;
	private final String spawnid;
	private final int[] spawntag;
	/*0 -> スポナーのID
	 *1 -> スポーンさせるエンティティのID*/
	public Spawner(WorldPosition pos, int[] tag) {
		super("spawner", pos);
		this.spawnerid = tag[0];
		this.spawnid = Identification.getEntityID(tag[1]);
		spawntag = new int[tag.length - 2];
		for (int i = 0;i < spawntag.length;i++) spawntag[i] = tag[i + 2];
	}
	
	@Override
	public void calc() {}
	
    @Override
    public void kill() {GameScene.world.removeEntity(this);}
    
    private boolean spawned = false;
    
    @Override
    public void onEvent(GameEvent event) {
    	if (event.eventType == GameEvent.EventType.Signal && !spawned) {
    		spawned = true;
    		GameScene.world.addEntity(Entity.NewEntity(spawnid, pos, spawntag));
    		kill();
    	}
    }
}
