package de.catma.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

	public void setTagDefinition(TagDefinition tagDefinition) {
		// update or delete existing properties
		setTagDefinition(tagDefinition, systemProperties, true);
		setTagDefinition(tagDefinition, userDefinedProperties, false);
		
		// new property definitions will be user defined 
		// sys props will be added only by conversion mechanisms
		for (PropertyDefinition pd : 
			tagDefinition.getUserDefinedPropertyDefinitions()) {
			if (!userDefinedProperties.containsKey(pd.getName())) {
				PropertyValueList values = 
						new PropertyValueList(
								pd.getPossibleValueList().getPropertyValueList().getValues());
				addUserDefinedProperty(new Property(pd, values));
			}
		}
		this.tagDefinition = tagDefinition;
	}
	
	private void setTagDefinition(
			TagDefinition tagDefinition, 
			Map<String,Property> properties, boolean overwriteValues) {
	
		List<Property> toBeRemoved = new ArrayList<Property>();
		
		for (Property p : properties.values()) {
			PropertyDefinition incomingPropDef = 
					tagDefinition.getPropertyDefinition(
							p.getPropertyDefinition().getUuid());
			if (incomingPropDef != null) {
				p.setPropertyDefinition(incomingPropDef);
				if (overwriteValues) {
					p.setPropertyValueList(
						incomingPropDef.getPossibleValueList().getPropertyValueList());
				}
			}
			else {
				toBeRemoved.add(p);
			}
		}
		
		for (Property p : toBeRemoved) {
			properties.remove(p.getName());
		}
		
	}

	public void synchronizeProperties(boolean withUserDefinedPropertyValues) {
	
		for (Property p : systemProperties.values()) {
			p.synchronize();
		}
		if (withUserDefinedPropertyValues) {
			for (Property p : userDefinedProperties.values()) {
				p.synchronize();
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
