package de.catma.backgroundservice;

public class DebugBackgroundServiceProvider implements
		BackgroundServiceProvider {
	
	private BackgroundService dummy = new BackgroundService(null, false);
	private ProgressListener progressListener = new LogProgressListener();

	@Override
	public BackgroundService getBackgroundService() {
		return dummy;
	}

	@Override
	public <T> void submit(ProgressCallable<T> callable,
			ExecutionListener<T> listener) {
		dummy.submit(callable, listener, progressListener);
	}

}
