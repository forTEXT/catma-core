package de.catma.core.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagsetDefinition implements Versionable, Iterable<TagDefinition> {
	
	private String id;
	private String name;
	private Version version;
	private Map<String,TagDefinition> tagDefinitions;
	private Map<String,Set<String>> tagDefinitionChildren;
	
	public TagsetDefinition(String id, String tagsetName, Version version) {
		this.id = id;
		this.name = tagsetName;
		this.version = version;
		this.tagDefinitions = new HashMap<String, TagDefinition>();
		this.tagDefinitionChildren = new HashMap<String, Set<String>>();
	}

	public Version getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		return "TAGSET_DEF["+name+",#"+id+","+version+"]";
	}

	void addTagDefinition(TagDefinition tagDef) {
		tagDefinitions.put(tagDef.getID(),tagDef);
		if (!tagDefinitionChildren.containsKey(tagDef.getParentID())) {
			tagDefinitionChildren.put(
					tagDef.getParentID(), new HashSet<String>());
		}
		tagDefinitionChildren.get(
				tagDef.getParentID()).add(tagDef.getID());
	}
	
	public String getID() {
		return id;
	}
	
	public boolean hasTagDefinition(String tagDefID) {
		return tagDefinitions.containsKey(tagDefID);
	}
	
	public TagDefinition getTagDefinition(String tagDefinitionID) {
		return tagDefinitions.get(tagDefinitionID);
	}
	
	public Iterator<TagDefinition> iterator() {
		return Collections.unmodifiableCollection(tagDefinitions.values()).iterator();
	}
	
	public String getName() {
		return name;
	}

	public boolean contains(TagDefinition tagDefinition) {
		return tagDefinitions.values().contains(tagDefinition);
	}

	public List<TagDefinition> getChildren(TagDefinition tagDefinition) {
		List<TagDefinition> children = new ArrayList<TagDefinition>();
		Set<String> directChildrenIDs = 
				tagDefinitionChildren.get(tagDefinition.getID());
		
		if (directChildrenIDs == null) {
			return Collections.emptyList();
			
		}
		
		for (String childID : directChildrenIDs) {
			TagDefinition child = getTagDefinition(childID); 
			children.add(child);
			children.addAll(getChildren(child));
		}

		return Collections.unmodifiableList(children);
	}

	Set<String> getChildIDs(TagDefinition tagDefinition) {
		Set<String> childIDs = new HashSet<String>();
		Set<String> directChildrenIDs = 
				tagDefinitionChildren.get(tagDefinition.getID());
		
		if (directChildrenIDs == null) {
			return Collections.emptySet();
			
		}
		
		for (String childID : directChildrenIDs) {
			TagDefinition child = getTagDefinition(childID); 
			childIDs.add(child.getID());
			childIDs.addAll(getChildIDs(child));
		}

		return Collections.unmodifiableSet(childIDs);	
	}

	void setName(String name) {
		this.name = name;
	}

	public void remove(TagDefinition tagDefinition) {
		this.tagDefinitions.remove(tagDefinition.getID());
		Set<String> childrenOfParent = this.tagDefinitionChildren.get(
				tagDefinition.getParentID());
		if (childrenOfParent != null) {
			childrenOfParent.remove(tagDefinition.getID());
		}
	}

	public String getTagPath(TagDefinition tagDefinition) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("/");
		builder.append(tagDefinition.getName());
		String baseID = tagDefinition.getParentID();
		
		while (!baseID.equals(TagDefinition.CATMA_BASE_TAG.getID())) {
			TagDefinition parentDef = getTagDefinition(baseID);
			builder.insert(0, parentDef.getName());
			builder.insert(0, "/");
			
			baseID = parentDef.getParentID();
		}
		
		return builder.toString();
	}
}
