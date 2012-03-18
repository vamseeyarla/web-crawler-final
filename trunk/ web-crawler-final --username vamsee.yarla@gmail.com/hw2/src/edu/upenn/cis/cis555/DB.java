package edu.upenn.cis.cis555;

import java.awt.image.DataBufferShort;
import java.io.File;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

public class DB {

	
	
	public static void main(String[] args)
	{
		EntityStore store;
		PrimaryIndex<String, String> index;
		
		File dir = new File("JEDB");
		boolean success=dir.mkdirs();
		if(success)
		{
			System.out.println("Created the DB Directory");
				}
		else
		{
			System.out.println("Cannot Create the DB Directory");
			
		}
		
		
		EnvironmentConfig envConfig=new EnvironmentConfig();
		StoreConfig storeConfig=new StoreConfig();
		envConfig.setAllowCreate(true);
		storeConfig.setAllowCreate(true);
		
		Environment env = new Environment(dir, envConfig);
		
		store=new EntityStore(env, "CIS555", storeConfig);
		index=store.getPrimaryIndex(String.class, String.class);
		
		
		
		//Runtime.getRuntime().
		//DbShutdownHook shutdown=ne
		
	}
}
