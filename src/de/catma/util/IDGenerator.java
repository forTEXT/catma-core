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
package de.catma.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class IDGenerator {
	public final static String ID_PREFIX = "CATMA_";
	
	public String generate() {
		return ID_PREFIX + UUID.randomUUID().toString().toUpperCase();
	}
	
	public String generate(String base) {
		return ID_PREFIX + UUID.nameUUIDFromBytes(base.getBytes()).toString().toUpperCase();
	}
	
	public String uuidBytesToCatmaID(byte[] uuidBytes) {
		if (uuidBytes == null) {
			return null;
		}
		ByteBuffer bb = ByteBuffer.wrap(uuidBytes);
		UUID id = new UUID(bb.getLong(0), bb.getLong(8));
		return ID_PREFIX + id.toString().toUpperCase();
	}
	
	public UUID catmaIDToUUID(String catmaID) {
		
		if (catmaID == null) {
			return null;
		}
		
		int index = catmaID.indexOf(
				IDGenerator.ID_PREFIX)+IDGenerator.ID_PREFIX.length();
		return UUID.fromString(catmaID.substring(index));
	}
	
	public byte[] catmaIDToUUIDBytes(String catmaID) {
		if ((catmaID == null) || catmaID.isEmpty()) {
			return null;
		}
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		UUID uuid = catmaIDToUUID(catmaID);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		return bb.array();
	}
}
