package de.catma.tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.catma.util.ContentInfoSet;

public class TagLibrary implements Iterable<TagsetDefinition> {

	private String id;
	private ContentInfoSet contentInfoSet;
	private Map<String,TagsetDefinition> tagsetDefinitionsByID;
	
	public TagLibrary(String id, String name) {
		this.id = id;
		this.contentInfoSet = new ContentInfoSet(name);
		tagsetDefinitionsByID = new HashMap<String, TagsetDefinition>();
	}

	public void add(TagsetDefinition tagsetDefinition) {
		tagsetDefinitionsByID.put(tagsetDefinition.getUuid(),tagsetDefinition);
	}

	//FIXME: this assumes that there is only one tagsetdef that can contain a
	// tagdef identified by id, this is not true in all cases for incoming tagsetdefs
	// of older CATMA versions since move operations between tagsets were possible and
	// the conversion algorithm of the standard tagset can in certain cases generate distinct IDs as well  
	// supporting move in CATMA 4 would certainly break this assumption as well!!!
	public TagDefinition getTagDefinition(String tagDefinitionID) {
		for(TagsetDefinition tagsetDefiniton : tagsetDefinitionsByID.values()) {
			if (tagsetDefiniton.hasTagDefinition(tagDefinitionID)) {
				return tagsetDefiniton.getTagDefinition(tagDefinitionID);
			}
		}
		return null;
	}
	
	public Iterator<TagsetDefinition> iterator() {
		return Collections.unmodifiableCollection(tagsetDefinitionsByID.values()).iterator();
	}

	public TagsetDefinition getTagsetDefinition(String tagsetDefinitionID) {
		return tagsetDefinitionsByID.get(tagsetDefinitionID);
	}
	
	public String getName() {
		return contentInfoSet.getTitle();
	}

	public void setName(String name) {
		this.contentInfoSet.setTitle(name);
	}
	
	public void setId(String id) {
		this.id = id;
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

	public void remove(TagsetDefinition tagsetDefinition) {
		tagsetDefinitionsByID.remove(tagsetDefinition.getUuid());
	}
	
	public String getId() {
		return id;
	}

	/**
	 * @param tagsetDefinition the tagsetDefinition is tested by {@link TagsetDefinition#getUuid()} only,
	 * so even if this TagLibrary contains another instance with the same uuid this method
	 * will return <code>true</code>! 
	 * @return true, if this TagLibrary contains a TagsetDefinition that has
	 * uuid equality with the given tagsetDefinition, else false.
	 */
	public boolean contains(TagsetDefinition tagsetDefinition) {
		return tagsetDefinitionsByID.containsKey(tagsetDefinition.getUuid());
	}
	
	public String getTagPath(TagDefinition tagDefinition) {
		TagsetDefinition tagsetDefinition = getTagsetDefinition(tagDefinition);
		return tagsetDefinition.getTagPath(tagDefinition);
	}

	@Override
	public String toString() {
		return (contentInfoSet.getTitle()==null) ? id : contentInfoSet.getTitle();
	}
	
	public ContentInfoSet getContentInfoSet() {
		//TODO: unmodifiable copy
		return contentInfoSet;
	}
	
	void setContentInfoSet(ContentInfoSet contentInfoSet) {
		this.contentInfoSet = contentInfoSet;
	}
}
