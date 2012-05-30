package de.catma.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.catma.tag.TagManager;
import de.catma.tag.TagsetDefinition;

public class UserMarkupCollectionManager implements Iterable<IUserMarkupCollection>{
	

	private TagManager tagManager;
	private List<IUserMarkupCollection> userMarkupCollections;

	public UserMarkupCollectionManager(TagManager tagManager) {
		this.tagManager = tagManager;
		userMarkupCollections = new ArrayList<IUserMarkupCollection>();
	}
	

	public void updateUserMarkupCollections(
			List<IUserMarkupCollection> outOfSynchCollections, TagsetDefinition tagsetDefinition) {
		for (IUserMarkupCollection userMarkupCollection : outOfSynchCollections) {
			tagManager.synchronize(
				userMarkupCollection.getTagLibrary().getTagsetDefinition(
						tagsetDefinition.getUuid()),
				tagsetDefinition);
			
			userMarkupCollection.synchronizeTagInstances(false);
		}
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


	public List<IUserMarkupCollection> getUserMarkupCollections(
			TagsetDefinition tagsetDefinition) {
		
		List<IUserMarkupCollection> result = 
				new ArrayList<IUserMarkupCollection>();
		
		for (IUserMarkupCollection userMarkupCollection : userMarkupCollections) {
			
			if (userMarkupCollection.getTagLibrary().contains(
					tagsetDefinition)) {
				TagsetDefinition containedTagsetDef = 
					userMarkupCollection.getTagLibrary().getTagsetDefinition(
							tagsetDefinition.getUuid());
				if (!containedTagsetDef.isSynchronized(tagsetDefinition)) {
					result.add(userMarkupCollection);
				}
			}
			
		}
		
		return result;
	}
}
