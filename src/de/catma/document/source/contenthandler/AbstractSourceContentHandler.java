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
