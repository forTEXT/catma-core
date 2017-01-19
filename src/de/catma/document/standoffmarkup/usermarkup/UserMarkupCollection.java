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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.catma.document.AccessMode;
import de.catma.document.source.ContentInfoSet;
import de.catma.tag.Property;
import de.catma.tag.PropertyValueList;
import de.catma.tag.TagDefinition;
import de.catma.tag.TagInstance;
import de.catma.tag.TagLibrary;
import de.catma.tag.TagsetDefinition;
import de.catma.util.IDGenerator;
import de.catma.util.Pair;

/**
 * A collection of user generated markup in the form of {@link TagReference}s.
 * 
 * @author marco.petris@web.de
 *
 */
public class UserMarkupCollection {

	private String id;
	private ContentInfoSet contentInfoSet;
	private TagLibrary tagLibrary;
	private List<TagReference> tagReferences;
	private AccessMode accessMode;
	
	/**
	 * @param id the identifier of the collections (depends on the repository)
	 * @param contentInfoSet the bibliographical metadata of this collection
	 * @param tagLibrary the internal library with all relevant {@link TagsetDefinition}s.
	 */
	public UserMarkupCollection(
			String id, ContentInfoSet contentInfoSet, TagLibrary tagLibrary) {
		this(id, contentInfoSet, tagLibrary, new ArrayList<TagReference>(), AccessMode.WRITE);
	}
	
	/**
	 * @param id the identifier of the collections (depends on the repository)
	 * @param contentInfoSet the bibliographical metadata of this collection
	 * @param tagLibrary the internal library with all relevant {@link TagsetDefinition}s.
	 * @param tagReferences referenced text ranges and referencing {@link TagInstance}s.
	 */
	public UserMarkupCollection(
			String id, ContentInfoSet contentInfoSet, TagLibrary tagLibrary,
			List<TagReference> tagReferences, AccessMode accessMode) {
		this.id = id;
		this.contentInfoSet = contentInfoSet;
		this.tagLibrary = tagLibrary;
		this.tagReferences = tagReferences;
		this.accessMode = accessMode;
	}


	public UserMarkupCollection(UserMarkupCollection userMarkupCollection) throws URISyntaxException {
		this(
			null, 
			new ContentInfoSet(userMarkupCollection.getContentInfoSet()), 
			new TagLibrary(userMarkupCollection.getTagLibrary()));
		
		IDGenerator idGenerator = new IDGenerator();
		Map<String,TagInstance> copiedTagInstances = new HashMap<String,TagInstance>();
		
		for (TagReference tr : userMarkupCollection.getTagReferences()) {
			TagInstance tagInstance = tr.getTagInstance();
			
			TagInstance copiedInstance = copiedTagInstances.get(tagInstance.getUuid());
			
			if (copiedInstance == null) {
				TagDefinition tagDefinition = 
					getTagLibrary().getTagDefinition(tagInstance.getTagDefinition().getUuid());
				copiedInstance = new TagInstance(idGenerator.generate(), tagDefinition);
				for (Property property : tagInstance.getSystemProperties()) {
					copiedInstance.addSystemProperty(
						new Property(
							tagDefinition.getPropertyDefinition(
								property.getPropertyDefinition().getUuid()),
							new PropertyValueList(property.getPropertyValueList())));
				}
				
				for (Property property : tagInstance.getUserDefinedProperties()) {
					copiedInstance.addSystemProperty(
						new Property(
							tagDefinition.getPropertyDefinition(
								property.getPropertyDefinition().getUuid()),
							new PropertyValueList(property.getPropertyValueList())));
				}
				
				copiedTagInstances.put(tagInstance.getUuid(), copiedInstance);
			}
		
			addTagReference( 
				new TagReference(copiedInstance, tr.getTarget().toString(), tr.getRange()));
		}
	
	}

	/**
	 * @return the internal library with all relevant {@link TagsetDefinition}s.
	 */
	public TagLibrary getTagLibrary() {
		return tagLibrary;
	}
	
	/**
	 * @return unmodifiable version of referenced text ranges and referencing {@link TagInstance}s.
	 */
	public List<TagReference> getTagReferences() {
		return Collections.unmodifiableList(tagReferences);
	}

	/**
	 * @param tagDefinition return all references with this tagdefinition (<b>excluding</b>
	 * all references that have a tag definition that is a child of the given definition, i. e.
	 * this is not a deep list of references)
	 * 
	 * @return the matching references
	 */
	public List<TagReference> getTagReferences(TagDefinition tagDefinition) {
		return getTagReferences(tagDefinition, false);
	}
	
	/**
	 * @param tagDefinition return all references with this tagdefinition 
	 * @param withChildReferences <code>true</code> include child tag definitions as well (deep list) or <code>false</code> exclude
	 * child tag definitions (shallow list)
	 * @return a list of tag references
	 */
	public List<TagReference> getTagReferences(
			TagDefinition tagDefinition, boolean withChildReferences) {
		
		List<TagReference> result = new ArrayList<TagReference>();
		
		Set<String> tagDefinitionIDs = new HashSet<String>();
		tagDefinitionIDs.add(tagDefinition.getUuid());
		
		if (withChildReferences) {
			tagDefinitionIDs.addAll(getChildIDs(tagDefinition));
		}
		
		for (TagReference tr : tagReferences) {
			if (tagDefinitionIDs.contains(tr.getTagDefinition().getUuid())) {
				result.add(tr);
			}
		}
		
		return result;
	}
	
