package de.catma.document.repository;

import java.util.Properties;

import de.catma.backgroundservice.BackgroundServiceProvider;
import de.catma.tag.TagManager;

public interface RepositoryFactory {

	Repository createRepository(
		BackgroundServiceProvider backgroundServiceProvider, TagManager tagManager, 
		Properties properties, int index) throws Exception;
	
}
