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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.catma.document.AccessMode;
import de.catma.document.Corpus;
import de.catma.document.source.ContentInfoSet;
import de.catma.document.source.SourceDocument;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.TagReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollection;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;
import de.catma.serialization.UserMarkupCollectionSerializationHandler;
import de.catma.tag.Property;
import de.catma.tag.TagInstance;
import de.catma.tag.TagLibrary;
import de.catma.tag.TagLibraryReference;
import de.catma.tag.TagManager;
import de.catma.tag.TagsetDefinition;
import de.catma.tag.Version;
import de.catma.user.User;
import de.catma.util.Pair;

/**
 * A repository to store {@link SourceDocument}s, {@link UserMarkupCollection}s and
 * {@link TagLibrary TagLibraries}.
 * 
 * @author marco.petris@web.de
 *
 */
public interface Repository {
	
	/**
	 * The Repository emits these change events to listeners that have
	 * been registered with {@link Repository#addPropertyChangeListener(RepositoryChangeEvent, PropertyChangeListener)}.
	 *
	 */
	/**
	 * @author marco.petris@web.de
	 *
	 */
	public static enum RepositoryChangeEvent {
		/**
		 * <p>{@link SourceDocument} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link SourceDocument#getID()}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link SourceDocument} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link SourceDocument}</li>
		 * </p><br />
		 * <p>{@link SourceDocument} Metadata changed or a document reload has taken place
		 * <li>{@link PropertyChangeEvent#getNewValue()} = new {@link SourceDocument}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link SourceDocument#getID()}</li>
		 * </p>
		 */
		sourceDocumentChanged,
		/**
		 * <p>{@link UserMarkupCollection} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = a {@link Pair} of 
		 * {@link UserMarkupCollectionReference} and corresponding {@link SourceDocument}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link UserMarkupCollection} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link UserMarkupCollection}</li>
		 * </p><br />
		 * <p>{@link UserMarkupCollection} Metadata changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link UserMarkupCollectionReference}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = old {@link ContentInfoSet}</li>
		 * </p>
		 */
		userMarkupCollectionChanged,
		/**
		 * Updates on the User Markup Collection's inner Tag Library.
		 * <li>{@link PropertyChangeEvent#getNewValue()} =
		 *  {@link java.util.List List} of updated {@link UserMarkupCollection}s</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = changed {@link TagsetDefinition}</li>
		 */
		userMarkupCollectionTagLibraryChanged,
		/**
		 * <p>{@link TagLibrary} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = a {@link Pair} of 
		 * {@link TagLibraryReference} and corresponding {@link SourceDocument}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link TagLibrary} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link TagLibraryReference}</li>
		 * </p><br />
		 * <p>{@link TagLibrary} Metadata changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link TagLibraryReference}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = old {@link ContentInfoSet}</li>
		 * </p>
		 */
		tagLibraryChanged,
		/**
		 * <p>{@link Corpus} added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link Corpus}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link Corpus} removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link SourceDocument}</li>
		 * </p><br />
		 * <p>{@link SourceDocument} added to {@link Corpus}:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link Corpus}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link SourceDocument}</li>
		 * </p>
		 * <p>{@link UserMarkupCollection} added to {@link Corpus}:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link Corpus}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link UserMarkupCollectionReference}</li>
		 * </p>
		 * <p>{@link Corpus} name changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link Corpus}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = the new name</li>
		 * </p>
		 * <p>{@link Corpus} reload:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = current {@link Corpus}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = old {@link Corpus}</li>
		 * </p>
		 */
		corpusChanged,
		/**
		 * Signals an exception:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = the {@link Exception}</li>
		 */
		exceptionOccurred, 
		/**
		 * <p>{@link Property} changed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} =  {@link List} of {@link Property Properties}</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = corresponding {@link TagInstance}</li>
		 * </p>
		 */
		propertyValueChanged,
		/**
		 * <p>{@link TagReference}s added:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = {@link List} of {@link TagReference}s</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = <code>null</code></li>
		 * </p><br />
		 * <p>{@link TagReference}s removed:
		 * <li>{@link PropertyChangeEvent#getNewValue()} = <code>null</code></li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = {@link List} of {@link TagReference}s</li>
		 * </p><br />
		 */
		tagReferencesChanged,
		/**
		 * <p> a notification to the repo holder.
		 * <li>{@link PropertyChangeEvent#getNewValue()} = the message of type String</li>
		 * <li>{@link PropertyChangeEvent#getOldValue()} = always <code>null</code></li>
		 * </p>
		 */
		notification,
		;
	}
	
