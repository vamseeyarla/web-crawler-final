/**
 * 
 */
package edu.upenn.cis.cis555;

import java.util.ArrayList;
import java.util.HashMap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * @author VamseeKYarlagadda
 *
 */
@Entity
public class ChannelData {


	public HashMap<String,ArrayList<String>> XPaths;
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
	this.XPaths=new HashMap<String,ArrayList<String>>();
	//this.URLs=new ArrayList<String>();
	
	
	for(int i=0;i<XPaths.length;i++)
	{
		if(!this.XPaths.containsKey(XPaths[i]))
		{
			this.XPaths.put(XPaths[i],new ArrayList<String>());
		}
	}
	
	this.URL=URL;
	
}

}
