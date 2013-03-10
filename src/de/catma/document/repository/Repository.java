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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.catma.document.Corpus;
import de.catma.document.source.SourceDocument;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.TagReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollection;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;
import de.catma.tag.Property;
import de.catma.tag.TagInstance;
import de.catma.tag.TagLibrary;
import de.catma.tag.TagLibraryReference;
import de.catma.tag.TagManager;
import de.catma.tag.TagsetDefinition;
import de.catma.user.User;
import de.catma.util.ContentInfoSet;

public interface Repository {
	
	public static enum RepositoryChangeEvent {
		sourceDocumentChanged,
		userMarkupCollectionChanged,
		userMarkupCollectionTagLibraryChanged,
		tagLibraryChanged,
		corpusChanged,
		exceptionOccurred, 
		repositoryChanged,
		propertyValueChanged,
		tagReferencesChanged,
		;
	}
	
	public void open(Map<String,String> userIdentification) throws Exception;
	public void reload() throws IOException;
	public void close();
	
	public void addPropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public void removePropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public String getName();
	public String getIdFromURI(URI uri);
	public String getFileURL(String sourceDocumentID, String... path);

	public void insert(SourceDocument sourceDocument) throws IOException;
	public void update(SourceDocument sourceDocument, ContentInfoSet contentInfoSet);
	public Collection<SourceDocument> getSourceDocuments();
	public SourceDocument getSourceDocument(String id);
	public void delete(SourceDocument sourceDocument) throws IOException;
	public SourceDocument getSourceDocument(UserMarkupCollectionReference umcRef);
	public void share(
			SourceDocument sourceDocument, 
			String userIdentification,
			AccessMode accessMode) throws IOException;
	
	public Collection<Corpus> getCorpora();
	public void createCorpus(String name) throws IOException;
	public void update(
		Corpus corpus, SourceDocument sourceDocument) throws IOException;
	public void update(
		Corpus corpus, UserMarkupCollectionReference userMarkupCollectionReference)
				throws IOException;
	public void update(
		Corpus corpus, StaticMarkupCollectionReference staticMarkupCollectionReference)
				throws IOException;
	public void delete(Corpus corpus) throws IOException;
	public void update(Corpus corpus, String name) throws IOException;
	public void share(Corpus corpus, String userIdentification, AccessMode accessMode) throws IOException;

	public void createUserMarkupCollection(String name, SourceDocument sourceDocument) 
			throws IOException;
	public void importUserMarkupCollection(
			InputStream inputStream, SourceDocument sourceDocument) throws IOException;
	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference, boolean refresh) throws IOException;
	public void update(
			UserMarkupCollection userMarkupCollection, 
			List<TagReference> tagReferences);
	public void update(
			TagInstance tagInstance, Property property) throws IOException;
	public void update(List<UserMarkupCollection> userMarkupCollections,
			TagsetDefinition tagsetDefinition);
	public void update(
			UserMarkupCollectionReference userMarkupCollectionReference, 
			ContentInfoSet contentInfoSet);
	public void delete(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	public void share(UserMarkupCollectionReference userMarkupCollectionRef, 
			String userIdentification, AccessMode accessMode) throws IOException;
	
	public List<UserMarkupCollectionReference> getWritableUserMarkupCollectionRefs(SourceDocument sd) throws IOException;
	
	public StaticMarkupCollectionReference insert(
			StaticMarkupCollection staticMarkupCollection);
	public StaticMarkupCollection getStaticMarkupCollection(
			StaticMarkupCollectionReference staticMarkupCollectionReference);
	public void update(StaticMarkupCollection staticMarkupCollection);
	public void delete(StaticMarkupCollection staticMarkupCollection);
	
	public void createTagLibrary(String name) throws IOException;
	public void importTagLibrary(InputStream inputStream) throws IOException;
	public Collection<TagLibraryReference> getTagLibraryReferences();
	public TagLibrary getTagLibrary(TagLibraryReference tagLibraryReference) 
			throws IOException;
	public void delete(TagLibraryReference tagLibraryReference) throws IOException;
	public void share(
			TagLibraryReference tagLibraryReference, 
			String userIdentification,
			AccessMode accessMode) throws IOException;

	
	public boolean isAuthenticationRequired();
	public User getUser();
	public TagManager getTagManager();
	
	
	
}
