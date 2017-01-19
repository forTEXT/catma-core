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
import java.util.Collections;
import java.util.List;

/**
 * A list of values set for a {@link Property}.
 * @author marco.petris@web.de
 *
 */
public class PropertyValueList {

	private List<String> values;

	public PropertyValueList(String value) {
		this();
		values.add(value);
	}

	public PropertyValueList(List<String> values) {
		this.values = values;
	}
	
	public PropertyValueList() {
		values = new ArrayList<String>();
	}

	public PropertyValueList(PropertyValueList propertyValueList) {
		this(new ArrayList<>(propertyValueList.values));
	}

	void setValues(List<String> values) {
		this.values = values; 
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i=0; i<values.size(); i++) {
			if (i>0) {
				sb.append(",");
			}
			sb.append(values.get(i));
		}
		
		return sb.toString();
	}

	/**
	 * @return the first value in this list or <code>null</code> if the list is empty
	 */
	public String getFirstValue() {
		if (values.size() > 0) {
			return values.get(0);
		}
		return null;
	}
	
	/**
	 * @return non modifiable list of the internal values
	 */
	public List<String> getValues() {
		return Collections.unmodifiableList(values);
	}
	
}