	/**
	 * @param userIdentification - a key value map that stores information to identify the user
	 * @throws Exception any repository error
	 */
	public void open(Map<String,String> userIdentification) throws Exception;
	/**
	 * Reloads information about available {@link SourceDocument}s, {@link UserMarkupCollection}s and
	 * {@link TagLibary TagLibraries}.
	 * @throws IOException any repository error
	 */
	public void reload() throws IOException;
	public void close();
	
	/**
	 * @param propertyChangeEvent event to listen for
	 * @param propertyChangeListener
	 */
	public void addPropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public void removePropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	/**
	 * @return name of the repository
	 */
	public String getName();
	/**
	 * @param uri may be used for ID creation
	 * @return an ID for the given URI, subsequent calls can produce different results
	 */
	public String getIdFromURI(URI uri);
	/**
	 * @param catmaID the {@link SourceDocument#getID()}
	 * @param path a list of path elements
	 * @return the constructed file url
	 */
	public String getFileURL(String sourceDocumentID, String... path);

	/**
	 * Inserts the given SourceDocument into the repository.
	 * @param sourceDocument
	 * @throws IOException
	 */
	public void insert(SourceDocument sourceDocument) throws IOException;
	/**
	 * @param sourceDocument document to be updated
	 * @param contentInfoSet new meta data
	 */
	public void update(SourceDocument sourceDocument, ContentInfoSet contentInfoSet);
	/**
	 * @return the available Source Documents
	 */
	public Collection<SourceDocument> getSourceDocuments();
	/**
	 * @param id ID of the SourceDocument
	 * @return the SourceDocument with the given ID
	 */
	public SourceDocument getSourceDocument(String id);
	public void delete(SourceDocument sourceDocument) throws IOException;
	/**
	 * @param umcRef
	 * @return the SourceDocument that belongs to the given UserMarkupCollection
	 */
	public SourceDocument getSourceDocument(UserMarkupCollectionReference umcRef);
	/**
	 * Shares the given SourceDocument with the specified acces mode to the user 
	 * identified by userIdentification.
	 * 
	 * @param sourceDocument
	 * @param userIdentification
	 * @param accessMode
	 * @throws IOException
	 */
	public void share(
			SourceDocument sourceDocument, 
			String userIdentification,
			AccessMode accessMode) throws IOException;
	
	/**
	 * @return the available corpora
	 */
	public Collection<Corpus> getCorpora();
	/**
	 * @param name the name of the new corpus
	 * @throws IOException
	 */
	public void createCorpus(String name) throws IOException;
	/**
	 * Adds the Source Document to the Corpus.
	 * @param corpus
	 * @param sourceDocument
	 * @throws IOException
	 */
	public void update(
		Corpus corpus, SourceDocument sourceDocument) throws IOException;
	/**
	 * Adds the User Markup Collection to the Corpus.
	 * @param corpus
	 * @param userMarkupCollectionReference
	 * @throws IOException
	 */
	public void update(
		Corpus corpus, UserMarkupCollectionReference userMarkupCollectionReference)
				throws IOException;
	/**
	 * Adds the Static Markup Collection to the corpus.
	 * @param corpus
	 * @param staticMarkupCollectionReference
	 * @throws IOException
	 */
	public void update(
		Corpus corpus, StaticMarkupCollectionReference staticMarkupCollectionReference)
				throws IOException;
	public void delete(Corpus corpus) throws IOException;
	/**
	 * Changes the name of the corpus.
	 * @param corpus
	 * @param name
	 * @throws IOException
	 */
	public void update(Corpus corpus, String name) throws IOException;
	/**
	 * Shares the given Corpus with the specified acces mode to the user 
	 * identified by userIdentification.
	 * @param corpus
	 * @param userIdentification
	 * @param accessMode
	 * @throws IOException
	 */
	public void share(Corpus corpus, String userIdentification, AccessMode accessMode) throws IOException;

	/**
	 * Creates a User Markup Collection with that name for the given Source Document.
	 * @param name
	 * @param sourceDocument
	 * @throws IOException
	 */
	public void createUserMarkupCollection(String name, SourceDocument sourceDocument) 
			throws IOException;
	/**
	 * Imports a User Markup Collection.
	 * @param inputStream the User Markup Collection
	 * @param sourceDocument the corresponding Source Document
	 * @throws IOException
	 */
	public void importUserMarkupCollection(
			InputStream inputStream, SourceDocument sourceDocument) throws IOException;
	
