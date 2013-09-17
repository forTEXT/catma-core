package de.catma.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections3 {
	public static <T> Collection<T> getSetDifference(Collection<T> col1, Collection<T> col2) {
		List<T> sDiff = new ArrayList<T>();
		
		for (T t : col1) {
			if (!col2.contains(t)) {
				sDiff.add(t);
			}
		}
		
		return sDiff;
	}
}
