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

import java.util.ArrayList;
import java.util.List;

/**
 * A list of possible values of a property.
 * 
 * @author marco.petris@web.de
 * @see PropertyDefinition
 */
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


	/**
	 * @return the first value or <code>null</code> if the list is empty
	 */
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
