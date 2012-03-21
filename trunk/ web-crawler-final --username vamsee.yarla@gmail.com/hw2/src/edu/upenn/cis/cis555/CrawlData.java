/**
 * 
 */
package edu.upenn.cis.cis555;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * @author VamseeKYarlagadda
 *
 */
@Entity
public class CrawlData {


	public String Timestamp;
//	public String Name;
	public String Data;
	public String DataHash;
	
	
@PrimaryKey
	public String URL;
	
public CrawlData()
{
	
}
	
public CrawlData(String URL, String Timestamp,String Data)
{
	
	this.URL=URL;
	this.Timestamp=Timestamp;
	this.Data=Data;
	
	if(this.Data!=null)
	{
	  MessageDigest md;
	  try{
	    md = MessageDigest.getInstance("SHA-1");
	    byte[] sha1hash = new byte[40];
	    md.update(Data.getBytes(), 0, Data.length());
	    sha1hash = md.digest();
	    this.DataHash=new String(sha1hash);
	  }
	  catch(Exception e)
	  {
		  this.DataHash=null;
	  }
	}
	else
	{
		this.Data=null;
		this.DataHash=null;
	}
	
}

}
