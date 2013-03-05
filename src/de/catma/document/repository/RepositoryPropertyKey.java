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

public enum RepositoryPropertyKey {
	Repository,
	SerializationHandlerFactory,
	RepositoryFolderPath,
	RepositoryFactory, 
	RepositoryAuthenticationRequired,
	RepositoryUrl,
	RepositoryUser,
	RepositoryPass,
	IndexerFactory, 
	IndexerUrl, 
	TempDir,
	;

	public boolean isTrue(Properties properties, int index, boolean defaultValue) {
		if (properties.containsKey(this.name()+index)) {
			return isTrue(properties, index);
		}
		else {
			return defaultValue;
		}
	}
	
	public boolean isTrue(Properties properties, int index) {
		return Boolean.parseBoolean(properties.getProperty(this.name()+index));
	}
	
	public String getProperty(Properties properties, int index) {
		return properties.getProperty(this.name()+index);
	}
	
	public boolean exists(Properties properties, int index) {
		return properties.containsKey(this.name()+index);
	}
}
