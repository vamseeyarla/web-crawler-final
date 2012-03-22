/**
 * 
 */
package edu.upenn.cis.cis555;

import java.util.ArrayList;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * Entity to save all the User data: from their Name, Passwords, Username and also the
 * channels that get created in the system. It helps to maintain a single repository 
 * of everything rather than storing all the data separately. 
 * 
 */
@Entity
public class UserData {

	public String Name;
	public String Password;
	public ArrayList<String> Channels;
	//public ArrayList<String> URLs;
	
	
@PrimaryKey
	public String Username;
	

public UserData()
{
	
}
	
//public UserData(String Username,String Password, String[] XPaths, String[] URLs)
public UserData(String Username,String Password, String[] Channels, String Name)
{
	this.Name=Name;
	this.Username=Username;
	this.Password=Password;
	this.Channels=new ArrayList<String>();
	//this.URLs=new ArrayList<String>();
	
	
	for(int i=0;i<Channels.length;i++)
	{
		if(!this.Channels.contains(Channels[i]))
		{
			this.Channels.add(Channels[i]);
		}
	}
	
	/*
	for(int i=0;i<URLs.length;i++)
	{
		if(!this.URLs.contains(URLs[i]))
		{
			this.URLs.add(URLs[i]);
		}
	}
	*/
	
}

}
