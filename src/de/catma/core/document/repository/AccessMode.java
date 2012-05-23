package de.catma.core.document.repository;

public enum AccessMode {
	READ(0),
	WRITE(1)
	;
	
	private int numericRepresentation;

	private AccessMode(int numericRepresentation) {
		this.numericRepresentation = numericRepresentation;
	}
	
	public int getNumericRepresentation() {
		return numericRepresentation;
	}
}
