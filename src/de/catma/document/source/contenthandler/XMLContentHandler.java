/*
 * CATMA Computer Aided Text Markup and Analysis
 *
 *    Copyright (C) 2008-2010  University Of Hamburg
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.catma.document.source.contenthandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.catma.document.Range;
import de.catma.document.source.FileOSType;
import de.catma.document.source.FileType;
import de.catma.document.source.SourceDocumentInfo;
import de.catma.document.source.TechInfoSet;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupCollection;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupInstance;
import de.catma.util.CloseSafe;
import de.catma.util.Pair;

/**
 * A content handler HTML based {@link de.catma.document.source.SourceDocument}s.
 *
 * @author marco.petris@web.de
 *
 */
public class XMLContentHandler extends AbstractSourceContentHandler {
	private List<StaticMarkupInstance> staticMarkupInstances = null;
	/* (non-Javadoc)
	 * @see de.catma.document.source.contenthandler.SourceContentHandler#load(java.io.InputStream)
	 */
	public void load(InputStream is) throws IOException {
//        XMLReader reader;
		
		try {
			Charset charset = 
					getSourceDocumentInfo().getTechInfoSet().getCharset();
				
			BufferedInputStream bis = null;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bis = new BufferedInputStream(is);
            byte[] byteBuffer = new byte[65536];
            int bCount = -1;
            while ((bCount=bis.read(byteBuffer)) != -1) {
                bos.write(byteBuffer, 0, bCount);
            }

            byte[] byteBuf = bos.toByteArray();
            ByteArrayInputStream toCharBis = new ByteArrayInputStream(byteBuf);

			InputStream fr = null; 
			if (BOMFilterInputStream.hasBOM(byteBuf)) {
				fr = new BOMFilterInputStream( toCharBis, charset );
			}
			else {
				fr = toCharBis;
			}
			
//			reader = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
	        Builder builder = new Builder();
	        Document document = builder.build(fr);
	        StringBuilder contentBuilder = new StringBuilder();
	        staticMarkupInstances = new ArrayList<StaticMarkupInstance>();
	        
	        processTextNodes(contentBuilder, document.getRootElement(), staticMarkupInstances);
	        setContent(contentBuilder.toString());	
			CloseSafe.close(is);
		} catch (Exception e) {
			CloseSafe.close(is);
			throw new IOException(e);
		}
	}
    
    /* (non-Javadoc)
     * @see de.catma.document.source.contenthandler.SourceContentHandler#load()
     */
    public void load() throws IOException {
    	
        try {
        	InputStream is = getSourceDocumentInfo().getTechInfoSet().getURI().toURL().openStream();
        	try {
        		load(is);
        	}
        	finally {
        		is.close();
        	}
        }
        catch (Exception e) {
        	throw new IOException(e);
        }
        

    }

    /**
     * Appends text elements to the given builder otherwise descents deeper into the
     * document tree.
     * @param contentBuilder the builder is filled with text elements
     * @param element the current element to process
     */
    private void processTextNodes(StringBuilder contentBuilder, Element element, List<StaticMarkupInstance> currentElementList) {
    	 	// start
		int start = contentBuilder.length();
		Stack elementStack = null; // muss auﬂerhalb passieren
    		
		// list or stack Element
		elementStack.push(element); //NullPointerException

		for( int idx=0; idx<element.getChildCount(); idx++) {
            Node curChild = element.getChild(idx);
            if (curChild instanceof Text) {
                contentBuilder.append(curChild.getValue());
            }
            else if (curChild instanceof Element) { //descent
                processTextNodes(contentBuilder, (Element)curChild, currentElementList);
            
            }
        }
        
        // end
        int end = contentBuilder.length();
        Range range = new Range(start,end);
        List<Pair<String,String>> attributes = null; //NullPointerException
        for (int i=0; i<element.getAttributeCount(); i++){
        	Pair pair = new Pair(element.getAttribute(i).getQualifiedName(),element.getAttribute(i).getValue());
        	attributes.add(pair);
        }
     // new StaticMarkupInstance for element
        														// getPath(elementStack)
        currentElementList.add(new StaticMarkupInstance(range, element.getValue(), attributes));
     // delete element from list or stack
        elementStack.pop();	
    }
    
    public List<StaticMarkupInstance> getStaticMarkupInstances() {
		return staticMarkupInstances;
	}
    
    public static void main(String[] args) {
    	XMLContentHandler contentHandler = new XMLContentHandler();
    	
    	contentHandler.setSourceDocumentInfo(new SourceDocumentInfo(
    			null, null, new TechInfoSet(FileType.XML, Charset.forName("UTF-8"), 
    					FileOSType.DOS, 0L, null)));
    	
		try {
			contentHandler.load(new FileInputStream("Pfad"));
			
			//System.out.println:
			contentHandler.getContent();
			contentHandler.getStaticMarkupInstances();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
