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

public class PropertyDefinition {
	
	public enum SystemPropertyName {
		catma_displaycolor,
		catma_markupauthor,
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
	private Integer id;
	private String name;
	private String uuid;
	private PropertyPossibleValueList possibleValueList;
	
	public PropertyDefinition(Integer id, String uuid, String name,
			PropertyPossibleValueList possibleValueList) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.possibleValueList = possibleValueList;
	}
	
	
	public PropertyDefinition(PropertyDefinition toCopy) {
		this.uuid = toCopy.uuid;
		this.name = toCopy.name;
		ArrayList<String> copiedPossibleValues = new ArrayList<String>();
		copiedPossibleValues.addAll(
			toCopy.possibleValueList.getPropertyValueList().getValues());

		this.possibleValueList = 
			new PropertyPossibleValueList(
				copiedPossibleValues, 
				toCopy.possibleValueList.isSingleSelect());
	}


	@Override
	public String toString() {
		return "PROP#" + id + "u#"+uuid+"["+name+"="+possibleValueList+"]";
	}
	
	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFirstValue() {
		return possibleValueList.getFirstValue();
	}
	
	public PropertyPossibleValueList getPossibleValueList() {
		return possibleValueList;
	}
	
	public void setPossibleValueList(PropertyPossibleValueList possibleValueList) {
		this.possibleValueList = possibleValueList;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void synchronizeWith(PropertyDefinition pd) {
		this.uuid = pd.uuid;
		this.name = pd.name;
		ArrayList<String> copiedPossibleValues = 
				new ArrayList<String>();
		copiedPossibleValues.addAll(
				pd.possibleValueList.getPropertyValueList().getValues());

		this.possibleValueList = 
			new PropertyPossibleValueList(
				copiedPossibleValues, 
				pd.possibleValueList.isSingleSelect());	}
}
