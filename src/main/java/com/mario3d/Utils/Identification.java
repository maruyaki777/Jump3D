package com.mario3d.Utils;

import java.util.HashMap;

public class Identification {
	private static final HashMap<Integer, String> entityid;
	
	static {
		entityid = new HashMap<>(20);
		entityid.put(-1, "test");
		entityid.put(1, "player");
		entityid.put(2, "sat");
		entityid.put(3, "lift");
		entityid.put(4, "messagebox");
		entityid.put(5, "daruma");
		entityid.put(6, "engineer");
		entityid.put(7, "faller");
		entityid.put(8, "goal");
		entityid.put(9, "turtle");
		entityid.put(10, "trapmessagebox");
		entityid.put(11, "trapfloor");
		entityid.put(12, "signalpoint");
		entityid.put(13, "spawner");
		entityid.put(14, "checkpoint");
		entityid.put(15, "maneki");
	}
	
	public static String getEntityID(int id) {
		return Identification.entityid.get(id);
	}
}
