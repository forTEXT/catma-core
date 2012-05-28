package de.catma.util;

import java.util.Random;

public class ColorConverter {
	
	private static String fillUp(String hexString) {
		if (hexString.length() < 2) {
			return "0"+hexString;
		}
		
		return hexString;
	}
	
	public static String toHex(String colorInteger) {
		return toHex(Integer.valueOf(colorInteger));
	}

	public static String toHex(int[] rgb) {
		return fillUp(Integer.toHexString(rgb[0]).toUpperCase()) 
				+ fillUp(Integer.toHexString(rgb[1]).toUpperCase()) 
				+ fillUp(Integer.toHexString(rgb[2]).toUpperCase());
	}
	
	public static String toHex(int rgb) {
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = (rgb >> 0) & 0xFF;
		return fillUp(Integer.toHexString(red).toUpperCase()) 
				+ fillUp(Integer.toHexString(green).toUpperCase()) 
				+ fillUp(Integer.toHexString(blue).toUpperCase());
	}
	
	public static String toRGBIntAsString(String hex) {
		return String.valueOf(toRGBInt(hex));
	}
	
	public static int toRGBInt(String hex) {
		if (hex.length() < 6) {
			throw new IllegalArgumentException("cannot convert " + hex);
		}
		String redString = hex.substring(0,2);
		String greenString = hex.substring(2,4);
		String blueString = hex.substring(4,6);
		
		int red = Integer.valueOf(redString, 16);
		int green = Integer.valueOf(greenString, 16);
		int blue = Integer.valueOf(blueString, 16);
		
		return ((255 & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8)  |
                ((blue & 0xFF) << 0);
	}

	public static String randomHex() {
		return toHex(getRandomColor());
	}
	
	
	private static int[] getRandomColor() {
		int i = new Random().nextInt(3);
		switch(i) {
			case 0 : {
				return new int[] { 255, 0, 0};
			}
			case 1 : {
				return new int[] { 0, 255, 0};
			}
			case 2 : {
				return new int[] { 0, 0, 255};
			}
			default : {
				return new int[] { 0, 0, 255};
			}
		}
	}
}
