package com.mario3d.CubeEntity;

import com.mario3d.Displays.GameScreen;
import com.mario3d.Displays.ViewTextScreen;
import com.mario3d.Events.CollisionEvent;
import com.mario3d.Events.GameEvent;
import com.mario3d.Models.ModelTextBox;
import com.mario3d.WorldSystem.RelativePosition;
import com.mario3d.WorldSystem.WorldPosition;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class MessageBox extends CubeEntity{

    private final String text;
    
    public MessageBox(WorldPosition pos, int[] tag) {
        super("messagebox", pos, new RelativePosition(-0.5, 0.0, -0.5), new RelativePosition(0.5, 1.0, 0.5));
        this.text = get(tag[0]);
        super.model = new ModelTextBox(this);
        GameScreen.models.add(model);
    }

    @Override
    public void calc() {}

    @Override
    public void onEvent(GameEvent event) {
        if (event.eventType != GameEvent.EventType.Collision) return;
        CollisionEvent ce = (CollisionEvent) event;
        if (ce.entity.id.equals("player") == false) return;
        if (ce.pos.y + ce.entity.collision.dy <= pos.y + BasePos.y) {
            ViewTextScreen.set(text);
        }
    }

    private String get(int id) {
        StringBuilder str = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(MessageBox.class.getResourceAsStream("/assets/text/"+String.valueOf(id)+".txt"), "utf-8"));) {
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                str.append(buf);
                str.append('\n');
            }
            reader.close();
        }
        catch (IOException e) {e.printStackTrace();return "";}
        return str.toString();
    }
}
