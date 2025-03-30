package com.mario3d.Profile;

import com.mario3d.Scenes.GameScene.CheckPoint;


public class Profile {
	
	public CheckPoint checkpoint;
	public int remaining;
	public int deathtime;
	public long timeplayed;
	public int courseid;

	public Profile(int remaining, int courceid) {
		this.checkpoint = null;
		this.remaining = remaining;
		this.deathtime = 0;
		this.timeplayed = 0;
	}
	
	public Profile(CheckPoint checkpoint, int remaining, int deathtime, long timeplayed, int courseid) {
		this.checkpoint = checkpoint;
		this.remaining = remaining;
		this.deathtime = deathtime;
		this.timeplayed = timeplayed;
		this.courseid = courseid;
	}
}
