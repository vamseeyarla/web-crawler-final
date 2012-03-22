/**
 * 
 */
package edu.upenn.cis.cis555;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * Thread class to ivoke the URLCrawler class when called upon the ADMIN using remotely.
 * We require thread as the crawling is to be separate from existing thread running.Only then we can stop
 * and control the process going on.
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
			//System.out.println("Crawler NOT FREE!! SLEEPING OVER");
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
			//System.out.println("CRAWL DATA DELETED");
			crawlObj.runCrawler(args);
			
			running=false;
			//System.out.println("FINISHED WITH CRAWLER");
		}
		
	}
}
