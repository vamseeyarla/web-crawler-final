package edu.upenn.cis.cis555;

import java.io.File;
import java.util.ArrayList;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

public class DB {

	public String Directory;
	EntityStore storeUserDetails;
	EntityStore storeChannels;
	PrimaryIndex <String, UserData> UserIndex;
	PrimaryIndex <String, ChannelData> ChannelIndex;
	Environment env;
	EnvironmentConfig envConfig;
	StoreConfig storeConfig;
	
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
		
		envConfig=new EnvironmentConfig();
		storeConfig=new StoreConfig();
		envConfig.setAllowCreate(true);
		storeConfig.setAllowCreate(true);
		
		env = new Environment(dir, envConfig);
		
		storeUserDetails=new EntityStore(env, "Users", storeConfig);
		storeChannels=new EntityStore(env, "Channels", storeConfig);
		
		UserIndex=storeUserDetails.getPrimaryIndex(String.class, UserData.class);
		
		ChannelIndex=storeChannels.getPrimaryIndex(String.class, ChannelData.class);
		
		DBClose closingHook=new DBClose(env, storeUserDetails,storeChannels);
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
	

	
	
	public boolean checkUserExists(String Username)
	{
		if(UserIndex.get(Username)!=null)
			return true;
		else
			return false;
	}
	
	public String nextChannelID()
	{
		EntityCursor<ChannelData> channeldata= ChannelIndex.entities();
		int Max=0;
		if(channeldata.count()==0)
		{
			return "1";
		}
		
		for(ChannelData temp: channeldata)
		{
		    if(Integer.parseInt(temp.ID) > Max)
		    {
		    	Max=Integer.parseInt(temp.ID);
		    }
		}
		
		return String.valueOf(Max+1);
	}
	
	public boolean addUser(String Username, String Password)
	{
	try{
			UserData data=new UserData();
			data.Username=Username;
			data.Password=Password;
			data.Channels=new ArrayList<String>();		
			UserIndex.put(data);	
			System.out.println("New User Success");
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Error in creating New User");
			return false;
		}
	}
	
	
	public UserData login(String Username, String Password)
	{
		UserData temp=UserIndex.get(Username);
		
		if(temp==null)
			return null;
		else
			if(temp.Password.equals(Password))
			{
				return temp;
			}
			else
			{
				return null;
			}
	}
	
	public void close()
	{
		DBClose closingHook=new DBClose(env, storeUserDetails,storeChannels);
		closingHook.start();
	}
	
	
}
