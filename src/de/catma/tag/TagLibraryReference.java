package de.catma.tag;

import de.catma.util.ContentInfoSet;

public class TagLibraryReference {

	private ContentInfoSet contentInfoSet;
	private String id;
	
	public TagLibraryReference(String id, ContentInfoSet contentInfoSet) {
		super();
		this.id = id;
		this.contentInfoSet = contentInfoSet;
	}
	
	@Override
	public String toString() {
		return contentInfoSet.getTitle();
	}
	
	public String getId() {
		return id;
	}
	
	public ContentInfoSet getContentInfoSet() {
		return contentInfoSet;
	}
}
