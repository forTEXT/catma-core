package de.catma.project;

import java.util.List;

import de.catma.document.repository.Repository;
import de.catma.user.User;

public interface ProjectManager {
	public User getUser();
	public List<ProjectReference> getProjectReferences() throws Exception;
	public ProjectReference createProject(String name, String description);
	public Repository openProject(ProjectReference projectReference);
}
