package de.catma.backgroundservice;

import java.util.Arrays;


public class LogProgressListener implements ProgressListener {

	public void setProgress(String value, Object... args) {
		System.out.println(
			value + ((args != null)? (" " +Arrays.toString(args)):""));
	}

	public void setException(Throwable t) {
		t.printStackTrace();
	}

}
