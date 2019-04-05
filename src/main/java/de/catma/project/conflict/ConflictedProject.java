package de.catma.project.conflict;

import java.util.Collection;
import java.util.List;

import de.catma.document.source.SourceDocument;
import de.catma.tag.TagDefinition;
import de.catma.tag.TagsetDefinition;

public interface ConflictedProject {

//	public boolean hasConflictedAnnotations();
//	
//	
//	public boolean hasConflictedCollectionMetadata();
//	public boolean hasConflictedTag();
//	public boolean hasConflictedTagsetMetadata();
	
	public List<CollectionConflict> getCollectionConflicts() throws Exception;
	public Collection<TagsetDefinition> getTagsets() throws Exception;
	Collection<SourceDocument> getDocuments() throws Exception;
	
	
}
