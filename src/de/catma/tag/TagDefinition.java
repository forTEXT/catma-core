package de.catma.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TagDefinition implements Versionable {

	private Integer id;
	private Integer parentId;
	
	private String uuid;
	private String name;
	private Version version;
	private Map<String,PropertyDefinition> systemPropertyDefinitions;
	private Map<String,PropertyDefinition> userDefinedPropertyDefinitions;
	private String parentUuid;
	private Set<Integer> deletedProperties;

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
		deletedProperties = new HashSet<Integer>();
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
				+ ",#" + uuid +","
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
			this.parentUuid = other.parentUuid;
			if (parentUuid != null) {
				this.parentId = 
					thisTagsetDefinition.getTagDefinition(this.parentUuid).getId();
			}
			
			synchPropertyDefinitions(systemPropertyDefinitions, other);
			synchPropertyDefinitions(userDefinedPropertyDefinitions, other);

			for (PropertyDefinition pd : other.getUserDefinedPropertyDefinitions()) {
				if (this.getPropertyDefinition(pd.getUuid()) == null) {
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
			if (other.getPropertyDefinition(pd.getUuid()) != null) {
				pd.synchronizeWith(pd);
			}
			else {
				deletedProperties.add(pd.getId());
				pdIterator.remove();
			}
		}	
	}

	void setVersion() {
		this.version = new Version();
	}
}
