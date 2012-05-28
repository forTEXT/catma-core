package de.catma.document.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import de.catma.backgroundservice.BackgroundServiceProvider;
import de.catma.tag.TagManager;


public class RepositoryManager {
	
	private List<Repository> repositories;
	
	public RepositoryManager(
			BackgroundServiceProvider backgroundServiceProvider, 
			TagManager tagManager, Properties properties) throws Exception {
		
		repositories = new ArrayList<Repository>();
		
		int index=1;
		while(RepositoryPropertyKey.Repository.exists(properties, index)) {
			
			RepositoryFactory repositoryFactory =  
				(RepositoryFactory)Class.forName(
					RepositoryPropertyKey.RepositoryFactory.getProperty(properties, index),
					true, Thread.currentThread().getContextClassLoader()).newInstance();
			
			Repository repository = 
				repositoryFactory.createRepository(
						backgroundServiceProvider, tagManager, properties, index);
			
			repositories.add(repository);
			
			index++;
		}

	}

	public List<Repository> getRepositories() {
		return Collections.unmodifiableList(repositories);
	}

	public void close() {
		for (Repository r : repositories) {
			try {
				r.close();
			}
			catch (Throwable t) {
				t.printStackTrace(); //TODO: log
			}
		}
		repositories.clear();
	}
}
