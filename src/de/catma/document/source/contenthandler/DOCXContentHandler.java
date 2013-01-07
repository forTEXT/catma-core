package de.catma.document.source.contenthandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import de.catma.document.source.FileOSType;

public class DOCXContentHandler extends AbstractSourceContentHandler {

	@Override
	public void load(InputStream is) throws IOException {
		XWPFDocument doc = new XWPFDocument(is);
		XWPFWordExtractor wordExtractor = new XWPFWordExtractor(doc);
		String buf = wordExtractor.getText();
		
    	//it's still microsoft after all
		if (FileOSType.getFileOSType(buf).equals(FileOSType.UNIX)) {
			buf = FileOSType.convertUnixToDos(buf);
		}
		
		setContent(buf);
	}

	@Override
	public void load() throws IOException {
		BufferedInputStream bis = null;
		try {
			
			bis = new BufferedInputStream(
				getSourceDocumentInfo().getTechInfoSet().getURI().toURL().openStream());
			
			load(bis);
		}
		finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

}
