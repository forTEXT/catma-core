package de.catma.core.tag;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface ITagLibrary extends Iterable<TagsetDefinition> {

	public void add(TagsetDefinition tagsetDefinition);
	
	public void replace(TagsetDefinition tagsetDefinition);
	
	public TagDefinition getTagDefinition(String tagDefinitionID);
	
	public Iterator<TagsetDefinition> iterator();
	
	public TagsetDefinition getTagsetDefinition(String tagsetDefinitionID);
	
	public String getName();

	public List<TagDefinition> getChildren(TagDefinition tagDefinition);

	public TagsetDefinition getTagsetDefinition(TagDefinition tagDefinition);

	public Set<String> getChildIDs(TagDefinition tagDefinition);

	public void remove(TagsetDefinition tagsetDefinition);
	
	public String getId();

	public boolean contains(TagsetDefinition tagsetDefinition);
	
	public String getTagPath(TagDefinition tagDefinition);
}
