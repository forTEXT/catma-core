/*   
 *   CATMA Computer Aided Text Markup and Analysis
 *   
 *   Copyright (C) 2009  University Of Hamburg
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

package de.catma.tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * A version information of a {@link Versionable}. E.g. a {@link TagDefinition} or 
 * a {@link Tagset}.<br>
 * A version string has the format: 
 * {@link Integer versionNumber}_{@link UUID}<br>
 * A negative version number marks the {@link Versionable} as deleted.<br>
 * <br>
 * Note this class is immutable! Version changes return a new Version!
 *
 * @see Versionable
 *
 * @author Marco Petris
 *
 */
public class Version {
	
	private static final SimpleDateFormat FORMAT = 
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private static final SimpleDateFormat SHORTFORMAT = 
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	private long version;
	
	public Version( Date version ) {
		this.version = version.getTime();
	}
	
	public Version() {
		version = new Date().getTime();
	}
	
	public Version(String versionString) {
		try {
			if (versionString.length() == 24) {
				this.version = SHORTFORMAT.parse(versionString).getTime();
			}
			else {
				this.version = FORMAT.parse(versionString).getTime();
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public Version(Version toCopy) {
		this.version = toCopy.version;
	}

	/**
	 * @return the string representation of this {@link Version}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return FORMAT.format(version);
	}

    public boolean isNewer(Version other) {
        return this.version < other.version;
    }
    
    public Date getDate() {
    	return new Date(version);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (version ^ (version >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Version)) {
			return false;
		}
		Version other = (Version) obj;
		if (version != other.version) {
			return false;
		}
		return true;
	}

}
