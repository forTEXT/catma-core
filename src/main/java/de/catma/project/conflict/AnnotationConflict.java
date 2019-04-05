package de.catma.project.conflict;

import java.util.List;

import de.catma.document.standoffmarkup.usermarkup.TagReference;
import de.catma.tag.TagInstance;

public class AnnotationConflict {

	private TagInstance devTagInstance;
	private List<TagReference> devTagReferences;

	private TagInstance masterTagInstance;
	private List<TagReference> masterTagReferences;
	
	
	public AnnotationConflict(TagInstance devTagInstance, List<TagReference> devTagReferences,
			TagInstance masterTagInstance, List<TagReference> masterTagReferences) {
		super();
		this.devTagInstance = devTagInstance;
		this.devTagReferences = devTagReferences;
		this.masterTagInstance = masterTagInstance;
		this.masterTagReferences = masterTagReferences;
	}
	
	
	public TagInstance getDevTagInstance() {
		return devTagInstance;
	}
	
	public List<TagReference> getDevTagReferences() {
		return devTagReferences;
	}
	
	public TagInstance getMasterTagInstance() {
		return masterTagInstance;
	}
	
	public List<TagReference> getMasterTagReferences() {
		return masterTagReferences;
	}
	
	
}
