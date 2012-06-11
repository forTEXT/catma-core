package de.catma.backgroundservice;

public interface BackgroundServiceProvider {
	public BackgroundService getBackgroundService();
	
	public <T> void submit( 
			String caption,
			ProgressCallable<T> callable, 
			ExecutionListener<T> listener);
}
