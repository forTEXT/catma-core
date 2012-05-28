package de.catma.tag;

public class TagLibraryReference {

	private String name;
	private String id;
	
	public TagLibraryReference(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getId() {
		return id;
	}
}
