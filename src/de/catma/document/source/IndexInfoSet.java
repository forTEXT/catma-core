package de.catma.document.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class IndexInfoSet {

    private List<String> unseparableCharacterSequences;
    private List<Character> userDefinedSeparatingCharacters;
    private Locale locale;
    
    public IndexInfoSet(List<String> unseparableCharacterSequences,
			List<Character> userDefinedSeparatingCharacters,
			Locale locale) {
		super();
		this.unseparableCharacterSequences = unseparableCharacterSequences;
		this.userDefinedSeparatingCharacters = userDefinedSeparatingCharacters;
		this.locale = locale;
	}

	public IndexInfoSet() {
		this.unseparableCharacterSequences = new ArrayList<String>();
		this.userDefinedSeparatingCharacters = new ArrayList<Character>();
	}
	
    public Locale getLocale() {
        return (locale==null) ? Locale.getDefault() : locale;
    }
    
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
     * @return a (possibly empty) list of unseparable character sequences,
     * does not return <oode>null</oode>
     */
    public List<String> getUnseparableCharacterSequences() {
        return (unseparableCharacterSequences==null) ?
                Collections.<String>emptyList() : unseparableCharacterSequences;
    }
    
    /**
     * @return a (possibly empty) list of user defined speparating character sequences,
     * does not return <oode>null</oode>
     */
    public List<Character> getUserDefinedSeparatingCharacters() {
        return (userDefinedSeparatingCharacters ==null) ?
                Collections.<Character>emptyList() : userDefinedSeparatingCharacters;
    }
    
    public void addUserDefinedSeparatingCharacter(Character character) {
    	if (userDefinedSeparatingCharacters == null) {
    		userDefinedSeparatingCharacters = new ArrayList<Character>();
    	}
    	userDefinedSeparatingCharacters.add(character);
    }
    
    public void addUnseparableCharacterSequence(String ucs) {
    	if (unseparableCharacterSequences == null) {
    		unseparableCharacterSequences = new ArrayList<String>();
    	}
    	unseparableCharacterSequences.add(ucs);
    }

    public void removeUserDefinedSeparatingCharacter(Character character) {
    	if (userDefinedSeparatingCharacters != null) {
    		userDefinedSeparatingCharacters.remove(character);
    	}
    }
    
    public void removeUnseparableCharacterSequence(String ucs) {
    	if (unseparableCharacterSequences != null) {
    		unseparableCharacterSequences.remove(ucs);
    	}
    }
}
