package de.catma.document.standoffmarkup.usermarkup;

import de.catma.tag.TagInstance;

public class TagInstanceInfo {

	private TagInstance tagInstance;
	private UserMarkupCollection userMarkupCollection;
	private String tagPath;
	
	public TagInstanceInfo(TagInstance tagInstance,
			UserMarkupCollection userMarkupCollection, String tagPath) {
		this.tagInstance = tagInstance;
		this.userMarkupCollection = userMarkupCollection;
		this.tagPath = tagPath;
	}

	public TagInstance getTagInstance() {
		return tagInstance;
	}

	public UserMarkupCollection getUserMarkupCollection() {
		return userMarkupCollection;
	}

	public String getTagPath() {
		return tagPath;
	}
	
	
}
