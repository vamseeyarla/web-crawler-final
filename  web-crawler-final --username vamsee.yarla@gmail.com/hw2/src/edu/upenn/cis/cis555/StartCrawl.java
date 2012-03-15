/**
 * 
 */
package edu.upenn.cis.cis555;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.w3c.dom.Document;

/**
 * @author VamseeKYarlagadda
 *
 */
public class StartCrawl {
String URL;
String XPath;
XPathEngine engine;
	
	StartCrawl(String URL, String XPath)
	{
		this.URL=URL;
		this.XPath=XPath;
		String[] XPaths=new String[1];
		XPaths[0]=this.XPath;
		engine=new XPathEngine(XPaths);
	}
	
	public void URLCrawl(String URL)
	{
		System.out.println("URL:     "+URL);
		ArrayList<String> subURLs=null;
		
		HttpClient client=new HttpClient(URL);
		ByteArrayOutputStream stream=client.fetchData();
		System.out.println("CON TYPE:    "+client.ConType);
		String content=stream.toString();
		//TODO MIGHT DO MD5 TO CHECK IF CONTENT IS ALREADY PARSED OR NOT.
		System.out.println("VAMSEE");
		Document root=engine.createDOM(stream, client);
		System.out.println("VAMSEE1");
		boolean[] status=engine.evaluate(root);
		System.out.println("VAMSEE2");
		if(status[0])
		{
			System.out.println("TRUE");
			//TODO Save the URL and content in Berkeley DB
		}
		else
		{
			System.out.println("FALSE");
		}
		
		int CurPos=0;
		int pos;
		while((pos=content.indexOf("href", CurPos))!=-1)
		{
			pos=pos+4;
			int subpos; //This is for finding where is "="
			if( ((subpos =content.indexOf("=",pos))==-1) || !content.substring(pos, subpos).trim().equalsIgnoreCase(""))
			{
				break;
			}
			int quotepos;
			if( ((quotepos =content.indexOf("\"",subpos+1))==-1) || !content.substring(subpos+1, quotepos).trim().equalsIgnoreCase(""))
			{
				break;
			}
			String suburl="";
			int trackpos=quotepos+1;
			while(content.charAt(trackpos)!='"')
			{
				suburl=suburl.concat(String.valueOf(content.charAt(trackpos)));
				trackpos++;
			}
			subURLs= new ArrayList<String>();
			subURLs.add(suburl.trim());	
			
		}
		System.out.println(subURLs);
		if(subURLs!=null)
		{
		for(int count=0;count<subURLs.size();count++)
		{
			System.out.println("URL:    "+ subURLs.get(count));
			URLCrawl(subURLs.get(count));
		}
		}
		
		
	}
}
