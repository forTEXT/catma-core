package de.catma.project;

import de.catma.document.repository.Repository;

public interface OpenProjectListener {
	public void progress(String msg, Object... params);
	public void ready(Repository repository);
	public void failure(Throwable t);
}
