package com.mario3d.Profile;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.mario3d.Scenes.GameScene.CheckPoint;
import com.mario3d.WorldSystem.WorldPosition;

public class ProfileManager {
	
	public static File exportDir = null;
	
	private static final byte[] keyword = new byte[] {80, 82, 79, 70, 73, 76, 69};
	private static final String filename = "JUMP3D_SAVEDATA.dat";
	
	private static File profile_file = null;
	
	public static void save(Profile profile) {
		if (profile_file == null) {
			return;
		}
		try (GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(profile_file));) {
			byte[] bytes = new byte[256];
			gos.write(keyword);
			if (profile.checkpoint != null) {
				bytes[0] = 1;
				gos.write(bytes, 0, 1);
				ByteBuffer.wrap(bytes, 0, 4).putInt(profile.checkpoint.priority).array();
				ByteBuffer.wrap(bytes, 4, 8).putDouble(profile.checkpoint.pos.x).array();
				ByteBuffer.wrap(bytes, 12, 8).putDouble(profile.checkpoint.pos.y).array();
				ByteBuffer.wrap(bytes, 20, 8).putDouble(profile.checkpoint.pos.z).array();
				gos.write(bytes, 0, 28);
			}
			else {
				bytes[0] = 0;
				gos.write(bytes, 0, 1);
			}
			ByteBuffer.wrap(bytes, 0, 4).putInt(profile.remaining).array();
			ByteBuffer.wrap(bytes, 4, 4).putInt(profile.deathtime).array();
			ByteBuffer.wrap(bytes, 8, 8).putLong(profile.timeplayed).array();
			ByteBuffer.wrap(bytes, 16, 4).putInt(profile.courseid).array();
			gos.write(bytes, 0, 20);
			gos.finish();
			gos.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
	public static void reset() {
		if (profile_file == null) return;
		try (FileOutputStream fos = new FileOutputStream(profile_file);) {
			fos.flush();
			fos.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
	public static Profile load() {
		File file = getFile();
		if (file.exists() && file.isFile()) {
			profile_file = file;
			try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(file));) {
				CheckPoint cp = null;
				byte[] bytes = new byte[256];
				gis.read(bytes, 0, keyword.length);
				for (int i = 0;i < keyword.length;i++) if (bytes[i] != keyword[i]) {
					gis.close();
					return null;
				}
				gis.read(bytes, 0, 1);
				if (bytes[0] == 1) {
					//優先順位
					gis.read(bytes, 0, 4);
					int priority = ByteBuffer.wrap(bytes, 0, 4).getInt();
					//座標
					gis.read(bytes, 0, 24);
					WorldPosition wp = new WorldPosition(0, 0, 0);
					wp.x = ByteBuffer.wrap(bytes, 0, 8).getDouble();
					wp.y = ByteBuffer.wrap(bytes, 8, 8).getDouble();
					wp.z = ByteBuffer.wrap(bytes, 16, 8).getDouble();
					cp = new CheckPoint(wp, priority);
				}
				gis.read(bytes, 0, 20);
				int remaining = ByteBuffer.wrap(bytes, 0, 4).getInt();
				int deathtime = ByteBuffer.wrap(bytes, 4, 4).getInt();
				long timeplayed = ByteBuffer.wrap(bytes, 8, 8).getLong();
				int courseid = ByteBuffer.wrap(bytes, 16, 4).getInt();
				Profile profile = new Profile(cp, remaining, deathtime, timeplayed, courseid);
				gis.close();
				return profile;
			}
			catch (EOFException e) {return null;}
			catch (IOException e) {e.printStackTrace();return null;}
		}
		return null;
	}
	
	private static File getFile() {
		File file = new File(".");
		String fileabs = file.getAbsolutePath();
		file = new File(fileabs.substring(0, fileabs.length() - 1)+filename);
		return file;
	}
	
	public static boolean existFile() {
		File file = getFile();
		if (file.exists() && file.isFile()) return true;
		else return false;
	}
	
	
	public static void outputResult(Profile profile) {
		if (exportDir == null) {
			return;
		}
		if (!exportDir.exists() || !exportDir.isDirectory()) exportDir.mkdirs();
		Date date = new Date();
		SimpleDateFormat filesdf = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
		File recordfile = new File(exportDir, "record_"+filesdf.format(date)+".txt");
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(recordfile), "utf-8"));) {
			bw.write("---|| Jump3D Record ||---\n\n");
			bw.write("Clear Date Time => ");
			bw.write(sdf.format(date));
			bw.write("\n\n");
			bw.write("< Game Time >\n");
			if (profile.timeplayed / 144000 > 0) bw.write(String.valueOf(profile.timeplayed / 144000) + "hours ");
			if (profile.timeplayed / 2400 > 0) bw.write(String.valueOf((profile.timeplayed % 144000) / 2400) + "minutes ");
			bw.write(String.valueOf((profile.timeplayed % 2400) / 40) + "seconds\n");
			bw.write("\n\n< Death >\n");
			bw.write(String.valueOf(profile.deathtime));
			bw.flush();
			bw.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
}
