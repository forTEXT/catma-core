package de.catma.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TagInstance {

	private String uuid;
	private TagDefinition tagDefinition;
	private Map<String,Property> systemProperties;
	private Map<String,Property> userDefinedProperties;
	
	public TagInstance(String uuid, TagDefinition tagDefinition) {
		this.uuid = uuid;
		this.tagDefinition = tagDefinition;

		systemProperties = new HashMap<String, Property>();
		setDefaultValues(
				systemProperties, 
				tagDefinition.getSystemPropertyDefinitions());

		userDefinedProperties = new HashMap<String, Property>();
		setDefaultValues(
			userDefinedProperties, 
			tagDefinition.getUserDefinedPropertyDefinitions());

	}
	
	private void setDefaultValues(
			Map<String,Property> properties,
			Collection<PropertyDefinition> propertyDefinitions) {
		for (PropertyDefinition pDef : propertyDefinitions) {
			properties.put(
			pDef.getUuid(), 
			new Property(pDef, new PropertyValueList(pDef.getFirstValue())));
		}
	}

	public TagDefinition getTagDefinition() {
		return tagDefinition;
	}

	public void addSystemProperty(Property property) {
		systemProperties.put(property.getPropertyDefinition().getUuid(), property);
	}
	
	public void addUserDefinedProperty(Property property) {
		userDefinedProperties.put(property.getPropertyDefinition().getUuid(), property);
	}
	
	@Override
	public String toString() {
		return "TAGINSTANCE[#"+uuid+","+tagDefinition+"]";
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public Property getSystemProperty(String id) {
		return systemProperties.get(id);
	}
	
	public Property getUserDefinedProperty(String id) {
		return userDefinedProperties.get(id);
	}
	
	public Collection<Property> getSystemProperties() {
		return Collections.unmodifiableCollection(systemProperties.values());
	}
	
	public Collection<Property> getUserDefinedProperties() {
		return Collections.unmodifiableCollection(userDefinedProperties.values());
	}
	
	public void synchronizeProperties(boolean withUserDefinedPropertyValues) {
		
		Iterator<Map.Entry<String, Property>> iterator = systemProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Property> entry = iterator.next();
			Property p = entry.getValue();
			if (getTagDefinition().getPropertyDefinition(entry.getKey())==null) {
				iterator.remove();
			}
			else {
				p.synchronize();
			}
		}
		
		//TODO: this leaves us with orphan property definitions, maybe we should always delete and 
		// just restrict p.synchronize to "withUserDefinedPropertyValues"
		if (withUserDefinedPropertyValues) {
			iterator = userDefinedProperties.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Property> entry = iterator.next();
				Property p = entry.getValue();
				if (getTagDefinition().getPropertyDefinition(entry.getKey())==null) {
					iterator.remove();
				}
				else {
					p.synchronize();
				}
			}
		}
	}

	public Property getProperty(String uuid) {
		if (systemProperties.containsKey(uuid)) {
			return getSystemProperty(uuid);
		}
		return getUserDefinedProperty(uuid);
	}
}
