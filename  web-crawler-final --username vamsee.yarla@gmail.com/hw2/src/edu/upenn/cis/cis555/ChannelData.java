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
public class ChannelData {


	public ArrayList<String> XPaths;
	public String URL;
	public String Name;
	
	
@PrimaryKey
	public String ID;
	

public ChannelData()
{
	
}
	
public ChannelData(String ID, String Name, String[] XPaths, String URL)
{
	this.ID=ID;
	this.Name=Name;
	this.XPaths=new ArrayList<String>();
	//this.URLs=new ArrayList<String>();
	
	
	for(int i=0;i<XPaths.length;i++)
	{
		if(!this.XPaths.contains(XPaths[i]))
		{
			this.XPaths.add(XPaths[i]);
		}
	}
	
	this.URL=URL;
	
}

}
