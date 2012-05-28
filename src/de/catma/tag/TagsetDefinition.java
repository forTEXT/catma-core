package de.catma.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagsetDefinition implements Versionable, Iterable<TagDefinition> {
	
	private Integer id;
	private String uuid;
	private String name;
	private Version version;
	private Map<String,TagDefinition> tagDefinitions;
	private Map<String,Set<String>> tagDefinitionChildren;
	
	public TagsetDefinition(Integer id, String uuid, String tagsetName, Version version) {
		this.id = id;
		this.uuid = uuid;
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
		return "TAGSET_DEF["+name+",#"+uuid+","+version+"]";
	}

	public void addTagDefinition(TagDefinition tagDef) {
		tagDefinitions.put(tagDef.getUuid(),tagDef);
		if (!tagDefinitionChildren.containsKey(tagDef.getParentUuid())) {
			tagDefinitionChildren.put(
					tagDef.getParentUuid(), new HashSet<String>());
		}
		tagDefinitionChildren.get(
				tagDef.getParentUuid()).add(tagDef.getUuid());
	}
	
	public String getUuid() {
		return uuid;
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
				tagDefinitionChildren.get(tagDefinition.getUuid());
		
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
				tagDefinitionChildren.get(tagDefinition.getUuid());
		
		if (directChildrenIDs == null) {
			return Collections.emptySet();
			
		}
		
		for (String childID : directChildrenIDs) {
			TagDefinition child = getTagDefinition(childID); 
			childIDs.add(child.getUuid());
			childIDs.addAll(getChildIDs(child));
		}

		return Collections.unmodifiableSet(childIDs);	
	}

	void setName(String name) {
		this.name = name;
		this.version = new Version();
	}

	public void remove(TagDefinition tagDefinition) {
		this.tagDefinitions.remove(tagDefinition.getUuid());
		Set<String> childrenOfParent = this.tagDefinitionChildren.get(
				tagDefinition.getParentUuid());
		if (childrenOfParent != null) {
			childrenOfParent.remove(tagDefinition.getUuid());
		}
	}

	public String getTagPath(TagDefinition tagDefinition) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("/");
		builder.append(tagDefinition.getName());
		String baseID = tagDefinition.getParentUuid();
		
		while (!baseID.isEmpty()) {
			TagDefinition parentDef = getTagDefinition(baseID);
			builder.insert(0, parentDef.getName());
			builder.insert(0, "/");
			
			baseID = parentDef.getParentUuid();
		}
		
		return builder.toString();
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

}

