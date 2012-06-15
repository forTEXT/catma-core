package de.catma.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.catma.document.source.SourceDocument;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollectionReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollectionReference;

public class Corpus {

	private String id;
	private String name;

	private List<SourceDocument> sourceDocuments;
	private List<StaticMarkupCollectionReference> staticMarkupCollectionRefs;
	private List<UserMarkupCollectionReference> userMarkupCollectionRefs;
	
	public Corpus(String id, String corpusName) {
		this.id = id;
		this.name = corpusName;
		this.sourceDocuments = new ArrayList<SourceDocument>();
		this.staticMarkupCollectionRefs = new ArrayList<StaticMarkupCollectionReference>();
		this.userMarkupCollectionRefs = new ArrayList<UserMarkupCollectionReference>();
	}

	public Corpus(String corpusName) {
		this(null, corpusName);
	}

	public void addSourceDocument(SourceDocument sourceDocument) {
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

	public List<SourceDocument> getSourceDocuments() {
		return Collections.unmodifiableList(sourceDocuments);
	}

	public List<StaticMarkupCollectionReference> getStaticMarkupCollectionRefs() {
		return Collections.unmodifiableList(staticMarkupCollectionRefs);
	}

	public List<UserMarkupCollectionReference> getUserMarkupCollectionRefs() {
		return Collections.unmodifiableList(userMarkupCollectionRefs);
	}
	
	public String getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
