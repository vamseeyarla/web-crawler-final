/**
 * 
 */
package edu.upenn.cis.cis555;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.w3c.dom.Document;

/**
 * @author VamseeKYarlagadda
 *
 */
public class StartCrawl {
String URL;

int NumHtml=0;
int NumXml=0;

HashMap<String,ArrayList<String>> XPaths;
XPathEngine engine;
Double MaxSize;
//ArrayList<String> subURLs=null;
String[] XPathsList;
int accessed=0;
String Directory;
int NumFiles;
boolean terminate=false;
HashMap<String,String> servers=new HashMap<String,String>();
URLFrontier frontier;

/*
 * 
 * Constructir which takes all the values to loads the methods of 
 * this class. It takes all the list of xpaths to be crawled
 * and the url to be starting with and also the max sixe 
 * of the file to lookm ut for and aslo the maximum number 
 * of files to be extracted.
 */
	StartCrawl(String URL, HashMap<String,ArrayList<String>> XPaths, Double MaxSize,String Directory,int NumFiles)
	{
		/*
		XPaths=new Hashtable<String,ArrayList<String>>();
		XPaths.put("/html/head",new ArrayList<String>());
		XPaths.put("/html/body/a",new ArrayList<String>());
		XPaths.put("/html/head/title",new ArrayList<String>());
		*/
		this.Directory=Directory;
		this.URL=URL;
		this.XPaths=XPaths;
		this.MaxSize=MaxSize;
		this.NumFiles=NumFiles;
		frontier=new URLFrontier();
		
		XPathsList=new String[this.XPaths.size()];
        int i=0;
		for(String temp: this.XPaths.keySet())
		{
			XPathsList[i]=temp;
			i++;
		}
		//System.out.println("XPATHS:  :"+ XPaths);
		engine=new XPathEngine(XPathsList);
		
	//	subURLs=new ArrayList<String>();
	}
	
	
	/*
	 * URLCrawl method that takes the input of the URL and check with the all the
	 * XPaths given and if anything matches; it proceeds to updation of the
	 * HashList and proceed with the derived links from it.
	 *  */
	public void URLCrawl(String URL)
	{
	
		DB db=DB.getInstance(Directory);
		if(XPaths.size()==0)
		{
			//System.out.println("NO XPaths to look for!");
			return;
		}
		boolean state=true;

		//System.out.println("URL:     "+URL);
	 
		HttpClient client=new HttpClient(URL,MaxSize,db.getURLTimestamp(URL));	
		ByteArrayOutputStream stream=client.fetchData();
		
		if(stream==null)
		{
			//System.out.println("Null File");
			//db.updateCrawlData(URL,//System.currentTimeMillis(),null);
		    //System.out.println("PROBLEM WITH URL OR NULL URL FROM HTTP CLIENT");
			//state=false;
			//	return;
		}
		else
		{
	
	/*	HttpClient client=new HttpClient(URL,MaxSize,db.getURLTimestamp(URL));	
		ByteArrayOutputStream stream=client.fetchData();
		*/
	
		
		while(state)
		{
			String content;
			//System.out.println("CON TYPE:    "+client.ConType);
			
			if(client.ConType.equalsIgnoreCase("XML"))
			{
				NumXml++;
			}
			else if(client.ConType.equalsIgnoreCase("HTML"))
			{
				NumHtml++;
			}
		      if(stream.toString().equalsIgnoreCase("NOTMODIFIED"))
		      {
		    	  //System.out.println("NOT MODIFIED");
		     	  content=db.getCrawledURLData(URL);
		    	  
		    	  stream=new ByteArrayOutputStream();
		    	  try{
		    		//  System.out.println(content);
		    	//	  System.out.println("MANOJ");
		    	  stream.write(content.getBytes());
		    	//  System.out.println("MANOJ1");
		    //	  System.out.println(stream.toString());
		    	  }
		    	  catch(Exception e)
		    	  {
		    		  stream=null;
		    	  }
		      }	
		      else
		      {
		          content=stream.toString();
		          
		          boolean contentSeen=ContentSeenTest.isContentSeen(content, Directory);
		          if(contentSeen)
		          {
		        	  //System.out.println("OLD CONTENT: Content has already been seen");
		          }
		          else
		          {
		        	  //System.out.println("NEW CONTENT: Content has not not seen before");
		          }
		      }
		
		//TODO: Update CrawlData
		
		//  db.updateCrawlData(URL,System.currentTimeMillis(),content);
		
	/*	
		//TODO MIGHT DO MD5 TO CHECK IF CONTENT IS ALREADY PARSED OR NOT.
		System.out.println("VAMSEE");
		System.out.println(stream.toString());
		System.out.println(client.ConType);
		System.out.println(client.Hostname);
		System.out.println(client.Link);
		*/
		      
		Document root=engine.createDOM(stream, client);
		
		if(root==null)
		{
			//System.out.println("PROBLEM WITH ROOT FROM XPathCRAWLER");
			break;
		}
		
		//System.out.println("VAMSEE1");
		boolean[] status=engine.evaluate(root);
		//System.out.println("VAMSEE2");
		
		for(int iterator=0;iterator<status.length;iterator++)
		{
		if(status[iterator])
		{
			db.updateCrawlData(URL,System.currentTimeMillis(),content);
			
			//System.out.println("TRUE FOR: "+XPathsList[iterator]+"  FOR URL:  "+URL);
			//TODO Save the URL and content in Berkeley DB
			ArrayList<String> temp=XPaths.get(XPathsList[iterator]);
			temp.add(URL);
			XPaths.put(XPathsList[iterator],temp);
			temp=null;
		}
		else
		{
			//System.out.println("FALSE FOR: "+XPathsList[iterator]+"  FOR URL:  "+URL);
		}
		}
		
		int CurPos=0;
		int pos;
		
		//System.out.println("YAHOOOOOOOOOOOOOOOOOOOOOO");
		//System.out.println(content);
		/*
		if(((pos=content.indexOf("href", CurPos))!=-1) || ((pos=content.indexOf("HREF", CurPos))!=-1) )
		{
		subURLs= new ArrayList<String>();
		}
		*/
		while(((pos=content.indexOf("href", CurPos))!=-1) || ((pos=content.indexOf("HREF", CurPos))!=-1))
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
			suburl=suburl.trim();
			
			if(suburl.indexOf("http")==0||suburl.indexOf("HTTP")==0)
			{
				//TODO: CHECK IF URL HAS ALREADY BEEN PARSED
				//subURLs.add(suburl);	
				frontier.put(suburl);
			}
			else
			{
			//	System.out.println("YAHOO");
				if(client.Link.equalsIgnoreCase("/"))
				{
					//TODO: CHECK IF URL HAS ALREADY BEEN PARSED
				//subURLs.add("http://".concat(client.Hostname).concat("/").concat(suburl));
				
				frontier.put("http://".concat(client.Hostname).concat("/").concat(suburl));
				}
				else
				{
					//TODO: CHECK IF URL HAS ALREADY BEEN PARSED
			//	subURLs.add("http://".concat(client.Hostname).concat(client.Link).concat("/").concat(suburl));	
				frontier.put("http://".concat(client.Hostname).concat(client.Link).concat("/").concat(suburl));
				
				}
			}
			
			CurPos=pos+1;
		}
	//	System.out.println("GMAIl");
		//System.out.println(frontier);
	//	System.out.println("GMAIL@");
		
		state=false;
		
		
		}
		
		}//ELSE 
		
