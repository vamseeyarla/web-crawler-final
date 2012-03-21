/**
 * 
 */
package edu.upenn.cis.cis555;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.sleepycat.persist.EntityCursor;

/**
 * @author VamseeKYarlagadda
 *
 */
public class XPathCrawler {

	public static void main(String[] args)
	{
		String URL;
		String Directory;
		int MaxSize;
		int NumFiles;
		
		boolean start=true;
		
		while(start)
		{
		
		if(args.length < 3 || args.length > 4)
		{
			System.out.println("Error in the arguments passed to the Program! Program terminated");
			break;
		}
		else
		{
		try{
			
			//URL PARSING
			URL l=new URL(args[0]);
			URL = args[0];
		
			/*
			//DIRECTORY PARSING
			File f=new File(args[1]);
			boolean status=f.isAbsolute();
			
			if(!status || !f.isFile())
			{
				System.out.println("The Passed directory is not a valid path! Program terminated");
				break;
			}
			if(!f.exists())
			{
				if(!f.mkdirs())
				{
					System.out.println("Couldn't create the directory structure! Program terminated");
					break;
				}
			}
			*/
			
			Directory=args[1];
			Directory="JEDB";
			
			DB db=DB.getInstance(Directory);
			if(db==null)
			{
				System.out.println("PROGRAM TERMINATED!");
				break;
			}
			
			//MAXIMUM SIZE OF FILE
			MaxSize=Integer.parseInt(args[2]);
			
			//OPTIONAL ARGUMENT
			if(args.length>3)
			{
				NumFiles=Integer.parseInt(args[3]);
			}
			
			}
			catch(MalformedURLException e)
			{
				System.out.println("The URL passed is not a valid URL! Program terminated");
				break;
			}
			catch(NumberFormatException e1)
			{
				System.out.println("The MaxSize/Number of Files passed is not a valid integer! Program terminated");
				break;
			}
		}
		
		//START THE PROCESS OF CRAWLING
		
		
		
		HashMap<String,ArrayList<String>> XPaths=new HashMap<String,ArrayList<String>>();
		
		EntityCursor<ChannelData> channels=DB.db.ChannelIndex.entities();
		
		for(ChannelData temp: channels)
		{
			for(String s: temp.XPaths.keySet())
			{
				if(!XPaths.containsKey(s))
				{
					ArrayList<String>URLs=new ArrayList<String>();
					XPaths.put(s,URLs);
				}
			}
		}
		
		//String XPath="/html/body";
		StartCrawl crawling= new StartCrawl(URL,XPaths,MaxSize);
		crawling.URLCrawl(URL);
		
		XPaths=crawling.XPaths;
		
		for(String temp: XPaths.keySet())
		{
			System.out.println(temp+" : "+XPaths.get(temp));
		}
		
		DB db=DB.getInstance(Directory);
		db.updateValues(XPaths);
		
		start=false;
		
		}
	}
}
