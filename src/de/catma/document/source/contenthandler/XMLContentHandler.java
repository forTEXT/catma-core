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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.swing.plaf.ColorUIResource;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import de.catma.document.Range;
import de.catma.document.source.ContentInfoSet;
import de.catma.document.source.FileOSType;
import de.catma.document.source.FileType;
import de.catma.document.source.SourceDocumentInfo;
import de.catma.document.source.TechInfoSet;
import de.catma.document.standoffmarkup.staticmarkup.StaticMarkupInstance;
import de.catma.document.standoffmarkup.usermarkup.TagReference;
import de.catma.document.standoffmarkup.usermarkup.UserMarkupCollection;
import de.catma.tag.Property;
import de.catma.tag.PropertyDefinition;
import de.catma.tag.PropertyPossibleValueList;
import de.catma.tag.PropertyValueList;
import de.catma.tag.TagDefinition;
import de.catma.tag.TagInstance;
import de.catma.tag.TagLibrary;
import de.catma.tag.TagManager;
import de.catma.tag.TagsetDefinition;
import de.catma.tag.Version;
import de.catma.util.CloseSafe;
import de.catma.util.Collections3;
import de.catma.util.ColorConverter;
import de.catma.util.IDGenerator;
import de.catma.util.Pair;

/**
 * A content handler HTML based {@link de.catma.document.source.SourceDocument}s.
 *
 * @author marco.petris@web.de
 *
 */
public class XMLContentHandler extends AbstractSourceContentHandler {
	protected List<String> nonlinebreakingElements = new ArrayList<String>();

	private List<StaticMarkupInstance> staticMarkupInstances = null;
	private IDGenerator idGenerator = new IDGenerator();
	private HashMap<String, TagDefinition> pathToTagDefMap = new HashMap<>();
 
	public XMLContentHandler() {
		nonlinebreakingElements = new ArrayList<String>();
	}
	
	
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
			
	        Builder builder = new Builder();
	        Document document = builder.build(fr);
	        StringBuilder contentBuilder = new StringBuilder();
	        staticMarkupInstances = new ArrayList<StaticMarkupInstance>();
	        TagManager tagManager = new TagManager(); //this should be provided by the Repository later on
	        TagLibrary tagLibrary = new TagLibrary(null, "Intrinsic Markup");
	        TagsetDefinition tagsetDefinition = 
	        		new TagsetDefinition(
	        			null, idGenerator.generate(), 
	        			"Intrinsic Markup", new Version());
	        
	        tagManager.addTagsetDefinition(tagLibrary, tagsetDefinition);
	        Stack<String> elementStack = new Stack<String>();
	        UserMarkupCollection userMarkupCollection = 
	        	new UserMarkupCollection(
	        		null, 
	        		new ContentInfoSet("", "Intrinsic Markup", "", "Intrinsic Markup"), 
	        		tagLibrary);
	        
