package de.catma.core.document.repository;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.catma.core.document.Corpus;
import de.catma.core.document.source.ISourceDocument;
import de.catma.core.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.core.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.core.document.standoffmarkup.usermarkup.UserMarkupCollection;
import de.catma.core.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;
import de.catma.core.tag.TagLibrary;
import de.catma.core.tag.TagLibraryReference;
import de.catma.core.user.User;

public interface Repository {
	
	public static enum PropertyChangeEvent {
		sourceDocumentAdded,
		userMarkupCollectionAdded,
		;
	}
	
	public void addPropertyChangeListener(
			PropertyChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public void removePropertyChangeListener(
			PropertyChangeEvent propertyChangeEvent, 
			PropertyChangeListener propertyChangeListener);
	
	public String getName();
	public void open(Map<String,String> userIdentification) throws Exception;

	public Collection<ISourceDocument> getSourceDocuments();
	public ISourceDocument getSourceDocument(String id);
	public Set<Corpus> getCorpora();
	public Set<TagLibraryReference> getTagLibraryReferences();

	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference);
	
	public StaticMarkupCollection getStaticMarkupCollection(
			StaticMarkupCollectionReference staticMarkupCollectionReference);
	
	public TagLibrary getTagLibrary(TagLibraryReference tagLibraryReference);
	
	public void delete(ISourceDocument sourceDocument);
	public void delete(UserMarkupCollection userMarkupCollection);
	public void delete(StaticMarkupCollection staticMarkupCollection);
	
	public void update(ISourceDocument sourceDocument);
	public void update(
			UserMarkupCollection userMarkupCollection, 
			ISourceDocument sourceDocument) throws IOException;
	public void update(StaticMarkupCollection staticMarkupCollection);

	public void insert(ISourceDocument sourceDocument) throws IOException;
	
	public void createUserMarkupCollection(String name, ISourceDocument sourceDocument) 
			throws IOException;
	
	public StaticMarkupCollectionReference insert(
			StaticMarkupCollection staticMarkupCollection);
	
	public String getIdFromURI(URI uri);
	
	public boolean isAuthenticationRequired();
	
	public User getUser();
}
