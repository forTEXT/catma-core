package de.catma.core.document.repository;

import java.util.Properties;

import de.catma.core.tag.TagManager;

public interface RepositoryFactory {

	Repository createRepository(
		TagManager tagManager, Properties properties, int index) throws Exception;
	
}
