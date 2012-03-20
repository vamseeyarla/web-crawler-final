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
    static DB db=null;
	
	public DB(String Directory)
	{
		this.Directory=Directory;			
	}
	
	public static DB getInstance(String Directory)
	{
		if(db==null)
		{
		db=new DB(Directory);
		if(!db.init())
		{
			System.out.println("FAIL INIT");
			db=null;
		}
		}
		
		return db;	
	}
	
	public boolean init()
	{
		File dir = new File(Directory);
		
		if(!dir.exists())
		{
		boolean success=dir.mkdirs();
		
		if(success)
		{
			System.out.println("Created the DB Directory");
				}
		else
		{
			System.out.println("Cannot Create the DB Directory");
			
		}
		}
		try{
		
		envConfig=new EnvironmentConfig();
		storeConfig=new StoreConfig();
		
		envConfig.setReadOnly(false);
		storeConfig.setReadOnly(false);
		
		envConfig.setAllowCreate(true);
		storeConfig.setAllowCreate(true);
		//storeConfig.setExclusiveCreate(true);
		
		env = new Environment(dir, envConfig);
		
		storeUserDetails=new EntityStore(env, "Users", storeConfig);
		storeChannels=new EntityStore(env, "Channels", storeConfig);
		
		UserIndex=storeUserDetails.getPrimaryIndex(String.class, UserData.class);
		
		ChannelIndex=storeChannels.getPrimaryIndex(String.class, ChannelData.class);
		
		DBClose closingHook=new DBClose(env, storeUserDetails,storeChannels);
		Runtime.getRuntime().addShutdownHook(closingHook);
		env.sync();
		return true;
		}
		catch(Exception e)
		{
			
			System.out.println(e.toString());
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
		if(db.UserIndex.get(Username)!=null)
		{
			env.sync();
			return true;
		}
			else
		{
			env.sync();
			return false;
		}
		
		}
	
	public String nextChannelID()
	{
		EntityCursor<ChannelData> channeldata= db.ChannelIndex.entities();
		int Max=0;
		System.out.println("CHANNEL STATUS:  "+channeldata);
		if(channeldata==null)
		{
			env.sync();
			return "1";
		}
		
		for(ChannelData temp: channeldata)
		{
		    if(Integer.parseInt(temp.ID) > Max)
		    {
		    	Max=Integer.parseInt(temp.ID);
		    }
		}
		env.sync();
		return String.valueOf(Max+1);
	}
	
	public boolean addUser(String Username, String Password)
	{
	try{
			UserData data=new UserData();
			data.Username=Username;
			data.Password=Password;
			data.Channels=new ArrayList<String>();		
			db.UserIndex.put(data);	
			System.out.println("New User Success");
			env.sync();
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Error in creating New User");
			env.sync();
			return false;
		}
	}
	
	
	public boolean addChannel(ChannelData data)
	{
	try{
			
			db.ChannelIndex.put(data);	
			System.out.println("New Channel Success");
			env.sync();
			return true;
		}
		catch(Exception e)
		{
			env.sync();
			System.out.println("Error in creating New Channel");
			return false;
		}
	}
	
	public UserData login(String Username, String Password)
	{
		UserData temp=db.UserIndex.get(Username);
		
		if(temp==null)
			return null;
		else
			if(temp.Password.equals(Password))
			{
				env.sync();
				return temp;
			}
			else
			{
				env.sync();
				return null;
			}
	}
	
	public boolean deleteChannel(String ID)
	{
		if(db.ChannelIndex.delete(ID))
		{
		env.sync();
		return true;
		}
		return false;
	}
	
	public boolean updateData(UserData data)
	{
		if(db.UserIndex.put(data)!=null)
		{
		env.sync();	
		return true;
		}
		return false;
		
	}
	
	
	public void close()
	{
		env.sync();
		DBClose closingHook=new DBClose(env, storeUserDetails,storeChannels);
		closingHook.start();
	}
	
	public boolean deleteData()
	{
		try{
		for(UserData d : UserIndex.entities())
		{
			UserIndex.delete(d.Username);
		}
		for(ChannelData d : ChannelIndex.entities())
		{
			ChannelIndex.delete(d.ID);
		}
		env.sync();
		
		return true;
		}
		catch(Exception e)
		{
			return false;	
		}
	}
	
}
