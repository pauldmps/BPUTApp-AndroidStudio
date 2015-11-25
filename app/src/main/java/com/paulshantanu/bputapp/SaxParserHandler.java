package com.paulshantanu.bputapp;

/* 
 * This class implements the basic SAX XML Parser to handle the XML parsing of the notices.
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParserHandler extends DefaultHandler {

	public static final int NOTICE_PARSER = 0;
	public static final int SCHEDULE_PARSER = 1;

	private int parser_type;

	int i = 0;
	String temp_str;
	private Notice notice_obj;
	private Schedule schedule_obj;

    SaxParserHandler(int parser_type) {
        this.parser_type = parser_type;
    }
	
	public Notice getNotice() {
		return notice_obj;
	}

    public Schedule getSchedule() {return schedule_obj;}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

        if(parser_type == SaxParserHandler.NOTICE_PARSER) {
            notice_obj = new Notice();
        }
        else if(parser_type == SaxParserHandler.SCHEDULE_PARSER) {
            schedule_obj = new Schedule();
        }

	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(qName.equals("notice")){
		notice_obj.setUrl(i, attributes.getValue(0));
	    }
		else if(qName.equals("thead")){
			notice_obj.setHas_table(true);
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)throws SAXException {
		super.characters(ch, start, length);		
		temp_str= new String(ch,start,length).trim();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.equals("notice")){
			notice_obj.setNotice_head(i,temp_str);
			i++;
		}
		else if (qName.equals("text")) {
        	notice_obj.setNotice_body(temp_str);
        }
        else if (qName.equals("thead")){
        	notice_obj.setTable_head(temp_str);
        }
        else if (qName.equals("td")){
        	notice_obj.setTable_body(temp_str);	
        }
        else if (qName.equals("semester")) {
            schedule_obj.setSemester(temp_str);
        }
        else if (qName.equals("branch")){
            schedule_obj.setBranch(temp_str);
        }
        else if (qName.equals("index")){
            schedule_obj.setIndex(temp_str);
        }
        else if (qName.equals("date")){
            schedule_obj.setDate(temp_str);
        }
        else if (qName.equals("sitting")){
            schedule_obj.setSitting(temp_str);
        }
        else if (qName.equals("code")){
            schedule_obj.setCode(temp_str);
        }
        else if (qName.equals("subject")){
            schedule_obj.setSubject(temp_str);
        }
	}
}
