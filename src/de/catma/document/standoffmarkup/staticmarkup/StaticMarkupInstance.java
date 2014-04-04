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
package de.catma.document.standoffmarkup.staticmarkup;

import java.util.List;

import de.catma.document.Range;
import de.catma.util.Pair;

public class StaticMarkupInstance {
	private Range range;
	private String name; // path -> z.B. /TEI/teiHeader/fileDesc/sourceDesc/p
	private List<Pair<String,String>> attributes;
	public StaticMarkupInstance(Range range, String name,
			List<Pair<String, String>> attributes) {
		super();
		this.range = range;
		this.name = name;
		this.attributes = attributes;
	}
	public Range getRange() {
		return range;
	}
	public void setRange(Range range) {
		this.range = range;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Pair<String, String>> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Pair<String, String>> attributes) {
		this.attributes = attributes;
	}
	
	
	
}
