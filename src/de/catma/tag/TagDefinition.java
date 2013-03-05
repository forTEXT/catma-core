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
package de.catma.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class TagDefinition implements Versionable {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Integer id;
	private Integer parentId;
	
	private String uuid;
	private String name;
	private Version version;
	private Map<String,PropertyDefinition> systemPropertyDefinitions;
	private Map<String,PropertyDefinition> userDefinedPropertyDefinitions;
	private String parentUuid;
	private Set<Integer> deletedPropertyDefinitions;

	public TagDefinition(
			Integer id, String uuid, 
			String name, Version version,  
			Integer parentId, String parentUuid) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.version = version;
		this.parentId = parentId;
		this.parentUuid = parentUuid;
		if (this.parentUuid == null) {
			this.parentUuid = "";
		}
		systemPropertyDefinitions = new HashMap<String, PropertyDefinition>();
		userDefinedPropertyDefinitions = new HashMap<String, PropertyDefinition>();
		deletedPropertyDefinitions = new HashSet<Integer>();
	}

	public TagDefinition(TagDefinition toCopy) {
		this(null, toCopy.uuid, 
				toCopy.name, new Version(toCopy.version), 
				null, toCopy.parentUuid);
		
		for (PropertyDefinition pd : toCopy.getSystemPropertyDefinitions()) {
			addSystemPropertyDefinition(new PropertyDefinition(pd));
		}
		for (PropertyDefinition pd : toCopy.getUserDefinedPropertyDefinitions()) {
			addUserDefinedPropertyDefinition(new PropertyDefinition(pd));
		}	
	}

	public Version getVersion() {
		return version;
	}
	
	
	@Override
	public String toString() {
		return "TAG_DEF[" + name 
				+ ",#"+id+"u#" + uuid +","
				+version
				+((parentUuid.isEmpty()) ? "]" : (",#"+parentUuid+"]"));
	}

	public void addSystemPropertyDefinition(PropertyDefinition propertyDefinition) {
		systemPropertyDefinitions.put(propertyDefinition.getUuid(), propertyDefinition);
	}
	
	public void addUserDefinedPropertyDefinition(PropertyDefinition propertyDefinition) {
		userDefinedPropertyDefinitions.put(propertyDefinition.getUuid(), propertyDefinition);
	}	
	
	public String getUuid() {
		return uuid;
	}
	
	/**
	 * @param id {@link #getUuid() uuid} of the PropertyDefinition
	 * @return the corresponding PropertyDefinition or <code>null</code> 
	 */
	public PropertyDefinition getPropertyDefinition(String id) {
		if (systemPropertyDefinitions.containsKey(id)) {
			return systemPropertyDefinitions.get(id);
		}
		else {
			return userDefinedPropertyDefinitions.get(id);
		}
	}
	
	public PropertyDefinition getPropertyDefinitionByName(String propertyName) {
		if (PropertyDefinition.SystemPropertyName.hasPropertyName(propertyName)) {
			for (PropertyDefinition pd : systemPropertyDefinitions.values()) {
				if (pd.getName().equals(propertyName)) {
					return pd;
				}
			}
		}
		
		for (PropertyDefinition pd : userDefinedPropertyDefinitions.values()) {
			if (pd.getName().equals(propertyName)) {
				return pd;
			}
		}
		
		return null;
	}
	
	public Collection<PropertyDefinition> getUserDefinedPropertyDefinitions() {
		return Collections.unmodifiableCollection(userDefinedPropertyDefinitions.values());
	}
	
	/**
	 * @return the ID of the parent TagDefinition or an empty String if this is
	 * 			a toplevel TagDefinittion. This method never returns <code>null</code>.
	 */
	public String getParentUuid() {
		return parentUuid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return getPropertyDefinitionByName(
			PropertyDefinition.SystemPropertyName.catma_displaycolor.name()).getFirstValue();
	}
	
	public String getAuthor() {
		PropertyDefinition authorPropertyDef =  getPropertyDefinitionByName(
			PropertyDefinition.SystemPropertyName.catma_markupauthor.name());
		if (authorPropertyDef != null) {
			return authorPropertyDef.getFirstValue();
		}
		else {
			return null;
		}
	}

	public Collection<PropertyDefinition> getSystemPropertyDefinitions() {
		return Collections.unmodifiableCollection(
				systemPropertyDefinitions.values());
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void setColor(String colorAsRgbInt) {
		getPropertyDefinitionByName(
			PropertyDefinition.SystemPropertyName.catma_displaycolor.name()).
				getPossibleValueList().setValue(colorAsRgbInt);
	}
	
	void setAuthor(String author) {
		getPropertyDefinitionByName(
			PropertyDefinition.SystemPropertyName.catma_markupauthor.name()).
				getPossibleValueList().setValue(author);
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Integer getParentId() {
		return parentId;
	}

	void synchronizeWith(TagDefinition other, TagsetDefinition thisTagsetDefinition) {
		if (!this.getVersion().equals(other.getVersion())) {
			this.name = other.name;
			this.version = new Version(other.getVersion());
			if (!this.parentUuid.equals(other.uuid)) {
				this.parentId = null;
			}
			this.parentUuid = other.parentUuid;
			if (!parentUuid.isEmpty()) {
				TagDefinition parentDefinition = 
						thisTagsetDefinition.getTagDefinition(this.parentUuid);
				if (parentDefinition != null) {
					this.parentId = parentDefinition.getId();
				}
			}
			
			synchPropertyDefinitions(systemPropertyDefinitions, other);
			synchPropertyDefinitions(userDefinedPropertyDefinitions, other);

			for (PropertyDefinition pd : other.getSystemPropertyDefinitions()) {
				if (this.getPropertyDefinition(pd.getUuid()) == null) {
					logger.info("adding system property " + pd + " to " + this + " because of synch");
					addSystemPropertyDefinition(
							new PropertyDefinition(pd));
				}
			}

			for (PropertyDefinition pd : other.getUserDefinedPropertyDefinitions()) {
				if (this.getPropertyDefinition(pd.getUuid()) == null) {
					logger.info("adding user property " + pd + " to " + this + " because of synch");
					addUserDefinedPropertyDefinition(
							new PropertyDefinition(pd));
				}
			}
			
		}
	}

	private void synchPropertyDefinitions(
			Map<String, PropertyDefinition> propertyDefinitions,
			TagDefinition other) {
		
		Iterator<PropertyDefinition> pdIterator =
				propertyDefinitions.values().iterator();
		
		while (pdIterator.hasNext()) {
			PropertyDefinition pd  = pdIterator.next();
			PropertyDefinition otherPd = other.getPropertyDefinition(pd.getUuid());
			
			if (otherPd != null) {
				logger.info("synching " + pd + " with "  + otherPd);
				pd.synchronizeWith(otherPd);
			}
			else {
				logger.info("deleting " + pd + " from " + this);
				deletedPropertyDefinitions.add(pd.getId());
				pdIterator.remove();
			}
		}	
	}

	void setVersion() {
		this.version = new Version();
	}
	
	public Set<Integer> getDeletedPropertyDefinitions() {
		return deletedPropertyDefinitions;
	}

	public void removeUserDefinedPropertyDefinition(PropertyDefinition propertyDefinition) {
		this.userDefinedPropertyDefinitions.remove(propertyDefinition.getUuid());
	}
}
