package de.catma.tag;

import java.util.ArrayList;
import java.util.List;

public class PropertyPossibleValueList {

	private boolean singleSelect;
	private PropertyValueList propertyValueList;
	
	public PropertyPossibleValueList(String value) {
		this(asList(value), true);
	}
	
	public PropertyPossibleValueList(List<String> values, boolean singleSelect) {
		super();
		this.propertyValueList = new PropertyValueList(values);
		this.singleSelect = singleSelect;
	}
	
	
	@Override
	public String toString() {
		return propertyValueList.toString();
	}


	public String getFirstValue() {
		return propertyValueList.getFirstValue();
	}
	
	void setValue(String value) {
		setValues(asList(value));
	}
	
	void setValues(List<String> values) {
		propertyValueList.setValues(values);
	}
	
	public boolean isSingleSelect() {
		return singleSelect;
	}
	
	public PropertyValueList getPropertyValueList() {
		return propertyValueList;
	}
	
	private static List<String> asList(String value) {
		ArrayList<String> result =new ArrayList<String>();
		result.add(value);
		return result;
	}
	
	
}
