package de.catma.core.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class IDGenerator {
	public final static String ID_PREFIX = "CATMA_";
	
	public String generate() {
		return ID_PREFIX + UUID.randomUUID().toString();
	}
	
	public String generate(String base) {
		return ID_PREFIX + UUID.nameUUIDFromBytes(base.getBytes()).toString();
	}
	
	public String uuidBytesToCatmaID(byte[] uuidBytes) {
		if (uuidBytes == null) {
			return null;
		}
		ByteBuffer bb = ByteBuffer.wrap(uuidBytes);
		UUID id = new UUID(bb.getLong(0), bb.getLong(8));
		return ID_PREFIX + id.toString();
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
