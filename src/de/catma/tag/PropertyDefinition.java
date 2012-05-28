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

	private String name;
	private String id;
	private PropertyPossibleValueList possibleValueList;
	
	public PropertyDefinition(String id, String name,
			PropertyPossibleValueList possibleValueList) {
		this.id = id;
		this.name = name;
		this.possibleValueList = possibleValueList;
	}
	
	
	@Override
	public String toString() {
		return "PROP#"+id+"["+name+"="+possibleValueList+"]";
	}
	
	public String getID() {
		return id;
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

}
