package com.mario3d;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Font;
import java.awt.FontFormatException;
import java.util.HashSet;
import java.util.Properties;

import com.mario3d.Profile.ProfileManager;

public class App {
	
	public static final String DEFAULT_LANGUAGE = "ja-JP";
	public static final int DEFAULT_FPS = 140;
	
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
        
        Properties settings = new Properties();
        File file = new File("./setting.properties");
        if (file.exists()) {
        	try (FileInputStream fis = new FileInputStream(file);) {
        		settings.load(fis);
        	}
        	catch (IOException e) {
        		
        	}
        }
        HashSet<String> langlist = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(App.class.getResourceAsStream("/assets/text/languages.txt"), "utf-8"));) {
        	String l;
        	while ((l = reader.readLine()) != null) {
        		langlist.add(l);
        	}
        }
        catch (IOException e) {
        	
        }
        String lang = settings.getProperty("lang", "ja-JP");
        String fps_str = settings.getProperty("fps", String.valueOf(DEFAULT_FPS));
        int fps;
        if (!langlist.contains(lang)) lang = "ja-JP";
        try {
        	fps = Integer.parseInt(fps_str);
        }
        catch (NumberFormatException e) {
        	fps = DEFAULT_FPS;
        }
        
        GameManager.display_report = Boolean.parseBoolean(settings.getProperty("display-report", "false"));
        
        String export_str = settings.getProperty("export", "false");
        String export_dir_str = settings.getProperty("export.dir", "./exports");
        boolean export = Boolean.parseBoolean(export_str);
        File export_dir = null;
        if (export) export_dir = new File(export_dir_str);
        
        ProfileManager.exportDir = export_dir;
        
        GameManager.language = lang;
        Display.target_fps = fps;
        
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
