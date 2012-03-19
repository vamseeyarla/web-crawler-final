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
	
	
@PrimaryKey
	public String ID;
	

public ChannelData()
{
	
}
	
public ChannelData(String ID, String[] XPaths, String URL)
{
	this.ID=ID;
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
