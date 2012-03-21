/**
 * 
 */
package edu.upenn.cis.cis555;

/**
 * @author VamseeKYarlagadda
 *
 */
public class InvokeCrawler extends Thread {

	String[] args;
	boolean running=false;
	XPathCrawler crawlObj;
	
	public void run()
	{
		if(XPathCrawler.crawler!=null)
		{
			running=false;
			System.out.println("Crawler NOT FREE!! SLEEPING OVER");
			try{
			Thread.sleep(5);
			}
			catch(Exception e)
			{
				
			}
		}
		else
		{
				
			running=true;
			XPathCrawler.crawler=new XPathCrawler();
			crawlObj=XPathCrawler.crawler;
			crawlObj.deleteCrawlData(args[1]);
			System.out.println("CRAWL DATA DELETED");
			crawlObj.runCrawler(args);
			
			running=false;
			System.out.println("FINISHED WITH CRAWLER");
		}
		
	}
}
