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

	public static XPathCrawler crawler=null;
	StartCrawl crawling=null;
	boolean start=false;
	
	
	public static void main(String[] args)
	{
		crawler=new XPathCrawler();
		crawler.runCrawler(args);
	//	crawler=null;
	}
	/*
	 * 
	 * This method acts as the starting method of all crawling. IT takes 4 inputs; URL, DB
	 * location, Max sixe of file, MAx number of files and actvates the crawling assocuiated with this 
	 * inputs.
	 */
	
	public void runCrawler(String[] args)
	{
		String URL;
		String Directory;
		double MaxSize=10;
		int NumFiles=-1;
		start=true;
		
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
				//System..out.println("The Passed directory is not a valid path! Program terminated");
				break;
			}
			if(!f.exists())
			{
				if(!f.mkdirs())
				{
					System..out.println("Couldn't create the directory structure! Program terminated");
					break;
				}
			}
			*/
			
			Directory=args[1];
		//	Directory="JEDB";
			
			DB db=DB.getInstance(Directory);
			if(db==null)
			{
				System.out.println("PROGRAM TERMINATED!");
				break;
			}
			
			//MAXIMUM SIZE OF FILE
			try{
			MaxSize=Double.parseDouble(args[2]);
			}
			catch(Exception e)
			{
				//MaxSize=10;
				System.out.println("Error in parsing Max File Size. Program Terminated!");
			}
			
			//OPTIONAL ARGUMENT
			if(args.length>3)
			{
			try{	
				NumFiles=Integer.parseInt(args[3]);
			}
			catch(Exception e)
			{
				System.out.println("Error in parsing Max no of Files to be parsed. Program Terminated!");
			}
			
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
		crawling= new StartCrawl(URL,XPaths,MaxSize,Directory,NumFiles);
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
		//crawling=null;
	}
	
	/*
	 * 
	 * This method deletes all the crawled data in the DB before; thereby 
	 * making the results of the Admin run crawling more realistic
	 */
	public boolean deleteCrawlData(String arg)
	{
		DB db=DB.getInstance(arg);
		return db.deleteCrawlData();
	}
	
	/*
	 * 
	 * This method takes the DB locationa nd chekcs all the size of the URLs fetched and thus stored in the DB.
	 * Used by ADMIN statistics
	 */
	
	public double findTotalSize(String arg)
	{
		DB db=DB.getInstance(arg);
		double total=0;
		
		for(CrawlData s: db.CrawlIndex.entities())
		{
			total=total+((s.Data.length()));
		}
		return total;
			
	}
	
	/*
	 * 
	 * This method takes the DB location and checks all the matched Xpaths with their URLs 
	 * and calcualtes the count of each channel and return it.
	 * Used by ADMIN statistics
	 */
	public Hashtable<String,Integer> findMatchedDocsSize(String arg)
	{
		DB db=DB.getInstance(arg);
		
		Hashtable<String,Integer> channel=new Hashtable<String,Integer>();
		
		for(ChannelData s: db.ChannelIndex.entities())
		{
			int total=0;
			for(String xpath: s.XPaths.keySet())
			{
				total=total+(s.XPaths.get(xpath).size());
			}
			channel.put(s.ID+" : "+s.Name, total);
		}
		
			return channel;
	}
	
}
