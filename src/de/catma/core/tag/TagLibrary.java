package de.catma.core.tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagLibrary implements Iterable<TagsetDefinition> {

	private String id;
	private String name;
	private Map<String,TagsetDefinition> tagsetDefinitions;
	
	public TagLibrary(String id, String name) {
		super();
		this.id = id;
		this.name = name;
		tagsetDefinitions = new HashMap<String, TagsetDefinition>();
	}

	void add(TagsetDefinition tagsetDefinition) {
		tagsetDefinitions.put(tagsetDefinition.getID(),tagsetDefinition);
	}

	public TagDefinition getTagDefinition(String tagDefinitionID) {
		for(TagsetDefinition tagsetDefiniton : tagsetDefinitions.values()) {
			if (tagsetDefiniton.hasTagDefinition(tagDefinitionID)) {
				return tagsetDefiniton.getTagDefinition(tagDefinitionID);
			}
		}
		return null;
	}
	
	public Iterator<TagsetDefinition> iterator() {
		return Collections.unmodifiableCollection(tagsetDefinitions.values()).iterator();
	}

	public TagsetDefinition getTagsetDefintion(String tagsetDefinitionID) {
		return tagsetDefinitions.get(tagsetDefinitionID);
	}
	
	public String getName() {
		return name;
	}

	public List<TagDefinition> getChildren(TagDefinition tagDefinition) {
		TagsetDefinition tagsetDefinition = getTagsetDefinition(tagDefinition);
		return tagsetDefinition.getChildren(tagDefinition);
	}

	public TagsetDefinition getTagsetDefinition(TagDefinition tagDefinition) {
		for (TagsetDefinition td : this) {
			if (td.contains(tagDefinition)) {
				return td;
			}
		}
		return null;
	}

	public Set<String> getChildIDs(TagDefinition tagDefinition) {
		TagsetDefinition tagsetDefinition = getTagsetDefinition(tagDefinition);
		return tagsetDefinition.getChildIDs(tagDefinition);
	}

	void remove(TagsetDefinition tagsetDefinition) {
		tagsetDefinitions.remove(tagsetDefinition.getID());
	}
	
	public String getId() {
		return id;
	}
}
