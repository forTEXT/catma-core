package de.catma.core.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TagDefinition implements Versionable {
	
	private static enum SystemPropertyName {
		catma_displaycolor,
		;
		
		public static boolean hasPropertyName(String name) {
			for (SystemPropertyName sysPropName : values()) {
				if (sysPropName.name().equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public final static TagDefinition CATMA_BASE_TAG = 
			new TagDefinition(
				"CATMA_BASE_TAG", "CATMA_BASE_TAG", new Version("1"), null);
	
	private String id;
	private String name;
	private Version version;
	private Map<String,PropertyDefinition> systemPropertyDefinitions;
	private Map<String,PropertyDefinition> userDefinedPropertyDefinitions;
	private String parentID;


	public TagDefinition(String id, String name, Version version, String parentID) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.parentID = parentID;
		if (this.parentID == null) {
			this.parentID = "";
		}
		systemPropertyDefinitions = new HashMap<String, PropertyDefinition>();
		userDefinedPropertyDefinitions = new HashMap<String, PropertyDefinition>();
	}

	public Version getVersion() {
		return version;
	}
	
	
	@Override
	public String toString() {
		return "TAG_DEF[" + name 
				+ ",#" + id +","
				+version
				+((parentID.isEmpty()) ? "]" : (",#"+parentID+"]"));
	}

	public void addSystemPropertyDefinition(PropertyDefinition propertyDefinition) {
		systemPropertyDefinitions.put(propertyDefinition.getId(), propertyDefinition);
	}
	
	public void addUserDefinedPropertyDefinition(PropertyDefinition propertyDefinition) {
		userDefinedPropertyDefinitions.put(propertyDefinition.getId(), propertyDefinition);
	}	
	
	public String getID() {
		return id;
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
		if (SystemPropertyName.hasPropertyName(propertyName)) {
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
	 * 			the {@link #CATMA_BASE_TAG}. This method never returns <code>null</code>.
	 */
	public String getParentID() {
		return parentID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return getPropertyDefinitionByName(
				SystemPropertyName.catma_displaycolor.name()).getFirstValue();
	}

	public Collection<PropertyDefinition> getSystemPropertyDefinitions() {
		return Collections.unmodifiableCollection(
				systemPropertyDefinitions.values());
	}
	
	void setType(String type) {
		this.name = type;
	}
	
	void setColor(String colorAsRgbInt) {
		getPropertyDefinitionByName(
			SystemPropertyName.catma_displaycolor.name()).
				getPossibleValueList().setValue(colorAsRgbInt);
	}
	
}
