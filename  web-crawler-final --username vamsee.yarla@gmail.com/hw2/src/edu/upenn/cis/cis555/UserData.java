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
@Entity
public class UserData {

	public String Password;
	public ArrayList<String> XPaths;
	//public ArrayList<String> URLs;
	
	
@PrimaryKey
	public String Username;
	

public UserData()
{
	
}
	
//public UserData(String Username,String Password, String[] XPaths, String[] URLs)
public UserData(String Username,String Password, String[] XPaths)
{
	this.Username=Username;
	this.Password=Password;
	this.XPaths=new ArrayList<String>();
	//this.URLs=new ArrayList<String>();
	
	
	for(int i=0;i<XPaths.length;i++)
	{
		if(!this.XPaths.contains(XPaths[i]))
		{
			this.XPaths.add(XPaths[i]);
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
