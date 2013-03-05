/*   
 *   CATMA Computer Aided Text Markup and Analysis
 *   
 *   Copyright (C) 2009-2013  University Of Hamburg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		return tagInstance.getUuid();
	}
	
	public Range getRange() {
		return range;
	}
	
	public String getColor() {
		return tagInstance.getSystemProperty(
			tagInstance.getTagDefinition().getPropertyDefinitionByName(
					PropertyDefinition.SystemPropertyName.catma_displaycolor.name()).getUuid()).
				getPropertyValueList().getFirstValue();
	}
	
	public TagInstance getTagInstance() {
		return tagInstance;
	}
	
	public URI getTarget() {
		return target;
	}
}
