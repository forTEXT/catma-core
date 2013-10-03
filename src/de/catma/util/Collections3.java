package de.catma.util;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	public static <T> Collection<T> getUnion(Collection<T> col1, Collection<T> col2) {
		List<T> union = new ArrayList<T>();
		union.addAll(col1);
		union.addAll(col2);
		return union;
	}
	
	public static <T> Collection<T> getUnion(T[] col1, T[] col2) {
		return getUnion(Arrays.asList(col1), Arrays.asList(col2));
	}
}
