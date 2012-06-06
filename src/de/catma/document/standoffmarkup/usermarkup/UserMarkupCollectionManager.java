package de.catma.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import de.catma.document.repository.Repository;
import de.catma.tag.TagManager;
import de.catma.tag.TagsetDefinition;

public class UserMarkupCollectionManager implements Iterable<UserMarkupCollection>{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private TagManager tagManager;
	private Repository repository;
			
	private List<UserMarkupCollection> userMarkupCollections;

	public UserMarkupCollectionManager(TagManager tagManager, Repository repository) {
		this.tagManager = tagManager;
		this.repository = repository;
		userMarkupCollections = new ArrayList<UserMarkupCollection>();
	}
	
	public void updateUserMarkupCollections(
			List<UserMarkupCollection> outOfSynchCollections, 
			TagsetDefinition tagsetDefinition) {
		
		for (UserMarkupCollection userMarkupCollection : outOfSynchCollections) {
			logger.info("synching " + userMarkupCollection);
			tagManager.synchronize(
				userMarkupCollection.getTagLibrary().getTagsetDefinition(
						tagsetDefinition.getUuid()),
				tagsetDefinition);
			
			userMarkupCollection.synchronizeTagInstances(false);
		}

		repository.update(outOfSynchCollections, tagsetDefinition);
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
		
		repository.update(userMarkupCollection, tagReferences);

	}


	public List<UserMarkupCollection> getUserMarkupCollections() {
		return Collections.unmodifiableList(userMarkupCollections);
	}


	public List<UserMarkupCollection> getUserMarkupCollections(
			TagsetDefinition tagsetDefinition, boolean inSynch) {
		
		List<UserMarkupCollection> result = 
				new ArrayList<UserMarkupCollection>();
		
		for (UserMarkupCollection userMarkupCollection : userMarkupCollections) {
			
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
		UserMarkupCollection userMarkupCollection = 
				getUserMarkupCollectionForTagInstance(instanceID); 
		
		List<TagReference> tagReferences = 
				userMarkupCollection.getTagReferences(instanceID);
		userMarkupCollection.removeTagReferences(tagReferences);
		repository.update(userMarkupCollection, tagReferences);
	}

	private UserMarkupCollection getUserMarkupCollectionForTagInstance(
			String instanceID) {
		for (UserMarkupCollection userMarkupCollection : userMarkupCollections) {
			if (userMarkupCollection.hasTagInstance(instanceID)) {
				return userMarkupCollection;
			}
		}
		return null;
	}

	public UserMarkupCollection getUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference) {
		for (UserMarkupCollection umc : userMarkupCollections) {
			if (umc.getId().equals(userMarkupCollectionReference.getId())) {
				return umc;
			}
		}
		
		return null;
	}

	public void remove(UserMarkupCollection userMarkupCollection) {
		userMarkupCollections.remove(userMarkupCollection);
	}

	public UserMarkupCollection updateUserMarkupCollection(
			UserMarkupCollectionReference userMarkupCollectionReference) {

		UserMarkupCollection userMarkupCollection = 
				getUserMarkupCollection(userMarkupCollectionReference);
		
		userMarkupCollection.setContentInfoSet(
				userMarkupCollectionReference.getContentInfoSet());
		
		return userMarkupCollection;
	}
}
