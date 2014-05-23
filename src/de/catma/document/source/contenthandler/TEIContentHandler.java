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
public class TEIContentHandler extends XMLContentHandler {
	static List<String> nonlinebreakingElements = new ArrayList<String>();
	static {
		nonlinebreakingElements.add("abbr");
		nonlinebreakingElements.add("add");
		nonlinebreakingElements.add("expan");
		nonlinebreakingElements.add("corr");
		nonlinebreakingElements.add("date");
		nonlinebreakingElements.add("del");
		nonlinebreakingElements.add("distinct");
		nonlinebreakingElements.add("emph");
		nonlinebreakingElements.add("foreign");
		nonlinebreakingElements.add("gap");
		nonlinebreakingElements.add("gloss");
		nonlinebreakingElements.add("hi");
		nonlinebreakingElements.add("index");
		nonlinebreakingElements.add("measure");
		nonlinebreakingElements.add("mentioned");
		nonlinebreakingElements.add("milestone");
		nonlinebreakingElements.add("num");
		nonlinebreakingElements.add("orig");
		nonlinebreakingElements.add("q");
		nonlinebreakingElements.add("quote");
		nonlinebreakingElements.add("ref");
		nonlinebreakingElements.add("reg");
		nonlinebreakingElements.add("rs");
		nonlinebreakingElements.add("said");
		nonlinebreakingElements.add("sic");
		nonlinebreakingElements.add("soCalled");
		nonlinebreakingElements.add("sp");
		nonlinebreakingElements.add("street");
		nonlinebreakingElements.add("term");
		nonlinebreakingElements.add("time");
		nonlinebreakingElements.add("unclear");
	}
	
	private List<StaticMarkupInstance> staticMarkupInstances = null;
	
	public TEIContentHandler() {
		nonlinebreakingElements = new ArrayList<String>();
	}
	
}
