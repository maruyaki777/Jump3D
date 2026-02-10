package com.mario3d.Displays;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Textures {
    public static Texture error_png;

    public static void init() {
        Random random = new Random(System.currentTimeMillis());
        int t = (int)(System.currentTimeMillis() % 5);
        for (int i = 0;i < t;i++) random.nextInt();
        try {
            error_png = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/misc/old_ground_tex.png"), true, TextureIO.PNG);
            String[] blocks = new String[] {"ground", "grass", "grass_face", "grass_dirt", "lava", "dirt", "brick"};
            String[] entities = new String[] {"player", "sat", "lift", "messagebox", "daruma", "engineer", "faller", "goal", "turtle", "trapmessagebox", "checkpoint", "maneki", "coinbundle", "fire"};
            String[] miscs = new String[] {"title", "old_ground_tex", "player2D"};
            Textures.Loader.loadTexture(Textures.Block.textures, 0, "block", blocks);
            Textures.Loader.loadTexture(Textures.Entity.textures, 0, "entity", entities);
            Textures.Loader.loadTexture(Textures.Misc.textures, 0, "misc", miscs);

            /*Block.textures[0] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/block/ground.png"), true, TextureIO.PNG);
            Block.textures[1] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/block/bedrock.png"), true, TextureIO.PNG);

            Entity.textures[0] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/player.png"), true, TextureIO.PNG);
            Entity.textures[1] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/sat.png"), true, TextureIO.PNG);
            Entity.textures[2] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/lift.png"), true, TextureIO.PNG);
            Entity.textures[3] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/messagebox.png"), true, TextureIO.PNG);
            Entity.textures[4] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/daruma.png"), true, TextureIO.PNG);
            Entity.textures[5] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/engineer.png"), true, TextureIO.PNG);
            Entity.textures[6] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/faller.png"), true, TextureIO.PNG);
            Entity.textures[7] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/entity/goal.png"), true, TextureIO.PNG);

            Misc.textures[0] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/misc/title.png"), true, TextureIO.PNG);
            Misc.textures[1] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/misc/old_ground_tex.png"), true, TextureIO.PNG);
            Misc.textures[2] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/misc/player2D.png"), true, TextureIO.PNG);*/

            //おまけ
            if (random.nextInt() % 50 == 0) Misc.textures[0] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/misc/title.json"), true, TextureIO.PNG);
        }
        catch (IOException e) {e.printStackTrace();}
    }

    public static class Loader {
        private static HashMap<String, Texture> texdata = new HashMap<>();
        private static void loadTexture(Texture[] textures, int address, String type, String name) {
            try {
                textures[address] = TextureIO.newTexture(Textures.class.getResourceAsStream("/assets/textures/"+type+"/"+name+".png"), true, TextureIO.PNG);
                texdata.put(type + ":" + name, textures[address]);
            }
            catch (IOException e) {e.printStackTrace();textures[address] = error_png;}
        }

        private static void loadTexture(Texture[] textures, int startaddress, String type, String[] names) {
            for (int i = 0;i < names.length;i++) {
                if (startaddress >= textures.length) break;
                loadTexture(textures, startaddress, type, names[i]);
                startaddress++;
            }
        }

        public static Texture getTexture(String type, String name) {
            Texture texture = texdata.get(type + ":" + name);
            if (texture == null) return Textures.error_png;
            return texture;
        }
    }

    public static class Block {
        public static Texture[] textures = new Texture[15];
    }

    public static class Entity {
        public static Texture[] textures = new Texture[15];
    }

    public static class Misc {
        public static Texture[] textures = new Texture[15];
    }
}
