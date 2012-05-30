package de.catma.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.catma.document.ContentInfoSet;
import de.catma.tag.ITagLibrary;
import de.catma.tag.TagDefinition;
import de.catma.tag.TagInstance;
import de.catma.tag.TagsetDefinition;

public class UserMarkupCollection implements IUserMarkupCollection {
	
	private String id;
	private ContentInfoSet contentInfoSet;
	private ITagLibrary tagLibrary;
	private List<TagReference> tagReferences;
	
	public UserMarkupCollection(
			String id, ContentInfoSet contentInfoSet, ITagLibrary tagLibrary,
			List<TagReference> tagReferences) {
		this.id = id;
		this.contentInfoSet = contentInfoSet;
		this.tagLibrary = tagLibrary;
		this.tagReferences = tagReferences;
	}


	public ITagLibrary getTagLibrary() {
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
	
	//FIXME: too naive 
	public void update(TagsetDefinition tagsetDefinition) {
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
	
	@Override
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
	
	public void setTagLibrary(ITagLibrary tagLibrary) {
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
}
