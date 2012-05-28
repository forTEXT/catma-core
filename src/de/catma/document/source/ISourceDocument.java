package de.catma.document.source;

import java.io.IOException;
import java.util.List;

import de.catma.document.Range;
import de.catma.document.source.contenthandler.SourceContentHandler;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;

public interface ISourceDocument {

	/**
	 * @param range the range of the content
	 * @return the content between the startpoint and the endpoint of the range
	 */
	public abstract String getContent(Range range) throws IOException;

	public abstract String getContent() throws IOException;

	public abstract void addStaticMarkupCollectionReference(
			StaticMarkupCollectionReference staticMarkupCollRef);

	public abstract void addUserMarkupCollectionReference(
			UserMarkupCollectionReference userMarkupCollRef);

	public abstract String getID();

	public abstract List<StaticMarkupCollectionReference> getStaticMarkupCollectionRefs();

	public abstract List<UserMarkupCollectionReference> getUserMarkupCollectionRefs();

	public abstract UserMarkupCollectionReference getUserMarkupCollectionReference(
			String id);

	public abstract SourceContentHandler getSourceContentHandler();

	public abstract int getLength() throws IOException;

	public abstract void unload();

	public abstract boolean isLoaded();
}