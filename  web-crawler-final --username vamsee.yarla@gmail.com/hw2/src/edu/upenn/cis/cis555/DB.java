package edu.upenn.cis.cis555;

import java.io.File;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

public class DB {

	public String Directory;
	EntityStore storeUserDetails;
	EntityStore storeChannels;
	PrimaryIndex <String, UserData> UserIndex;
	PrimaryIndex <String, ChannelData> ChannelIndex;
	
	public DB(String Directory)
	{
		
		this.Directory=Directory;
					
	}
	
	public boolean init()
	{
		File dir = new File(Directory);
		boolean success=dir.mkdirs();
		if(success)
		{
			System.out.println("Created the DB Directory");
				}
		else
		{
			System.out.println("Cannot Create the DB Directory");
			
		}
		try{
		
		EnvironmentConfig envConfig=new EnvironmentConfig();
		StoreConfig storeConfig=new StoreConfig();
		envConfig.setAllowCreate(true);
		storeConfig.setAllowCreate(true);
		
		Environment env = new Environment(dir, envConfig);
		
		storeUserDetails=new EntityStore(env, "Users", storeConfig);
		storeChannels=new EntityStore(env, "Channels", storeConfig);
		
		UserIndex=storeUserDetails.getPrimaryIndex(String.class, UserData.class);
		
		ChannelIndex=storeChannels.getPrimaryIndex(String.class, ChannelData.class);
		
		DBClose closingHook=new DBClose(env, storeUserDetails);
		Runtime.getRuntime().addShutdownHook(closingHook);
		return true;
		}
		catch(Exception e)
		{
			System.out.println("Error in Connecting to Berkeley DB");
			return false;
		}
		
		/*
		
		String[] temp={"hi","Kri"};
		UserData x=new UserData("vamsee","krish",temp);
		
		UserData y=new UserData("manoj","krishna yarlagadda",temp);
		UserIndex.put(y);
		ChannelIndex.put(x);
	
		UserIndex.delete("manoj");
		ChannelIndex.delete("vamsee");
		
		UserData result=UserIndex.get("manoj");
		
		System.out.println(result.Password);
		result=ChannelIndex.get("vamsee");
		System.out.println(result.Password);
	*/	
	}
	
	
}
