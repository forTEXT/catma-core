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

import de.catma.backgroundservice.BackgroundServiceProvider;
import de.catma.tag.TagManager;

/**
 * Creates a repository.
 * 
 * @author marco.petris@web.de
 *
 */
public interface RepositoryFactory {

	/**
	 * @param backgroundServiceProvider 
	 * @param tagManager
	 * @param properties Properties with keys from see {@link RepositoryPropertyKey}.
	 * @param index the index of the index-based properties, see {@link RepositoryPropertyKey}. 
	 * @return the new Repository
	 * @throws Exception
	 */
	Repository createRepository(
		BackgroundServiceProvider backgroundServiceProvider, TagManager tagManager, 
		Properties properties, int index) throws Exception;
	
}
