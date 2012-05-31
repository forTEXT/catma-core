package de.catma.tag;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface ITagLibrary extends Iterable<TagsetDefinition> {

	public void add(TagsetDefinition tagsetDefinition);
	
	public TagDefinition getTagDefinition(String tagDefinitionID);
	
	public Iterator<TagsetDefinition> iterator();
	
	public TagsetDefinition getTagsetDefinition(String tagsetDefinitionID);
	
	public String getName();

	public List<TagDefinition> getChildren(TagDefinition tagDefinition);

	public TagsetDefinition getTagsetDefinition(TagDefinition tagDefinition);

	public Set<String> getChildIDs(TagDefinition tagDefinition);

	public void remove(TagsetDefinition tagsetDefinition);
	
	public String getId();

	/**
	 * @param tagsetDefinition the tagsetDefinition is tested by {@link TagsetDefinition#getUuid()} only,
	 * so even if this TagLibrary contains another instance with the same uuid this method
	 * will return <code>true</code>! 
	 * @return true, if this TagLibrary contains a TagsetDefinition that has
	 * uuid equality with the given tagsetDefinition, else false.
	 */
	public boolean contains(TagsetDefinition tagsetDefinition);
	
	public String getTagPath(TagDefinition tagDefinition);
	
	public void setName(String name);
	
	public void setId(String id);
}
