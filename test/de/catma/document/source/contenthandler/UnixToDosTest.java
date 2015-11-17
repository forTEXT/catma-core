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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import de.catma.document.source.FileOSType;

public class UnixToDosTest {
	
	public UnixToDosTest(String in, String out) throws IOException {
		copy(in, out);
	}

	private void copy(String in, String out) throws IOException {
		FileInputStream is = new FileInputStream(new File(in));
		
		StringBuilder contentBuffer = new StringBuilder(); 
		
		BufferedInputStream bis = null;

		try {
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
				fr = new BOMFilterInputStream( toCharBis, Charset.forName("UTF-8"));
			}
			else {
				fr = toCharBis;
			}

			BufferedReader reader = new BufferedReader(
					new InputStreamReader( fr,  Charset.forName("UTF-8") ) );
			
			char[] charBuf = new char[65536];
			int cCount = -1;
	        while((cCount=reader.read(charBuf)) != -1) {
	        	contentBuffer.append( charBuf, 0, cCount);
	        }
    		String buf = contentBuffer.toString();
    		System.out.println("before: " + buf.length());
    		if (FileOSType.getFileOSType(buf).equals(FileOSType.UNIX)) {
    			buf = FileOSType.convertUnixToDos(buf);
    		}
    		System.out.println("after: " + buf.length());
    		
    		if (out != null) {
    			Writer repoSourceFileWriter =  
    					new BufferedWriter(new OutputStreamWriter(
    							new FileOutputStream(out),
    							"UTF-8"));
    			try {
    				repoSourceFileWriter.append(buf);
    			}
    			finally {
    				if (repoSourceFileWriter != null) {
    					repoSourceFileWriter.close();
    				}
    			}
    		}
    		else {
    			System.out.println(buf);
    		}

		}
		finally {
			if( bis != null ) {
				bis.close();
			}
		}	
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new UnixToDosTest(args[0], (args.length>1)?args[1]:null);
	}

}
