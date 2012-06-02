package de.catma.document.standoffmarkup.usermarkup;

import java.util.List;
import java.util.Set;

import de.catma.document.ContentInfoSet;
import de.catma.tag.ITagLibrary;
import de.catma.tag.TagDefinition;
import de.catma.tag.TagInstance;
import de.catma.tag.TagsetDefinition;

public interface IUserMarkupCollection {

	public ITagLibrary getTagLibrary();
	
	public List<TagReference> getTagReferences();

	public List<TagReference> getTagReferences(TagDefinition tagDefinition);
	
	public List<TagReference> getTagReferences(
			TagDefinition tagDefinition, boolean withChildReferences);
	
	public Set<TagReference> getTagReferences(TagInstance ti);
	public Set<TagReference> getTagReferences(String tagInstanceID);
	public boolean hasTagInstance(String instanceID);
	
	//TODO: still necessary?
	Set<String> getChildIDs(TagDefinition tagDefinition);

	public List<TagDefinition> getChildren(TagDefinition tagDefinition);

	
	void update(TagsetDefinition tagsetDefinition);

	public void addTagReferences(List<TagReference> tagReferences);
	public void addTagReference(TagReference tagReference);
	
	public String getId();
	
	public String getName();
	
	public ContentInfoSet getContentInfoSet();
	
	public boolean isEmpty();

	public void setId(String id);

	public void setTagLibrary(ITagLibrary tagLibrary);
	
	public void synchronizeTagInstances(boolean withUserDefinedPropertyValues);

	public void removeTagReferences(Set<TagReference> tagReferences);

}
