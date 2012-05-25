package de.catma.core.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.catma.core.tag.TagManager;
import de.catma.core.tag.TagsetDefinition;

public class UserMarkupCollectionManager implements Iterable<IUserMarkupCollection>{
	

	private TagManager tagManager;
	private List<IUserMarkupCollection> userMarkupCollections;

	public UserMarkupCollectionManager(TagManager tagManager) {
		this.tagManager = tagManager;
		userMarkupCollections = new ArrayList<IUserMarkupCollection>();
	}
	

	public List<IUserMarkupCollection> updateUserMarkupCollections(
			TagsetDefinition tagsetDefinition) {
		List<IUserMarkupCollection> modified = new ArrayList<IUserMarkupCollection>();
		for (IUserMarkupCollection userMarkupCollection : userMarkupCollections) {
			
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
	

	public void add(IUserMarkupCollection userMarkupCollection) {
		this.userMarkupCollections.add(userMarkupCollection);		
	}
	
	@Override
	public Iterator<IUserMarkupCollection> iterator() {
		return userMarkupCollections.iterator();
	}

	public void addTagReferences(
			List<TagReference> tagReferences,
			IUserMarkupCollection userMarkupCollection) {
	
		userMarkupCollection.addTagReferences(tagReferences);
	}


	public List<IUserMarkupCollection> getUserMarkupCollections() {
		return Collections.unmodifiableList(userMarkupCollections);
	}
}