		//last_accessed_time=System.currentTimeMillis();
		
		
		String agent;
		
		if(client.crawl_delay.containsKey(client.useragent))
		{
			agent=client.useragent;
		}
		else
		{
			agent="*";
		}
		
		if(client.Hostname!=null)
		{
		if(servers.containsKey(client.Hostname))
		{
			servers.put(client.Hostname, String.valueOf(System.currentTimeMillis()));
		}
		else
		{
			boolean match=false;
			for(String s: servers.keySet())
			{
				if(client.Hostname.indexOf(s)!=-1)
				{
					match=true;
					servers.put(client.Hostname, String.valueOf(System.currentTimeMillis()));
					break;
				}
			}
			
			if(!match)
			{
				servers.put(client.Hostname, String.valueOf(System.currentTimeMillis()));
			}
		}
		}
		
		if(client.crawl_delay.containsKey(agent))
		{
			//System.out.println("GOING TO SLEEP FOR: "+client.crawl_delay.get(agent));
		try{
			Thread.sleep(Integer.parseInt(client.crawl_delay.get(agent)));
			}
			catch(Exception e)
			{
				//System.out.println("CRAWL DELAY THREAD INTERRUPTED");
			}
		}
		
		
		
		/*
		if(subURLs!=null)
		{
		for(int count=0;count<subURLs.size();count++)
		{
			System.out.println("URL:    "+ subURLs.get(count));
			URLCrawl(subURLs.get(count));
		}
		}
		*/
		
		if(terminate)
		{
			return;
		}
		
		String link;
		//if(subURLs.size()!=0)
		if((link=frontier.get())!=null)
		{
			
			accessed++;
			if(NumFiles!=-1 && accessed==NumFiles)
			{
				//System.out.println("TOTAL:   "+accessed);
				return;
			}
			URLCrawl(link);
		}
		
		
	}
}
