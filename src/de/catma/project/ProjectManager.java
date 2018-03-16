package de.catma.project;

import de.catma.Pager;
import de.catma.backgroundservice.BackgroundServiceProvider;
import de.catma.user.User;

public interface ProjectManager {
	String create(String name, String description) throws Exception;

	void delete(String projectId) throws Exception;

	public User getUser();
	public Pager<ProjectReference> getProjectReferences() throws Exception;
	public ProjectReference createProject(String name, String description) throws Exception;
	public void openProject(
		ProjectReference projectReference, 
		OpenProjectListener openProjectListener, 
		BackgroundServiceProvider backgroundServiceProvider);
}
