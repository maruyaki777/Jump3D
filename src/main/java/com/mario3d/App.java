package com.mario3d;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.awt.Font;
import java.awt.FontFormatException;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        //フォント設定
        GraphicsEnvironment ge = null;
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, App.class.getResourceAsStream("/assets/font/PixelMplus12-Regular.ttf")));
        }
        catch (FontFormatException e) {e.printStackTrace(); System.exit(-1);}
        catch (IOException e) {e.printStackTrace(); System.exit(-1);}

        //プロパティの入手
        Properties prop = new Properties();
        try {
            prop.load(App.class.getResourceAsStream("/info.properties"));
        }
        catch (IOException e) {version = "error / Can't load properties file.";}
        catch (NullPointerException e) {version = "error / Can't load properties file.";}
        if (version == null) version = "Jump3D " + prop.getProperty("version");
        Display d = new Display();
        MouseKeyboard mk = new MouseKeyboard(d.glWindow);
        d.glWindow.addKeyListener(mk);
        d.glWindow.addMouseListener(mk);
        GameManager.mk = mk;
        GameManager.display = d;
        while (true) {
            if (Display.finishedinit) break;
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {}
        }
        GameManager.start();
    }

    public static String version = null;
}
