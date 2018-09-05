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
package de.catma.serialization;

import java.io.IOException;
import java.io.OutputStream;

import nu.xom.Document;
import nu.xom.Serializer;

public class DocumentSerializer {
	public void serialize(Document document, OutputStream outputStream) throws IOException {
//			outputStream.write( BOMFilterInputStream.UTF_8_BOM ); // some jdks do not write it on their own
		Serializer serializer = new Serializer( outputStream );
		serializer.setIndent( 4 );
		serializer.write( document );
	}
}
