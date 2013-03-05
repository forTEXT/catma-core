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
package de.catma.document.source.contenthandler;

import java.io.IOException;

import de.catma.document.source.SourceDocumentInfo;

public abstract class AbstractSourceContentHandler implements SourceContentHandler {

    private SourceDocumentInfo sourceDocumentInfo;
    private String content;
    
    public void setSourceDocumentInfo(SourceDocumentInfo sourceDocumentInfo) {
		this.sourceDocumentInfo = sourceDocumentInfo;
	}
    
    public SourceDocumentInfo getSourceDocumentInfo() {
		return sourceDocumentInfo;
	}

	public String getContent() throws IOException {
		if (content == null) {
			load();
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void unload() {
		content = null;
	}

    @Override
    public boolean isLoaded() {
    	return (content != null);
    }
}
