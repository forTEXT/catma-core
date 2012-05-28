package de.catma.document.repository;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.catma.document.Corpus;
import de.catma.document.source.ISourceDocument;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.IUserMarkupCollection;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;
import de.catma.tag.ITagLibrary;
import de.catma.tag.TagLibraryReference;
import de.catma.user.User;

public interface Repository {
	
	public static enum RepositoryChangeEvent {
		sourceDocumentAdded,
		userMarkupCollectionAdded, 
		tagLibraryChanged,
		exceptionOccurred,
		;
	}
	public void open(Map<String,String> userIdentification) throws Exception;
	
	public void addPropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public void removePropertyChangeListener(
			RepositoryChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public String getName();
	public String getIdFromURI(URI uri);

	public Collection<ISourceDocument> getSourceDocuments();
	public ISourceDocument getSourceDocument(String id);
	
	public Set<Corpus> getCorpora();

	public Set<TagLibraryReference> getTagLibraryReferences();
	public ITagLibrary getTagLibrary(TagLibraryReference tagLibraryReference) 
			throws IOException;

	public IUserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference);
	
	public StaticMarkupCollection getStaticMarkupCollection(
			StaticMarkupCollectionReference staticMarkupCollectionReference);
	
	
	public void delete(ISourceDocument sourceDocument);
	public void delete(IUserMarkupCollection userMarkupCollection);
	public void delete(StaticMarkupCollection staticMarkupCollection);
	public void delete(TagLibraryReference tagLibraryReference) throws IOException;
	
	public void update(ISourceDocument sourceDocument);
	public void update(
			IUserMarkupCollection userMarkupCollection, 
			ISourceDocument sourceDocument) throws IOException;
	public void update(StaticMarkupCollection staticMarkupCollection);

	public void insert(ISourceDocument sourceDocument) throws IOException;
	public StaticMarkupCollectionReference insert(
			StaticMarkupCollection staticMarkupCollection);
	
	public void createUserMarkupCollection(String name, ISourceDocument sourceDocument) 
			throws IOException;
	
	public void createTagLibrary(String name) throws IOException;
	public void importTagLibrary(InputStream inputStream) throws IOException;
	
	public boolean isAuthenticationRequired();
	
	public User getUser();
	
	public void close();


}
