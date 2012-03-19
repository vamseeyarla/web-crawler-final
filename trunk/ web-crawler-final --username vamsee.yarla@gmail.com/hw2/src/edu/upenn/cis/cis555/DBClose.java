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
	private EntityStore store;
	
	
	
	public DBClose(Environment env, EntityStore store)
	{
		this.env=env;
		this.store=store;
	}
	
	public void run()
	{
		try{
			if(env!=null)
			{
				store.close();
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
