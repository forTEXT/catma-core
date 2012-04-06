package de.catma.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagInstance {

	private String id;
	private TagDefinition tagDefinition;
	private Map<String,Property> systemProperties;
	private Map<String,Property> userDefinedProperties;
	
	public TagInstance(String id, TagDefinition tagDefinition) {
		this.id = id;
		this.tagDefinition = tagDefinition;
		systemProperties = new HashMap<String, Property>();
		userDefinedProperties = new HashMap<String, Property>();
	}
	
	public TagDefinition getTagDefinition() {
		return tagDefinition;
	}

	public void addSystemProperty(Property property) {
		systemProperties.put(property.getName(), property);
	}
	
	public void addUserDefinedProperty(Property property) {
		userDefinedProperties.put(property.getName(), property);
	}
	
	@Override
	public String toString() {
		return "TAGINSTANCE[#"+id+","+tagDefinition+"]";
	}
	
	public String getID() {
		return id;
	}
	
	public Property getSystemProperty(String name) {
		return systemProperties.get(name);
	}

	public Property getUserDefinedProperty(String name) {
		return userDefinedProperties.get(name);
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
							p.getPropertyDefinition().getId());
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
}
