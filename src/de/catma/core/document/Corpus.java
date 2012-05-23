package de.catma.core.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.catma.core.document.source.ISourceDocument;
import de.catma.core.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.core.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;

public class Corpus {

	private String name;

	private List<ISourceDocument> sourceDocuments;
	private List<StaticMarkupCollectionReference> staticMarkupCollectionRefs;
	private List<UserMarkupCollectionReference> userMarkupCollectionRefs;
	
	public Corpus(String corpusName) {
		this.name = corpusName;
		this.sourceDocuments = new ArrayList<ISourceDocument>();
		this.staticMarkupCollectionRefs = new ArrayList<StaticMarkupCollectionReference>();
		this.userMarkupCollectionRefs = new ArrayList<UserMarkupCollectionReference>();
	}

	public void addSourceDocument(ISourceDocument sourceDocument) {
		sourceDocuments.add(sourceDocument);
	}

	public void addStaticMarkupCollectionReference(
			StaticMarkupCollectionReference staticMarkupCollRef) {
		staticMarkupCollectionRefs.add(staticMarkupCollRef);
	}

	public void addUserMarkupCollectionReference(
			UserMarkupCollectionReference userMarkupCollRef) {
		userMarkupCollectionRefs.add(userMarkupCollRef);
	}
	
	@Override
	public String toString() {
		return name;
	}

	public List<ISourceDocument> getSourceDocuments() {
		return Collections.unmodifiableList(sourceDocuments);
	}

	public List<StaticMarkupCollectionReference> getStaticMarkupCollectionRefs() {
		return Collections.unmodifiableList(staticMarkupCollectionRefs);
	}

	public List<UserMarkupCollectionReference> getUserMarkupCollectionRefs() {
		return Collections.unmodifiableList(userMarkupCollectionRefs);
	}
	
	
	
}
