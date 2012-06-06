package de.catma.document.repository;

import java.util.Properties;

public class RepositoryReference {
	
	private RepositoryFactory repositoryFactory;
	private Properties properties;
	private int index;
	private String name;
	private boolean authenticationRequired;
	
	public RepositoryReference(RepositoryFactory repositoryFactory,
			Properties properties, int index) {
		this.repositoryFactory = repositoryFactory;
		this.properties = properties;
		this.index = index;
		this.name = 
			RepositoryPropertyKey.Repository.getProperty(properties, index);
		this.authenticationRequired = 
			RepositoryPropertyKey.RepositoryAuthenticationRequired.isTrue(
					properties, index, true);
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public RepositoryFactory getRepositoryFactory() {
		return repositoryFactory;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public boolean isAuthenticationRequired() {
		return authenticationRequired;
	}
}