	/**
	 * @param tagDefinition 
	 * @return a set of {@link TagDefinition#getId() IDs} of tag definitions that
	 * are children of the given definition (deep list)
	 */
	public Set<String> getChildIDs(TagDefinition tagDefinition) {
		return tagLibrary.getChildIDs(tagDefinition);
	}

	/**
	 * @param tagDefinition
	 * @return a set of tag definitions that are children of the given 
	 * definition (deep list)
	 */
	public List<TagDefinition> getChildren(TagDefinition tagDefinition) {
		return tagLibrary.getChildren(tagDefinition);
	}

	@Override
	public String toString() {
		return contentInfoSet.getTitle();
	}
	
	public void addTagReferences(List<TagReference> tagReferences) {
		this.tagReferences.addAll(tagReferences);	
	}
	
	public void addTagReference(TagReference tagReference) {
		this.tagReferences.add(tagReference);
	}
	
	/**
	 * @return the identifier of this collection (depends on the repository) 
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return name of this collection
	 */
	public String getName() {
		return contentInfoSet.getTitle();
	}
	
	/**
	 * @return metadata for this collection
	 */
	public ContentInfoSet getContentInfoSet() {
		return contentInfoSet;
	}
	
	/**
	 * @return <code>true</code> if there are no tag references in this collection
	 */
	public boolean isEmpty() {
		return tagReferences.isEmpty();
	}
	
	/**
	 * @param tagLibrary the internal library with all relevant {@link TagsetDefinition}s
	 * must correspond to the tag definitions of the tag intances of the tag references 
	 * of this collection
	 */
	public void setTagLibrary(TagLibrary tagLibrary) {
		this.tagLibrary = tagLibrary;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * {@link TagInstance#synchronizeProperties() Synchronizes} all the Tag Instances. 
	 */
	public void synchronizeTagInstances() {
		HashSet<TagInstance> tagInstances = new HashSet<TagInstance>();
		for (TagReference tr : tagReferences) {
			tagInstances.add(tr.getTagInstance());
		}
		
		for (TagInstance ti : tagInstances) {
			if (getTagLibrary().getTagsetDefinition(ti.getTagDefinition()) != null) {
				//TODO: handle move between TagsetDefinitions
				ti.synchronizeProperties();
			}
			else {
				tagReferences.removeAll(getTagReferences(ti.getUuid()));
			}
		}
	}

	
	/**
	 * @param tagInstanceID
	 * @return all references which belong to the {@link TagInstance} with the given ID
	 */
	public List<TagReference> getTagReferences(String tagInstanceID) {
		List<TagReference> result = new ArrayList<TagReference>();
		
		for (TagReference tr : getTagReferences()) {
			if (tr.getTagInstanceID().equals(tagInstanceID)) {
				result.add(tr);
			}
		}
		
		return result;
	}

	/**
	 * @param ti
	 * @return all references which belong to the given TagInstance
	 */
	public List<TagReference> getTagReferences(TagInstance ti) {
		return getTagReferences(ti.getUuid());
	}
	
	/**
	 * @param instanceID
	 * @return <code>true</code> if there is a TagReference with the given TagInstance's ID
	 */
	public boolean hasTagInstance(String instanceID) {
		for (TagReference tr : getTagReferences()) {
			if (tr.getTagInstanceID().equals(instanceID)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param tagReferences references to be removed
	 */
	public void removeTagReferences(List<TagReference> tagReferences) {
		this.tagReferences.removeAll(tagReferences);
	}


	/**
	 * @param tagsetDefinition
	 * @return all references that have TagInstances with the given tag def
	 */
	public List<TagReference> getTagReferences(TagsetDefinition tagsetDefinition) {
		ArrayList<TagReference> result = new ArrayList<TagReference>();
		if (getTagLibrary().contains(tagsetDefinition)) {
			for (TagDefinition td : getTagLibrary().getTagsetDefinition(
					tagsetDefinition.getUuid())) {
				
				result.addAll(getTagReferences(td));
				
			}
		}
		
		return result;
	}
	
	/**
	 * @param contentInfoSet Metadata for this collection
	 */
	void setContentInfoSet(ContentInfoSet contentInfoSet) {
		this.contentInfoSet = contentInfoSet;
	}

	/**
	 * @param instanceID the {@link TagInstance#getUuid() uuid} of the TagInstance
	 * @return a {@link Pair} with the {@link TagLibrary#getTagPath(TagDefinition) Tag path} 
	 * and the corresponding {@link TagInstance}.
	 */
	public Pair<String,TagInstance> getInstance(String instanceID) {
		for (TagReference tr : tagReferences) {
			if (tr.getTagInstanceID().equals(instanceID)) {
				return new Pair<String,TagInstance>(
						this.tagLibrary.getTagPath(tr.getTagDefinition()),
						tr.getTagInstance());
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserMarkupCollection other = (UserMarkupCollection) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	public AccessMode getAccessMode() {
		return accessMode;
	}
}
