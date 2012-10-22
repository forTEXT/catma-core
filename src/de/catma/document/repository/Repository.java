package de.catma.document.repository;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.catma.document.Corpus;
import de.catma.document.source.SourceDocument;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollection;
import de.catma.document.standoffmarkup.usermarkup.TagReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;
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
		;
	}
	
	public void open(Map<String,String> userIdentification) throws Exception;
	public void close();
	
	public void addPropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public void removePropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public String getName();
	public String getIdFromURI(URI uri);

	public void insert(SourceDocument sourceDocument) throws IOException;
	public void update(SourceDocument sourceDocument, ContentInfoSet contentInfoSet);
	public Collection<SourceDocument> getSourceDocuments();
	public SourceDocument getSourceDocument(String id);
	public void delete(SourceDocument sourceDocument) throws IOException;
	public SourceDocument getSourceDocument(UserMarkupCollectionReference umcRef);

	public Set<Corpus> getCorpora();
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

	public void createUserMarkupCollection(String name, SourceDocument sourceDocument) 
			throws IOException;
	public void importUserMarkupCollection(
			InputStream inputStream, SourceDocument sourceDocument) throws IOException;
	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	public void update(
			UserMarkupCollection userMarkupCollection, 
			List<TagReference> tagReferences);
	public void update(List<UserMarkupCollection> userMarkupCollections,
			TagsetDefinition tagsetDefinition);
	public void update(
			UserMarkupCollectionReference userMarkupCollectionReference, 
			ContentInfoSet contentInfoSet);
	public void delete(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	
	public List<UserMarkupCollectionReference> getWritableUserMarkupCollectionRefs(SourceDocument sd) throws IOException;
	
	public StaticMarkupCollectionReference insert(
			StaticMarkupCollection staticMarkupCollection);
	public StaticMarkupCollection getStaticMarkupCollection(
			StaticMarkupCollectionReference staticMarkupCollectionReference);
	public void update(StaticMarkupCollection staticMarkupCollection);
	public void delete(StaticMarkupCollection staticMarkupCollection);
	
	public void createTagLibrary(String name) throws IOException;
	public void importTagLibrary(InputStream inputStream) throws IOException;
	public Set<TagLibraryReference> getTagLibraryReferences();
	public TagLibrary getTagLibrary(TagLibraryReference tagLibraryReference) 
			throws IOException;
	public void delete(TagLibraryReference tagLibraryReference) throws IOException;
	
	public boolean isAuthenticationRequired();
	public User getUser();
	public TagManager getTagManager();
	
	
	
}
