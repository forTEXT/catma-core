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

package de.catma.document.source.contenthandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * The standard content handler which handles plain text files.
 *
 * @author marco.petris@web.de
 * @see de.catma.document.source.TechInfoSet
 */
public class StandardContentHandler extends AbstractSourceContentHandler {
	
	/* (non-Javadoc)
	 * @see de.catma.document.source.contenthandler.SourceContentHandler#load(java.io.InputStream)
	 */
	public void load(InputStream is) throws IOException {
		
		Charset charset = 
			getSourceDocumentInfo().getTechInfoSet().getCharset();
		
		StringBuilder contentBuffer = new StringBuilder(); 
		
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
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

		BufferedReader reader = new BufferedReader(
				new InputStreamReader( fr, charset ) );
		
		char[] charBuf = new char[65536];
		int cCount = -1;
        while((cCount=reader.read(charBuf)) != -1) {
        	contentBuffer.append( charBuf, 0, cCount);
        }

        // some texts seem to include non valid unicode characters
        // and this causes problems when converting text to HTML
        // for GUI delivery and during indexing 
		setContent(
			contentBuffer.toString().replaceAll(
				"[^\\x09\\x0A\\x0D\\x20-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFFF]", "?"));
	}

    /* (non-Javadoc)
     * @see de.catma.document.source.contenthandler.SourceContentHandler#load()
     */
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
    
	@SuppressWarnings("unused")
	private void showBytes( File file, int byteCount ) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream( file );
			for( int i=0; i<byteCount; i++ ) {
				System.out.printf( "%1$x\n", fis.read() );
			}
			
		}
		catch( Exception exc ) {
			exc.printStackTrace();
		}
		finally {
			if( fis != null ) {
				try {
					fis.close();
				} catch( IOException ignored) {}
			}
		}
	}
}
