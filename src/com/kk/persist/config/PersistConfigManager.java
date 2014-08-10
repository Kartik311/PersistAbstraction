/**
 * 
 */
package com.kk.persist.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Kartikeya
 *
 */
public class PersistConfigManager extends DefaultHandler {
	boolean setProperty;
	private InputStream inputStream = null;
	private Properties configProperty = new Properties();
	private String value = null;
	private String key = null;
	
	public Properties configurePersist(){
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			//File configFile = new File("c:/persis.xml");
			inputStream = this.getClass().getClassLoader().getResourceAsStream("persist.xml");
			saxParser.parse(inputStream, this);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return configProperty;
	}
	
	@Override
	public void characters(char[] buffer, int start, int length){
		value = new String(buffer, start, length);
	}
	
	@Override
	public void startElement (String uri, String localname, String qName, Attributes attributes) throws SAXException{
		key = "";
		value = "";
		if(qName.equalsIgnoreCase("property")){
			key = attributes.getValue("name");
		}
	}
	
	@Override
	public void endElement(String uri, String localname, String qName)
		throws SAXException{
		if(qName.equalsIgnoreCase("property")){
			configProperty.setProperty(key, value);
		}
	}
}
