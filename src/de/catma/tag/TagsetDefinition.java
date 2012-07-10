package de.catma.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class TagsetDefinition implements Versionable, Iterable<TagDefinition> {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Integer id;
	private String uuid;
	private String name;
	private Version version;
	private Map<String,TagDefinition> tagDefinitions;
	private Map<String,Set<String>> tagDefinitionChildren;
	private Set<Integer> deletedTagDefinitions;
	
	public TagsetDefinition(
			Integer id, String uuid, String tagsetName, Version version) {
		this.id = id;
		this.uuid = uuid;
		this.name = tagsetName;
		this.version = version;
		this.tagDefinitions = new HashMap<String, TagDefinition>();
		this.tagDefinitionChildren = new HashMap<String, Set<String>>();
		this.deletedTagDefinitions = new HashSet<Integer>();
	}

	public TagsetDefinition(TagsetDefinition toCopy) {
		this (null, toCopy.uuid, toCopy.name, new Version(toCopy.version));
		for (TagDefinition tagDefinition : toCopy) {
			addTagDefinition(new TagDefinition(tagDefinition));
		}
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

	
	void synchronizeWith(
			TagsetDefinition tagsetDefinition) throws IllegalArgumentException {
		if (!this.getUuid().equals(tagsetDefinition.getUuid())) {
			throw new IllegalArgumentException(
				"can only synch between different versions of the same uuid, this! uuid #" 
				+ this.getUuid() + " incoming uuid #" + tagsetDefinition.getUuid());
		}
		
		if (!tagsetDefinition.getVersion().equals(this.getVersion())) {
			this.setName(tagsetDefinition.getName());
			this.version = new Version(tagsetDefinition.getVersion());
		}
		
		Iterator<Map.Entry<String,TagDefinition>> iterator = 
				tagDefinitions.entrySet().iterator();
		
		while(iterator.hasNext()) {
			TagDefinition td = iterator.next().getValue();
			
			if (tagsetDefinition.hasTagDefinition(td.getUuid())) {
				TagDefinition other = 
						tagsetDefinition.getTagDefinition(td.getUuid());
				if (!td.getVersion().equals(other.getVersion())) {
					logger.info("synching " + td + " with " + other);
					td.synchronizeWith(other, this);
				}
			}
			else {
				logger.info("marking " + td + " in " + this + " as deleted");
				if (td.getId() != null) {
					deletedTagDefinitions.add(td.getId());
				}
				iterator.remove();
				tagDefinitionChildren.remove(td.getUuid());
			}
			
		}
		for (TagDefinition td : tagsetDefinition) {
			if (!this.hasTagDefinition(td.getUuid())) {
				logger.info("adding " + td + " to " + this + " because of synch");
				addTagDefinition(new TagDefinition(td));
			}
		}
	}
	
	public boolean isSynchronized(TagsetDefinition tagsetDefinition) {
		
		if (this.getVersion().equals(tagsetDefinition.getVersion())) {
			for (TagDefinition td : this) {
				if (tagsetDefinition.hasTagDefinition(td.getUuid())) {
					if (!td.getVersion().equals(
							tagsetDefinition.getTagDefinition(
									td.getUuid()).getVersion())) {
						return false;
					}
				}
				else {
					return false;
				}
			}
			
			for (TagDefinition td : tagsetDefinition) {
				if (!this.hasTagDefinition(td.getUuid())) {
					return false; 
				}
			}
			
			return true;
		}
		return false;
	}
	
	void setVersion() {
		this.version = new Version();
	}
	
	public Set<Integer> getDeletedTagDefinitions() {
		return deletedTagDefinitions;
	}
}
