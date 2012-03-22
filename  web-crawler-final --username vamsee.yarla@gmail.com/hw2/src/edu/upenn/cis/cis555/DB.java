package edu.upenn.cis.cis555;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
	EntityStore storeCrawl;
	PrimaryIndex <String, UserData> UserIndex;
	PrimaryIndex <String, ChannelData> ChannelIndex;
	PrimaryIndex <String, CrawlData> CrawlIndex;
	Environment env;
	EnvironmentConfig envConfig;
	StoreConfig storeConfig;
    static DB db=null;
	
    /*
     * Contrcutor to take DB directory as input
     */
	public DB(String Directory)
	{
		this.Directory=Directory;			
	}
	
	 /*
     * Function which takes Directory as input and returns the object of 
     * this class. this ensures only one instance of the 
     * object has been created.
     */
	public static DB getInstance(String Directory)
	{
		if(db==null)
		{
		db=new DB(Directory);
		if(!db.init())
		{
			//System.out.println("FAIL INIT");
			db=null;
		}
		}
		
		return db;	
	}
	
	/*
	 * Initializes the fields required for opening the DB and makes sure
	 * everything is working perfectly or not. If not it returns false
	 */
	public boolean init()
	{
		File dir = new File(Directory);
		
		if(!dir.exists())
		{
		boolean success=dir.mkdirs();
		
		if(success)
		{
			//System.out.println("Created the DB Directory");
				}
		else
		{
			//System.out.println("Cannot Create the DB Directory");
			
		}
		}
		try{
		
		envConfig=new EnvironmentConfig();
		storeConfig=new StoreConfig();
		
		envConfig.setReadOnly(false);
		storeConfig.setReadOnly(false);
		envConfig.setLocking(false);
		//storeConfig.setLocking(false);
		
		envConfig.setAllowCreate(true);
		storeConfig.setAllowCreate(true);
		//storeConfig.setExclusiveCreate(true);
		
		env = new Environment(dir, envConfig);
		
		storeUserDetails=new EntityStore(env, "Users", storeConfig);
		storeChannels=new EntityStore(env, "Channels", storeConfig);
		storeCrawl=new EntityStore(env, "CrawlInfo", storeConfig);
		
		UserIndex=storeUserDetails.getPrimaryIndex(String.class, UserData.class);
		
		ChannelIndex=storeChannels.getPrimaryIndex(String.class, ChannelData.class);
		
		CrawlIndex=storeCrawl.getPrimaryIndex(String.class, CrawlData.class);
		
		DBClose closingHook=new DBClose(env, storeUserDetails,storeChannels,storeCrawl);
		Runtime.getRuntime().addShutdownHook(closingHook);
		env.sync();
		return true;
		}
		catch(Exception e)
		{
			
			//System.out.println(e.toString());
			//System.out.println("Error in Connecting to Berkeley DB");
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
	

	
	/*
	 * Function to check whther the given user exists in the system or not. 
	 * If exists, returns true.otherwise false
	 * It helps in creating new logins
	 * 
	 */
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
	
	/*
	 * Function to autogenerate a number for the next channel ID and using this vaue
	 * the CHannelData class can actually save the data Uniquely.
	 * 
	 */
	public String nextChannelID()
	{
		EntityCursor<ChannelData> channeldata= db.ChannelIndex.entities();
		int Max=0;
		//System.out.println("CHANNEL STATUS:  "+channeldata);
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
	
	/*
	 * Function to take Name, Username, password as input and saves it accrodingly in the database.
	 * The checking for existing username is already done by previous methods. 
	 * This gets activated only when everything is unique.
	 * 
	 * 
	 */
	public boolean addUser(String Username, String Password, String Name)
	{
	try{
			UserData data=new UserData();
			data.Name=Name;
			data.Username=Username;
			data.Password=Password;
			data.Channels=new ArrayList<String>();		
			db.UserIndex.put(data);	
			//System.out.println("New User Success");
			env.sync();
			return true;
		}
		catch(Exception e)
		{
			//System.out.println("Error in creating New User");
			env.sync();
			return false;
		}
	}
	
	/*
	 * Function to add a channel to the DB. It populates the object of Channed
	 * Data with data like Xpaths, Name, ID and saves it in the DB.
	 * 
	 */
	
	public boolean addChannel(ChannelData data)
	{
	try{
			
			db.ChannelIndex.put(data);	
			//System.out.println("New Channel Success");
			env.sync();
			return true;
		}
		catch(Exception e)
		{
			env.sync();
			//System.out.println("Error in creating New Channel");
			return false;
		}
	}
	
	
	/*
	 * Function to check if the user exists in the ystem or not
	 * if so, returns an object of UserData with complete info about the users
	 * 
	 */
	
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
	
	/*
	 * Function to delete a channel from DB if a user has requested to do so.
	 * It deletes the channel along with their XPaths and commits the DB
	 * 
	 */
	
	public boolean deleteChannel(String ID)
	{
		//System.out.println(db.ChannelIndex.get(ID).Name);
		if(db.ChannelIndex.delete(ID))
		{
		env.sync();
		return true;
		}
		return false;
	}
	
	/*
	 * Updates the UserData object when the crawl has been done and or if any new channel was 
	 * inserted by the user.
	 * It helps in keeping track of user channels and data.
	 * 
	 */
	
	public boolean updateData(UserData data)
	{
		if(db.UserIndex.put(data)!=null)
		{
		env.sync();	
		return true;
		}
		return false;
		
	}
	
	/*
	 * Function to close the DB by committing everything.
	 * 
	 */
	public void close()
	{
		env.sync();
		DBClose closingHook=new DBClose(env, storeUserDetails,storeChannels,storeCrawl);
		closingHook.start();
	}
	
	/*
	 * Function to delete entire data from the database.
	 * 
	 */
	
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
			for(CrawlData d : CrawlIndex.entities())
			{
			CrawlIndex.delete(d.URL);
			}
		env.sync();
		System.out.println("DELETED ALL DATA FROM DB");
		return true;
		}
		catch(Exception e)
		{
			return false;	
		}
	}
	
	/*
	 * Function to delete crawled data from the database.
	 * Employed by the method called by the admin programmer
	 * 
	 */
	public boolean deleteCrawlData()
	{
		try{
		   for(CrawlData d : CrawlIndex.entities())
			{
			CrawlIndex.delete(d.URL);
			}
		env.sync();
		
		return true;
		}
		catch(Exception e)
		{
			return false;	
		}
	}
	
	/*
	 * Function to update the values in the DB. The correspoding XPaths and all
	 * the matching URLs with it.
	 * 
	 */
	public boolean updateValues(HashMap<String,ArrayList<String>> XPaths)
	{
		//System.out.println(XPaths);
		
		for(ChannelData data: ChannelIndex.entities())
		{
			HashMap<String,ArrayList<String>> org=new HashMap<String, ArrayList<String>>();
			for(String xpath: data.XPaths.keySet())
			{
				org.put(xpath, XPaths.get(xpath));
			}
			data.XPaths=org;
			ChannelIndex.put(data);
		}
		env.sync();
		return true;
	}
	
	/*
	 * Function to retrieve ChannelData using the identifier ID.
	 * Returns an ChannelData object out.
	 * 
	 */
	public ChannelData getChannelData(String ID)
	{
		return ChannelIndex.get(ID);
	}
	
	/*
	 * Function to update crawled data in the DB. updated in the crawl index
	 * It also save the timestamp of the document at which it was retrived.
	 * 
	 */
	public boolean updateCrawlData(String URL, long Time,String Data)
	{
		try{
		 Date headDate =new Date(Time);
         DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
  	   	 String date=headformatter.format(headDate).concat(" GMT");
		 
  	   	 CrawlIndex.put(new CrawlData(URL,date,Data));
  	   	 env.sync();
  	   	 return true;
		}
		catch(Exception e)
		{env.sync();
			return false;
		}
	}
	
	/*
	 * Function to check whether the URL has been crawled before
	 * or not. If so, retrives the data of it rather than fetchcing it agian form the browser. 
	 * 
	 */
	public boolean checkURLCrawled(String URL)
	{
	    
		if(CrawlIndex.get(URL)==null)
		{
			env.sync();
			return false;
		}
		else
		{
			env.sync();
			return true;
		}
	}
	
	/*
	 * Function to returve the timestamp of the URL. Used for IF-mOdified-Since header
	 * 
	 */
	public String getURLTimestamp(String URL)
	{
		if(CrawlIndex.get(URL)!=null)
		{
			env.sync();
		return CrawlIndex.get(URL).Timestamp;
		}
		else
		{
			env.sync();
			return null;
		}
		}

	/*
	 * Function to retrieve the crawled data from the DB if it has already been crawled.
	 */
	public String getCrawledURLData(String URL)
	{
		
		return CrawlIndex.get(URL).Data;
	
	}
	

}
