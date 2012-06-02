package de.catma.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import de.catma.document.repository.Repository;
import de.catma.tag.TagManager;
import de.catma.tag.TagsetDefinition;

public class UserMarkupCollectionManager implements Iterable<IUserMarkupCollection>{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private TagManager tagManager;
	private Repository repository;
			
	private List<IUserMarkupCollection> userMarkupCollections;

	public UserMarkupCollectionManager(TagManager tagManager, Repository repository) {
		this.tagManager = tagManager;
		this.repository = repository;
		userMarkupCollections = new ArrayList<IUserMarkupCollection>();
	}
	
	public void updateUserMarkupCollections(
			List<IUserMarkupCollection> outOfSynchCollections, 
			TagsetDefinition tagsetDefinition) {
		
		for (IUserMarkupCollection userMarkupCollection : outOfSynchCollections) {
			logger.info("synching " + userMarkupCollection);
			tagManager.synchronize(
				userMarkupCollection.getTagLibrary().getTagsetDefinition(
						tagsetDefinition.getUuid()),
				tagsetDefinition);
			
			userMarkupCollection.synchronizeTagInstances(false);
		}

		repository.update(outOfSynchCollections, tagsetDefinition);
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
		
		repository.update(userMarkupCollection, tagReferences);

	}


	public List<IUserMarkupCollection> getUserMarkupCollections() {
		return Collections.unmodifiableList(userMarkupCollections);
	}


	public List<IUserMarkupCollection> getUserMarkupCollections(
			TagsetDefinition tagsetDefinition, boolean inSynch) {
		
		List<IUserMarkupCollection> result = 
				new ArrayList<IUserMarkupCollection>();
		
		for (IUserMarkupCollection userMarkupCollection : userMarkupCollections) {
			
			if (userMarkupCollection.getTagLibrary().contains(
					tagsetDefinition)) {
				TagsetDefinition containedTagsetDef = 
					userMarkupCollection.getTagLibrary().getTagsetDefinition(
							tagsetDefinition.getUuid());
				if (containedTagsetDef.isSynchronized(tagsetDefinition) == inSynch) {
					result.add(userMarkupCollection);
				}
			}
			
		}
		
		return result;
	}

	public void removeTagInstance(String instanceID) {
		IUserMarkupCollection userMarkupCollection = 
				getUserMarkupCollectionForTagInstance(instanceID); 
		
		Set<TagReference> tagReferences = 
				userMarkupCollection.getTagReferences(instanceID);
		userMarkupCollection.removeTagReferences(tagReferences);
		repository.update(userMarkupCollection, tagReferences);
	}

	private IUserMarkupCollection getUserMarkupCollectionForTagInstance(
			String instanceID) {
		for (IUserMarkupCollection userMarkupCollection : userMarkupCollections) {
			if (userMarkupCollection.hasTagInstance(instanceID)) {
				return userMarkupCollection;
			}
		}
		return null;
	}
}
