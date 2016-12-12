package org.uni.illuxplain;

public class Utils {

	public static String striper(String data) {

		return data.substring(0, data.indexOf("@"));
	}

	public static String oneToManyStriper(String data) {
		String usernameStripper = data.substring(0, data.indexOf("_"));
		return usernameStripper;
	}
	
	public static String ManyToManyStriper(String data) {
		String usernameStripper = data.substring(data.indexOf("/") +1);
		return usernameStripper;
	}
	
}
