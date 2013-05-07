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
package de.catma.user;

/**
 * {@link User}s can have roles.
 * 
 * @author marco.petris@web.de
 *
 */
public enum Role {

	STANDARD(0),
	ADMIN(1)
	;
	
	private int val;

	private Role(int val) {
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}
	
	public boolean is(int val) {
		return getVal() == val; 
	}
	
	public static Role getRole(int val) {
		for (Role role : values()) {
			if (role.is(val)) {
				return role;
			}
		}
		
		throw new IllegalArgumentException("no role with val " + val);
	}
}
