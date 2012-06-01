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
import de.catma.document.source.ISourceDocument;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.IUserMarkupCollection;
import de.catma.document.standoffmarkup.usermarkup.TagReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;
import de.catma.tag.ITagLibrary;
import de.catma.tag.TagLibraryReference;
import de.catma.tag.TagsetDefinition;
import de.catma.user.User;

public interface Repository {
	
	public static enum RepositoryChangeEvent {
		sourceDocumentChanged,
		userMarkupCollectionChanged, 
		tagLibraryChanged,
		exceptionOccurred,
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

	public void insert(ISourceDocument sourceDocument) throws IOException;
	public Collection<ISourceDocument> getSourceDocuments();
	public ISourceDocument getSourceDocument(String id);
	public void delete(ISourceDocument sourceDocument);
	
	public Set<Corpus> getCorpora();

	public void createUserMarkupCollection(String name, ISourceDocument sourceDocument) 
			throws IOException;
	public void importUserMarkupCollection(
			InputStream inputStream, ISourceDocument sourceDocument) throws IOException;
	public IUserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	public void update(
			IUserMarkupCollection userMarkupCollection, 
			List<TagReference> tagReferences);
	public void update(List<IUserMarkupCollection> userMarkupCollections,
			TagsetDefinition tagsetDefinition);
	public void delete(
			UserMarkupCollectionReference userMarkupCollectionReference) throws IOException;
	
	public StaticMarkupCollectionReference insert(
			StaticMarkupCollection staticMarkupCollection);
	public StaticMarkupCollection getStaticMarkupCollection(
			StaticMarkupCollectionReference staticMarkupCollectionReference);
	public void update(StaticMarkupCollection staticMarkupCollection);
	public void delete(StaticMarkupCollection staticMarkupCollection);
	
	public void createTagLibrary(String name) throws IOException;
	public void importTagLibrary(InputStream inputStream) throws IOException;
	public Set<TagLibraryReference> getTagLibraryReferences();
	public ITagLibrary getTagLibrary(TagLibraryReference tagLibraryReference) 
			throws IOException;
	public void delete(TagLibraryReference tagLibraryReference) throws IOException;
	
	public boolean isAuthenticationRequired();
	public User getUser();
	
}
