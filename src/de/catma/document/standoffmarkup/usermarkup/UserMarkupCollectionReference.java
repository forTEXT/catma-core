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
package de.catma.document.standoffmarkup.usermarkup;

import de.catma.document.standoffmarkup.MarkupCollectionReference;
import de.catma.util.ContentInfoSet;

public class UserMarkupCollectionReference 
	implements MarkupCollectionReference {
	
	private String id;
	private ContentInfoSet contentInfoSet;
	
	public UserMarkupCollectionReference(String id,
			ContentInfoSet contentInfoSet) {
		this.id = id;
		this.contentInfoSet = contentInfoSet;
	}

	@Override
	public String toString() {
		return contentInfoSet.getTitle();
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return contentInfoSet.getTitle();
	}
	
	public ContentInfoSet getContentInfoSet() {
		return contentInfoSet;
	}
	
	public void setContentInfoSet(ContentInfoSet contentInfoSet) {
		this.contentInfoSet = contentInfoSet;
	}
}
