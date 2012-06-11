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
	public <T> void submit(String caption, ProgressCallable<T> callable,
			ExecutionListener<T> listener) {
		progressListener.setProgress(caption);
		dummy.submit(callable, listener, progressListener);
	}

}
