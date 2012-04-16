package de.catma.core.document.standoffmarkup.usermarkup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.catma.core.tag.TagDefinition;
import de.catma.core.tag.TagLibrary;
import de.catma.core.tag.TagsetDefinition;

public class UserMarkupCollection {
	private String id;
	private String name;
	private TagLibrary tagLibrary;
	private List<TagReference> tagReferences;
	
	public UserMarkupCollection(TagLibrary tagLibrary,
			List<TagReference> tagReferences) {
		super();
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
		tagDefinitionIDs.add(tagDefinition.getID());
		
		if (withChildReferences) {
			tagDefinitionIDs.addAll(getChildIDs(tagDefinition));
		}
		
		for (TagReference tr : tagReferences) {
			if (tagDefinitionIDs.contains(tr.getTagDefinition().getID())) {
				result.add(tr);
			}
		}
		
		return result;
	}
	
	Set<String> getChildIDs(TagDefinition tagDefinition) {
		return tagLibrary.getChildIDs(tagDefinition);
	}

	public List<TagDefinition> getChildren(TagDefinition tagDefinition) {
		return tagLibrary.getChildren(tagDefinition);
	}

	@Override
	public String toString() {
		return name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	void update(TagsetDefinition tagsetDefinition) {
		List<TagReference> toBeRemoved = new ArrayList<TagReference>();
		for (TagReference tr : tagReferences) {
			TagDefinition newTagDef = 
					tagsetDefinition.getTagDefinition(tr.getTagDefinition().getID());
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
}
