package com.mario3d.CubeEntity;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Events.GameEvent;
import com.mario3d.Events.GoalEvent;
import com.mario3d.Models.ModelGoal;
import com.mario3d.Scenes.GameScene;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;

public class Goal extends CubeEntity{
    public Goal(WorldPosition pos) {
        super("goal", pos, new RelativePosition(-0.5, 0, -0.5), new RelativePosition(0.5, 1, 0.5));
        super.model = new ModelGoal(this);
        GameScreen.models.add(model);
    }

    private static final double goalheight = 13.4;

    @Override
    public void calc() {
        WorldPosition playerpos = GameScene.player.pos;
        double playerdx = GameScene.player.collision.dx;
        if (playerpos.y > this.pos.y + 0.5 && playerpos.y < this.pos.y + goalheight &&
            playerpos.x - playerdx < this.pos.x && playerpos.x + playerdx > this.pos.x &&
            playerpos.z - playerdx < this.pos.z && playerpos.z + playerdx > this.pos.z)
        {
            GameEvent.add(new GoalEvent(GameScene.player, new WorldPosition(this.pos.x, playerpos.y, this.pos.z), playerpos.y - this.pos.y - 1));
        }
    }
}
