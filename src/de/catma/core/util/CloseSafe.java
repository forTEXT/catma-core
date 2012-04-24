package de.catma.core.util;

import java.io.Closeable;

public class CloseSafe {

	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (Exception exc) {
			//TODO: log
			exc.printStackTrace();
		}
	}
}
