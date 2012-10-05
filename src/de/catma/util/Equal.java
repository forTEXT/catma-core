package de.catma.util;

public class Equal {

	public static boolean nullSave(Object o1, Object o2) {
		if (o1 == null) {
			return (o2 == null);
		}
		else {
			if (o2 == null) {
				return false;
			}
			else {
				return o1.equals(o2);
			}
		}
	}
	
	public static boolean nonNull(Object o1, Object o2) {
		if ((o1 == null) || (o2 == null)) {
			return false;
		}
		else {
			return o1.equals(o2);
		}
	}
}
