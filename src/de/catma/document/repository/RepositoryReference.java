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