	public void importUserMarkupCollection(
			InputStream inputStream,
			final SourceDocument sourceDocument, 
			UserMarkupCollectionSerializationHandler userMarkupCollectionSerializationHandler) 
					throws IOException;
	/**
	 * @param userMarkupCollectionReference
	 * @return the User Markup Collection for the given reference.
	 * @throws IOException
	 */
	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	/**
	 * @param userMarkupCollectionReference
	 * @param refresh true -> do not use cached values
	 * @return the corresponding collection
	 * @throws IOException
	 */
	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference, boolean refresh) throws IOException;
	/**
	 * Add the Tag References to the given User Markup Collection or remove the 
	 * given Tag References from the User Markup Collection.
	 * @param userMarkupCollection
	 * @param tagReferences
	 */
	public void update(
			UserMarkupCollection userMarkupCollection, 
			List<TagReference> tagReferences);
	/**
	 * Updates the given Properties in the Tag Instance.
	 * @param tagInstance
	 * @param property 
	 * @throws IOException
	 */
	public void update(
			TagInstance tagInstance, Collection<Property> properties) throws IOException;
	/**
	 * Updates the give TagsetDefinition within the list of User Markup Collections.
	 * @param userMarkupCollections
	 * @param tagsetDefinition
	 */
	public void update(List<UserMarkupCollection> userMarkupCollections,
			TagsetDefinition tagsetDefinition);
	/**
	 * Updates the User Markup Collection's metadata.
	 * @param userMarkupCollectionReference
	 * @param contentInfoSet metadata
	 */
	public void update(
			UserMarkupCollectionReference userMarkupCollectionReference, 
			ContentInfoSet contentInfoSet);
	public void delete(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	/**
	 * Shares the given User Markup Collection with the specified acces mode to the user 
	 * identified by userIdentification.
	 * @param userMarkupCollectionRef
	 * @param userIdentification
	 * @param accessMode
	 * @throws IOException
	 */
	public void share(UserMarkupCollectionReference userMarkupCollectionRef, 
			String userIdentification, AccessMode accessMode) throws IOException;
	
	/**
	 * @param sd
	 * @return a list of User Markup Collections belonging to the given Source Document
	 * with {@link AccessMode#WRITE write} access mode. 
	 * @throws IOException
	 */
	public List<UserMarkupCollectionReference> getWritableUserMarkupCollectionRefs(SourceDocument sd) throws IOException;
	
	public StaticMarkupCollectionReference insert(
			StaticMarkupCollection staticMarkupCollection);
	public StaticMarkupCollection getStaticMarkupCollection(
			StaticMarkupCollectionReference staticMarkupCollectionReference);
	public void update(StaticMarkupCollection staticMarkupCollection);
	public void delete(StaticMarkupCollection staticMarkupCollection);
	
	/**
	 * @param name the name of the Tag Library
	 * @throws IOException
	 */
	public void createTagLibrary(String name) throws IOException;
	/**
	 * @param inputStream the tag library
	 * @throws IOException
	 */
	public void importTagLibrary(InputStream inputStream) throws IOException;
	/**
	 * @return the available Tag Libraries.
	 */
	public Collection<TagLibraryReference> getTagLibraryReferences();
	public TagLibrary getTagLibrary(TagLibraryReference tagLibraryReference) 
			throws IOException;
	public void delete(TagLibraryReference tagLibraryReference) throws IOException;
	/**
	 * Shares the given Tag Library with the specified acces mode to the user 
	 * identified by userIdentification.
	 * @param tagLibraryReference
	 * @param userIdentification
	 * @param accessMode
	 * @throws IOException
	 */
	public void share(
			TagLibraryReference tagLibraryReference, 
			String userIdentification,
			AccessMode accessMode) throws IOException;

	
	/**
	 * @return if true then users have to authenticate for access to the repository
	 */
	public boolean isAuthenticationRequired();
	/**
	 * @return current user of this repository instance
	 */
	public User getUser();
	
	/**
	 * @return the Tag Manager for this repository
	 */
	public TagManager getTagManager();
	
	
	/**
	 * @param sourceDocument
	 * @return the file object that belongs to the given SourceDocument
	 */
	public File getFile(SourceDocument sourceDocument);
	
	public int getNewUserMarkupCollectionRefs(Corpus corpus);
	
	public void spawnContentFrom(String userIdentifier, boolean copyCorpora, boolean copyTagLibs) throws IOException;
	public TagLibrary getTagLibraryFor(String uuid, Version version) throws IOException;
	
	public User createIfAbsent(Map<String, String> userIdentification) throws IOException;
	
}
