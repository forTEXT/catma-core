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

/**
 * A definition or type for a {@link Property}.
 * 
 * @author marco.petris@web.de
 *
 */
public class PropertyDefinition {
	
	/**
	 * Property names used for PropertyDefinitions of system properties.
	 * System properties are obligatory properties present for all {@link TagDefinition}s.
	 */
	public enum SystemPropertyName {
		/**
		 * The color of the tag as a combined RGB integer value representing an
		 * opaque sRGB color consisting of the red component in bits 16-23,
		 * the green component in bits 8-15, and the blue component in bits 0-7
		 * @see de.catma.util.ColorConverter
		 */
		catma_displaycolor,
		/**
		 * The author of the specific {@link TagInstance} that carries the
		 * TagDefinition with this PropertyDefinition.
		 */
		catma_markupauthor,
		;
		
		/**
		 * @param name
		 * @return <code>true</code> if ther is a system property with the given name 
		 */
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
	
	/**
	 * @param id the identifier of the definition (depends on the repository)
	 * @param uuid a CATMA uuid see {@link de.catma.util.IDGenerator}.
	 * @param name the name of the property
	 * @param possibleValueList a list of possible values (this is more meant as an offer than
	 * a restriction since adhoc values of {@link Property properties} are allowed explicitly).
	 */
	public PropertyDefinition(Integer id, String uuid, String name,
			PropertyPossibleValueList possibleValueList) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.possibleValueList = possibleValueList;
	}
	
	
	/**
	 * Copy constructor.
	 * @param toCopy
	 */
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
	
	/**
	 * @return a CATMA uuid see {@link de.catma.util.IDGenerator}.
	 */
	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the first possible value or <code>null</code> if there are no
	 * values specified yet
	 */
	public String getFirstValue() {
		return possibleValueList.getFirstValue();
	}
	
	public PropertyPossibleValueList getPossibleValueList() {
		return possibleValueList;
	}
	
	public void setPossibleValueList(PropertyPossibleValueList possibleValueList) {
		this.possibleValueList = possibleValueList;
	}
	
	/**
	 * @return a repository dependent identifier
	 */
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Replaces this definition with the given definition. (The {@link #getId() id}
	 * will not be overridden.
	 * 
	 * @param pd 
	 */
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
