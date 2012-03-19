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
public class DBClose extends Thread {

	private Environment env;
	private EntityStore user_store;
	private EntityStore channel_store;
	
	
	public DBClose(Environment env, EntityStore user_store,EntityStore channel_store)
	{
		this.env=env;
		this.user_store=user_store;
		this.channel_store=channel_store;
	}
	
	public void run()
	{
		try{
			if(env!=null)
			{
				user_store.close();
				channel_store.close();
				env.cleanLog();
				env.close();
				System.out.println("Database Closed");
			}
			}
		catch(Exception e)
		{
			System.out.println("Database not closed properly");
		}
	}
}
