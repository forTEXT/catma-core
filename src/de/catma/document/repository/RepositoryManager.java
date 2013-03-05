/*   
 *   CATMA Computer Aided Text Markup and Analysis
 *   
 *   Copyright (C) 2009-2013  University Of Hamburg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.catma.document.repository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import de.catma.backgroundservice.BackgroundServiceProvider;
import de.catma.tag.TagManager;


public class RepositoryManager {
	public static enum RepositoryManagerEvent {
		repositoryStateChange,
		;
	}
	private BackgroundServiceProvider backgroundServiceProvider;
	private TagManager tagManager;

	private Set<RepositoryReference> repositoryReferences;
	private Set<Repository> openRepositories;
	private PropertyChangeSupport propertyChangeSupport;
	
	public RepositoryManager(
			BackgroundServiceProvider backgroundServiceProvider, 
			TagManager tagManager, Properties properties) throws Exception {
		propertyChangeSupport = new PropertyChangeSupport(this);
		this.backgroundServiceProvider = backgroundServiceProvider;
		this.tagManager = tagManager;
		
		repositoryReferences = new TreeSet<RepositoryReference>(
				new Comparator<RepositoryReference>() {
			@Override
			public int compare(RepositoryReference o1, RepositoryReference o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		openRepositories = new HashSet<Repository>();
		
		int index=1;
		while(RepositoryPropertyKey.Repository.exists(properties, index)) {
			
			RepositoryFactory repositoryFactory =  
				(RepositoryFactory)Class.forName(
					RepositoryPropertyKey.RepositoryFactory.getProperty(properties, index),
					true, Thread.currentThread().getContextClassLoader()).newInstance();
			
			repositoryReferences.add(
					new RepositoryReference(
						repositoryFactory, properties, index));
			
			index++;
		}

	}
	
	

	public void addPropertyChangeListener(RepositoryManagerEvent propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName.name(), listener);
	}



	public void removePropertyChangeListener(RepositoryManagerEvent propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName.name(),
				listener);
	}



	public Set<RepositoryReference> getRepositoryReferences() {
		return Collections.unmodifiableSet(repositoryReferences);
	}

	public Repository openRepository(
			RepositoryReference repositoryReference, 
			Map<String, String> userIdentification) throws Exception {
		Repository repository = 
				repositoryReference.getRepositoryFactory().createRepository(
				backgroundServiceProvider, tagManager,
				repositoryReference.getProperties(),
				repositoryReference.getIndex());
		
		repository.open(userIdentification);
		
		openRepositories.add(repository);
		propertyChangeSupport.firePropertyChange(
				RepositoryManagerEvent.repositoryStateChange.name(), null, repository);
		return repository;
	}
	
	public void close(Repository repository) {
		openRepositories.remove(repository);
		repository.close();
		propertyChangeSupport.firePropertyChange(
				RepositoryManagerEvent.repositoryStateChange.name(), repository, null);
	}
	
	public void close() {
		for (Repository r : openRepositories) {
			try {
				r.close();
			}
			catch (Throwable t) {
				t.printStackTrace(); //TODO: log
			}
		}
		openRepositories.clear();
		repositoryReferences.clear();
	}

	public boolean isOpen(RepositoryReference repositoryReference) {
		for (Repository r : openRepositories) {
			if (r.getName().equals(repositoryReference.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOpenRepository() {
		return !openRepositories.isEmpty();
	}
}
