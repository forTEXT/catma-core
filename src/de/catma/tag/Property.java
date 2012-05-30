package de.catma.tag;


public class Property {

	private PropertyDefinition propertyDefinition;
	private PropertyValueList propertyValueList;
	
	public Property(PropertyDefinition propertyDefinition,
			PropertyValueList propertyValueList) {
		this.propertyDefinition = propertyDefinition;
		this.propertyValueList = propertyValueList;
	}

	public String getName() {
		return propertyDefinition.getName();
	}
	
	public PropertyValueList getPropertyValueList() {
		return propertyValueList;
	}
	
	public PropertyDefinition getPropertyDefinition() {
		return propertyDefinition;
	}

	void setPropertyDefinition(PropertyDefinition propertyDefinition) {
		//FIXME: check current values against possible values
		this.propertyDefinition = propertyDefinition;
	}
	
	public void setPropertyValueList(PropertyValueList propertyValueList) {
		this.propertyValueList = propertyValueList;
	}

	public void synchronize() {
		setPropertyValueList(
			propertyDefinition.getPossibleValueList().getPropertyValueList());
	}
}
