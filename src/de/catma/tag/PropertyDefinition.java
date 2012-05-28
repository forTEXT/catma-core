package de.catma.tag;



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
	
	
	@Override
	public String toString() {
		return "PROP#"+uuid+"["+name+"="+possibleValueList+"]";
	}
	
	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}
	
	public String getFirstValue() {
		return possibleValueList.getFirstValue();
	}
	
	public PropertyPossibleValueList getPossibleValueList() {
		return possibleValueList;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
}
