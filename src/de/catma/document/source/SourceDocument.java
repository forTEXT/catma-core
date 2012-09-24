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
	
	public void setId(String id) {
		this.id = id;
	}
}