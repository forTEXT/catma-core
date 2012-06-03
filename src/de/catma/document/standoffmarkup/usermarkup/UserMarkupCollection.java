package de.catma.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.catma.document.ContentInfoSet;
import de.catma.tag.TagLibrary;
import de.catma.tag.TagDefinition;
import de.catma.tag.TagInstance;
import de.catma.tag.TagsetDefinition;

public class UserMarkupCollection {

	private String id;
	private ContentInfoSet contentInfoSet;
	private TagLibrary tagLibrary;
	private List<TagReference> tagReferences;
	
	public UserMarkupCollection(
			String id, ContentInfoSet contentInfoSet, TagLibrary tagLibrary,
			List<TagReference> tagReferences) {
		this.id = id;
		this.contentInfoSet = contentInfoSet;
		this.tagLibrary = tagLibrary;
		this.tagReferences = tagReferences;
	}


	public TagLibrary getTagLibrary() {
		return tagLibrary;
	}
	
	public List<TagReference> getTagReferences() {
		return Collections.unmodifiableList(tagReferences);
	}

	public List<TagReference> getTagReferences(TagDefinition tagDefinition) {
		return getTagReferences(tagDefinition, false);
	}
	
	public List<TagReference> getTagReferences(
			TagDefinition tagDefinition, boolean withChildReferences) {
		
		List<TagReference> result = new ArrayList<TagReference>();
		
		Set<String> tagDefinitionIDs = new HashSet<String>();
		tagDefinitionIDs.add(tagDefinition.getUuid());
		
		if (withChildReferences) {
			tagDefinitionIDs.addAll(getChildIDs(tagDefinition));
		}
		
		for (TagReference tr : tagReferences) {
			if (tagDefinitionIDs.contains(tr.getTagDefinition().getUuid())) {
				result.add(tr);
			}
		}
		
		return result;
	}
	
	public Set<String> getChildIDs(TagDefinition tagDefinition) {
		return tagLibrary.getChildIDs(tagDefinition);
	}

	public List<TagDefinition> getChildren(TagDefinition tagDefinition) {
		return tagLibrary.getChildren(tagDefinition);
	}

	@Override
	public String toString() {
		return contentInfoSet.getTitle();
	}
	
	//FIXME: obsolete
	private void update(TagsetDefinition tagsetDefinition) {
		List<TagReference> toBeRemoved = new ArrayList<TagReference>();
		for (TagReference tr : tagReferences) {
			TagDefinition newTagDef = 
					tagsetDefinition.getTagDefinition(tr.getTagDefinition().getUuid());
			if (newTagDef != null) {
				tr.getTagInstance().setTagDefinition(newTagDef);
			}
			else {
				toBeRemoved.add(tr);
			}
		}
		
		tagReferences.removeAll(toBeRemoved);
	}

	public void addTagReferences(List<TagReference> tagReferences) {
		this.tagReferences.addAll(tagReferences);	
	}
	
	public void addTagReference(TagReference tagReference) {
		this.tagReferences.add(tagReference);
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return contentInfoSet.getTitle();
	}
	
	public ContentInfoSet getContentInfoSet() {
		return contentInfoSet;
	}
	
	public boolean isEmpty() {
		return tagReferences.isEmpty();
	}
	
	public void setTagLibrary(TagLibrary tagLibrary) {
		this.tagLibrary = tagLibrary;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void synchronizeTagInstances(boolean withUserDefinedPropertyValues) {
		HashSet<TagInstance> tagInstances = new HashSet<TagInstance>();
		for (TagReference tr : tagReferences) {
			tagInstances.add(tr.getTagInstance());
		}
		
		for (TagInstance ti : tagInstances) {
			ti.synchronizeProperties(withUserDefinedPropertyValues);
		}
	}

	
	public Set<TagReference> getTagReferences(String tagInstanceID) {
		HashSet<TagReference> result = new HashSet<TagReference>();
		
		for (TagReference tr : getTagReferences()) {
			if (tr.getTagInstanceID().equals(tagInstanceID)) {
				result.add(tr);
			}
		}
		
		return result;
	}

	public Set<TagReference> getTagReferences(TagInstance ti) {
		return getTagReferences(ti.getUuid());
	}
	
	public boolean hasTagInstance(String instanceID) {
		for (TagReference tr : getTagReferences()) {
			if (tr.getTagInstanceID().equals(instanceID)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeTagReferences(Set<TagReference> tagReferences) {
		this.tagReferences.removeAll(tagReferences);
	}
}
