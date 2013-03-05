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
