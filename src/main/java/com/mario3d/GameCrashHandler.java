package com.mario3d;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.event.WindowEvent;

public class GameCrashHandler {
	
	public static GameCrashHandler INSTANCE = null;
	
	private GameCrashHandler(Exception e, Class<?> cls) {
		StringBuilder sb = new StringBuilder();
		Throwable ex = e;
		do {
			sb.append(e.toString());sb.append('\n');
			for (StackTraceElement ste : e.getStackTrace()) {
				sb.append("    ");
				sb.append(ste.toString());
				sb.append('\n');
			}
		}while ((ex = ex.getCause()) != null);
		String msg = sb.toString();
		JFrame frame = new JFrame();
		frame.setTitle("Jump3D Crash Report");
		frame.setResizable(false);
		frame.addWindowListener(this.new CrashWindowAdapter());
		Container c = frame.getContentPane();
		JPanel panel = new JPanel();
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.setText(msg);
		JLabel l = new JLabel("Jump3D Crashed. Sorry!");
		l.setFont(new Font("Tahoma", Font.PLAIN, 20));
		JPanel errorpanel = new JPanel();
		errorpanel.setLayout(new BorderLayout());
		panel.add("North", l);
		errorpanel.add("South", ta);
		errorpanel.setBorder(new TitledBorder(new EtchedBorder(), "Error Message"));
		
		JPanel infopanel = new JPanel();
		infopanel.setBorder(new TitledBorder(new EtchedBorder(), "Info"));
		infopanel.setLayout(new BoxLayout(infopanel, BoxLayout.Y_AXIS));
		
		JLabel version = new JLabel("Version : " + App.version);
		JLabel caughtclass = new JLabel("Caught Class : " + cls.getName());
		infopanel.add(version);
		infopanel.add(caughtclass);
		c.setLayout(new BorderLayout());
		c.add("North", panel);
		c.add("West", infopanel);
		c.add("South", errorpanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	private class CrashWindowAdapter extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(-1);
		}
	}
	
	public static void displayException(Exception e, Class<?> c) {
		GameCrashHandler gch = new GameCrashHandler(e, c);
		INSTANCE = gch;
		try {
			gch.wait();
		}
		catch (InterruptedException e1) {
			System.exit(-1);
		}
	}
}
