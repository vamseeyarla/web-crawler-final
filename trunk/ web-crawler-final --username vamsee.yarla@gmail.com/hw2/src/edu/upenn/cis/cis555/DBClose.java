/**
 * 
 */
package edu.upenn.cis.cis555;

import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityStore;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * Class to commit all the database transactions and close all it repositories.
 * 
 */
public class DBClose extends Thread {

	private Environment env;
	private EntityStore user_store;
	private EntityStore channel_store;
	private EntityStore crawl_store;
	
	public DBClose(Environment env, EntityStore user_store,EntityStore channel_store,EntityStore crawl_store)
	{
		this.env=env;
		this.user_store=user_store;
		this.channel_store=channel_store;
		this.crawl_store=crawl_store;
	}
	
	public void run()
	{
		try{
			if(env!=null)
			{
				
				//System.out.println("Stage1");
				user_store.close();
				//System.out.println("Stage2");
				channel_store.close();
				//System.out.println("Stage3");
				crawl_store.close();
				//System.out.println("Stage4");
				env.cleanLog();
				//System.out.println("Stage5");
				env.close();
				//System.out.println("Database Closed");
			}
			}
		catch(Exception e)
		{
			//System.out.println("Database not closed properly");
		}
	}
}
