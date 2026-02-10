package com.mario3d.Scenes;

import com.jogamp.newt.event.KeyEvent;
import com.mario3d.GameManager;
import com.mario3d.Displays.EndCreditScreen;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

public class EndCreditsScene implements Scene, KeyAction{

    @Override
    public void init() {
        nextScene = null;
        EndCreditScreen.resetscroll();
        String[] credittext;
        List<String> read_text = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(EndCreditsScene.class.getResourceAsStream("/assets/text/" + GameManager.language + "/credits.txt"), "utf-8"));) {
            String buf;
            while ((buf = reader.readLine()) != null) read_text.add(buf);
            credittext = new String[read_text.size()];
            credittext = read_text.toArray(credittext);
        }
        catch (IOException e) {e.printStackTrace();nextScene = GameManager.scene_slot[0];credittext = new String[0];}
        EndCreditScreen.setEndCredit(credittext);
    }

    private static Scene nextScene;

    @Override
    public Scene execute() {
        if (EndCreditScreen.finished) nextScene = GameManager.scene_slot[0];
        return nextScene;
    }

    @Override
    public void onKey(short KeyCode) {
        if (KeyCode == KeyEvent.VK_ESCAPE) nextScene = GameManager.scene_slot[0];
    }
}
