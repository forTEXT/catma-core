package de.catma.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyValueList {

	private List<String> values;

	public PropertyValueList(String value) {
		values = new ArrayList<String>();
		values.add(value);
	}

	public PropertyValueList(List<String> values) {
		this.values = values;
	}
	
	void setValues(List<String> values) {
		this.values = values; 
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i=0; i<values.size(); i++) {
			if (i>1) {
				sb.append(",");
			}
			sb.append(values.get(i));
		}
		
		return sb.toString();
	}

	public String getFirstValue() {
		if (values.size() > 0) {
			return values.get(0);
		}
		return null;
	}
	
	public List<String> getValues() {
		return Collections.unmodifiableList(values);
	}
	
}
