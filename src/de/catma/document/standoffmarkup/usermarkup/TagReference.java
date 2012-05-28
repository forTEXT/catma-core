package de.catma.document.standoffmarkup.usermarkup;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;

import de.catma.document.Range;
import de.catma.tag.PropertyDefinition;
import de.catma.tag.TagDefinition;
import de.catma.tag.TagInstance;
import de.catma.tag.TargetText;

public class TagReference {

	public static class RangeComparator implements Comparator<TagReference> {
		@Override
		public int compare(TagReference o1, TagReference o2) {
			return o1.getRange().compareTo(o2.getRange());
		}
	}
	
	private TagInstance tagInstance;
	private Range range;
	private URI target;
	private TargetText targetText;

	public TagReference(TagInstance tagInstance, String uri, Range range) 
			throws URISyntaxException {
		this.tagInstance = tagInstance;
		this.target = new URI(uri);
		this.range = range;
	}
	
	@Override
	public String toString() {
		return tagInstance + "@" + target + "#" + range;
	}

	public TagDefinition getTagDefinition() {
		return tagInstance.getTagDefinition();
	}
	
	public String getTagInstanceID() {
		return tagInstance.getID();
	}
	
	public Range getRange() {
		return range;
	}
	
	public String getColor() {
		return tagInstance.getSystemProperty(
			tagInstance.getTagDefinition().getPropertyDefinitionByName(
					PropertyDefinition.SystemPropertyName.catma_displaycolor.name()).getID()).
				getPropertyValueList().getFirstValue();
	}
	
	public TagInstance getTagInstance() {
		return tagInstance;
	}
	
	public URI getTarget() {
		return target;
	}
}
