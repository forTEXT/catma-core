package de.catma.core.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.catma.core.tag.TagManager;
import de.catma.core.tag.TagsetDefinition;

public class UserMarkupCollectionManager implements Iterable<UserMarkupCollection>{
	

	private TagManager tagManager;
	private List<UserMarkupCollection> userMarkupCollections;

	public UserMarkupCollectionManager(TagManager tagManager) {
		this.tagManager = tagManager;
		userMarkupCollections = new ArrayList<UserMarkupCollection>();
	}
	

	public List<UserMarkupCollection> updateUserMarkupCollections(
			TagsetDefinition tagsetDefinition) {
		List<UserMarkupCollection> modified = new ArrayList<UserMarkupCollection>();
		for (UserMarkupCollection userMarkupCollection : userMarkupCollections) {
			
			if (userMarkupCollection.getTagLibrary().contains(
					tagsetDefinition)) {
				modified.add(userMarkupCollection);
				userMarkupCollection.update(tagsetDefinition);
				tagManager.update(
						userMarkupCollection.getTagLibrary(), 
						tagsetDefinition);
			}
		}
		
		return modified;
	}
	

	public void add(UserMarkupCollection userMarkupCollection) {
		this.userMarkupCollections.add(userMarkupCollection);		
	}
	
	@Override
	public Iterator<UserMarkupCollection> iterator() {
		return userMarkupCollections.iterator();
	}

	public void addTagReferences(
			List<TagReference> tagReferences,
			UserMarkupCollection userMarkupCollection) {
	
		userMarkupCollection.addTagReferences(tagReferences);
	}


	public List<UserMarkupCollection> getUserMarkupCollections() {
		return Collections.unmodifiableList(userMarkupCollections);
	}
}
