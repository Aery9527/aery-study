package org.aery.study.java.io.tool;

public class RandomID {

	public static String get() {
		String id = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
		return get(id.substring(id.length() - 4));
	}

	public static String get(String id) {
		return "RID(" + id + ")";
	}

}
