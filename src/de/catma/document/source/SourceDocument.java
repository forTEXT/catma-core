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
package de.catma.document.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.catma.document.Range;
import de.catma.document.source.contenthandler.SourceContentHandler;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;

public class SourceDocument {

	private String id;
	private SourceContentHandler sourceContentHandler;
	private List<StaticMarkupCollectionReference> staticMarkupCollectionRefs;
	private List<UserMarkupCollectionReference> userMarkupCollectionRefs;
	private Integer length = null;
	
	SourceDocument(String id, SourceContentHandler handler) {
		this.id = id;
		this.sourceContentHandler = handler;
		this.staticMarkupCollectionRefs = new ArrayList<StaticMarkupCollectionReference>();
		this.userMarkupCollectionRefs = new ArrayList<UserMarkupCollectionReference>();
	}

	/**
	 * Displays the title of the document.
	 */
	@Override
	public String toString() {
		String title = 
				sourceContentHandler.getSourceDocumentInfo().getContentInfoSet().getTitle();
		return ((title == null) || (title.isEmpty()))? id : title;
	}

	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getContent(de.catma.core.document.Range)
	 */
	public String getContent( Range range ) throws IOException {
		int length = getContent().length();
		return getContent().substring(
				Math.min(range.getStartPoint(), length), 
				Math.min(range.getEndPoint(), length));
	}
	
	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getContent()
	 */
	public String getContent() throws IOException {
		return sourceContentHandler.getContent();
	}

	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#addStaticMarkupCollectionReference(de.catma.core.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference)
	 */
	public void addStaticMarkupCollectionReference(
			StaticMarkupCollectionReference staticMarkupCollRef) {
		staticMarkupCollectionRefs.add(staticMarkupCollRef);
	}

	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#addUserMarkupCollectionReference(de.catma.core.document.standoffmarkup.usermarkup.UserMarkupCollectionReference)
	 */
	public void addUserMarkupCollectionReference(
			UserMarkupCollectionReference userMarkupCollRef) {
		userMarkupCollectionRefs.add(userMarkupCollRef);
	}

	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getID()
	 */
	public String getID() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getStaticMarkupCollectionRefs()
	 */
	public List<StaticMarkupCollectionReference> getStaticMarkupCollectionRefs() {
		return Collections.unmodifiableList(staticMarkupCollectionRefs);
	}
	
	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getUserMarkupCollectionRefs()
	 */
	public List<UserMarkupCollectionReference> getUserMarkupCollectionRefs() {
		return Collections.unmodifiableList(userMarkupCollectionRefs);
	}
	
	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getUserMarkupCollectionReference(java.lang.String)
	 */
	public UserMarkupCollectionReference getUserMarkupCollectionReference(String id) {
		for (UserMarkupCollectionReference ref : userMarkupCollectionRefs) {
			if (ref.getId().equals(id)) {
				return ref;
			}
		}
		return null;
	}
	
	public boolean removeUserMarkupCollectionReference(
			UserMarkupCollectionReference uRef) {
		return this.userMarkupCollectionRefs.remove(uRef);
	}
	
	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getSourceContentHandler()
	 */
	public SourceContentHandler getSourceContentHandler() {
		return sourceContentHandler;
	}

	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#getLength()
	 */
	public int getLength() throws IOException {
		if (length == null) {
			length = getContent().length();
		}
		return length;
	}
	
	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#unload()
	 */
	public void unload() {
		sourceContentHandler.unload();
	}

	/* (non-Javadoc)
	 * @see de.catma.core.document.source.ISourceDocument#isLoaded()
	 */
	public boolean isLoaded() {
		return sourceContentHandler.isLoaded();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SourceDocument other = (SourceDocument) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	
}