package de.catma.core.document.repository;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Set;

import de.catma.core.document.Corpus;
import de.catma.core.document.source.SourceDocument;
import de.catma.core.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.core.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.core.document.standoffmarkup.usermarkup.UserMarkupCollection;
import de.catma.core.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;
import de.catma.core.tag.TagLibrary;
import de.catma.core.tag.TagLibraryReference;

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
	public void open() throws Exception;

	public Collection<SourceDocument> getSourceDocuments();
	public SourceDocument getSourceDocument(String id);
	public Set<Corpus> getCorpora();
	public Set<TagLibraryReference> getTagLibraryReferences();

	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference);
	
	public StaticMarkupCollection getStaticMarkupCollection(
			StaticMarkupCollectionReference staticMarkupCollectionReference);
	
	public TagLibrary getTagLibrary(TagLibraryReference tagLibraryReference);
	
	public void delete(SourceDocument sourceDocument);
	public void delete(UserMarkupCollection userMarkupCollection);
	public void delete(StaticMarkupCollection staticMarkupCollection);
	
	public void update(SourceDocument sourceDocument);
	public void update(
			UserMarkupCollection userMarkupCollection, 
			SourceDocument sourceDocument) throws IOException;
	public void update(StaticMarkupCollection staticMarkupCollection);

	public void insert(SourceDocument sourceDocument) throws IOException;
	
	public void createUserMarkupCollection(String name, SourceDocument sourceDocument) 
			throws IOException;
	
	public StaticMarkupCollectionReference insert(
			StaticMarkupCollection staticMarkupCollection);
	
	public String createIdFromURI(URI uri);
	 
}
