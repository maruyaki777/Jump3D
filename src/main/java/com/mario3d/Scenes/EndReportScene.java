package com.mario3d.Scenes;

import com.jogamp.newt.event.KeyEvent;
import com.mario3d.GameManager;

public class EndReportScene implements Scene, KeyAction {

	private static Scene nextScene;
	@Override
	public Scene execute() {
		return nextScene;
	}

	@Override
	public void init() {
		nextScene = null;
	}

	@Override
	public void onKey(short KeyCode) {
		if (KeyCode == KeyEvent.VK_ESCAPE) nextScene = GameManager.scene_slot[0];
	}
}