	        processTextNodes(
	        		contentBuilder, 
	        		document.getRootElement(), 
	        		elementStack, tagManager,
	        		tagLibrary, tagsetDefinition,
	        		userMarkupCollection);
	        for (TagsetDefinition tagsetDef : userMarkupCollection.getTagLibrary()) {
	        	for (TagDefinition tagDef : tagsetDef) {
	        		System.out.println(tagDef);
	        	}
	        }
	        for (TagReference tr : userMarkupCollection.getTagReferences()) {
	        	System.out.println(tr);
	        }
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
     * @throws URISyntaxException 
     */
    private void processTextNodes(
    		StringBuilder contentBuilder, Element element,
    		Stack<String> elementStack, TagManager tagManager, 
    		TagLibrary tagLibrary, 
    		TagsetDefinition tagsetDefinition,
    		UserMarkupCollection userMarkupCollection) throws URISyntaxException {
    	
		int start = contentBuilder.length();


		StringBuilder pathBuilder = new StringBuilder();
        for (int j=0; j<elementStack.size(); j++){
        	pathBuilder.append(elementStack.get(j) + "/");
        }
        
        String parentPath = pathBuilder.toString();
        
        elementStack.push(element.getQualifiedName()); 
        String path = parentPath + "/" + elementStack.peek();

        TagDefinition tagDefinition = pathToTagDefMap.get(path);
        
        if (tagDefinition == null) {
        	TagDefinition parentTag = pathToTagDefMap.get(parentPath);
        	String parentUuid = (parentTag==null)?null:parentTag.getUuid();
        	tagDefinition = 
        		new TagDefinition(
        			null, idGenerator.generate(), 
        			elementStack.peek(), new Version(), null, parentUuid);
        	PropertyDefinition colorDef = 
        		new PropertyDefinition(
        			null, 
        			idGenerator.generate(), 
        			PropertyDefinition.SystemPropertyName.catma_displaycolor.name(), 
        			new PropertyPossibleValueList(
        				ColorConverter.toRGBIntAsString(ColorConverter.randomHex())));
        	tagDefinition.addSystemPropertyDefinition(colorDef);
        			
        	pathToTagDefMap.put(path, tagDefinition);
        	tagManager.addTagDefinition(tagsetDefinition, tagDefinition);
        }

		for( int idx=0; idx<element.getChildCount(); idx++) {
            Node curChild = element.getChild(idx);
            if (curChild instanceof Text) {
            	if (!nonlinebreakingElements.contains(element.getLocalName())) {
            		contentBuilder.append("\n");
            	}
            	if (!curChild.getValue().trim().isEmpty()) {
            		contentBuilder.append(curChild.getValue());
            	}
            }
            else if (curChild instanceof Element) { //descent
                processTextNodes(
                	contentBuilder, 
                	(Element)curChild, 
                	elementStack,
                	tagManager,
                	tagLibrary,
                	tagsetDefinition,
                	userMarkupCollection);
            
            }
        }
        
        int end = contentBuilder.length();
        Range range = new Range(start,end);

        TagInstance tagInstance = new TagInstance(idGenerator.generate(), tagDefinition);

        for (int i=0; i<element.getAttributeCount(); i++) {
        	PropertyDefinition propertyDefinition = 
        		tagDefinition.getPropertyDefinitionByName(
        				element.getAttribute(i).getQualifiedName());
        	
        	if (propertyDefinition == null) {
        		propertyDefinition = 
        			new PropertyDefinition(
        				null, idGenerator.generate(), 
        				element.getAttribute(i).getQualifiedName(), 
        				new PropertyPossibleValueList(element.getAttribute(i).getValue()));
        		tagManager.addUserDefinedPropertyDefinition(tagDefinition, propertyDefinition);
        	}
        	else if (!propertyDefinition
        			.getPossibleValueList()
        			.getPropertyValueList()
        			.getValues().contains(element.getAttribute(i).getValue())) {
        		List<String> newValueList = new ArrayList<>();
        		newValueList.addAll(
        				propertyDefinition
        						.getPossibleValueList()
        						.getPropertyValueList()
        						.getValues());
        		newValueList.add(element.getAttribute(i).getValue());
        		propertyDefinition.setPossibleValueList(
        			new PropertyPossibleValueList(newValueList, false));
        		
        	}
        	Property property = 
        		new Property(
        			propertyDefinition, 
        			new PropertyValueList(element.getAttribute(i).getValue()));
        	tagInstance.addUserDefinedProperty(property);
        }
        														
        TagReference tagReference = new TagReference(tagInstance, "sourceDocID", range);
        userMarkupCollection.addTagReference(tagReference);
     
        elementStack.pop();	
    }
    
    @Override
    public List<StaticMarkupInstance> getStaticMarkupInstances() {
		return staticMarkupInstances;
	}
    
    public static void main(String[] args) {
    	XMLContentHandler contentHandler = new XMLContentHandler();
    	
    	contentHandler.setSourceDocumentInfo(new SourceDocumentInfo(
    			null, null, new TechInfoSet(FileType.XML, Charset.forName("UTF-8"), 
    					FileOSType.DOS, 0L, null)));
    	
		try {

			String file1 = new String("C:/data/projects/catma/miles/ca1_2009_2_1_Chris_Miles.xml");
			//String file2 = new String("Z:\\dlt000189.xml");
			contentHandler.load(new FileInputStream(file1));
			//contentHandler.load(new FileInputStream(file2));

//			String file1 = new String("Y:\\ca1_2009_2_1_Chris_Miles.xml");
//			String file2 = new String("Y:\\dlt000189.xml");
			String file2 = new String("Y:\\LIBANIUS_VOL. I. FASC. I  ORATIONES I-V.xml");
//			contentHandler.load(new FileInputStream(file1));
//			contentHandler.load(new FileInputStream(file2));

			
			//System.out.println:
//			System.out.println("content:");
//			System.out.println("***"+contentHandler.getContent()+"***");
//			System.out.println("StaticMarkupInstances:");
//			for (int x=0; x<contentHandler.staticMarkupInstances.size(); x++){
//				System.out.println(contentHandler.staticMarkupInstances.get(x).getRange());
//				System.out.println(contentHandler.staticMarkupInstances.get(x).getPath());
//				System.out.println(contentHandler.staticMarkupInstances.get(x).getAttributes());
//				System.out.println();
//			}
			//System.out.println(contentHandler.getStaticMarkupInstances());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
