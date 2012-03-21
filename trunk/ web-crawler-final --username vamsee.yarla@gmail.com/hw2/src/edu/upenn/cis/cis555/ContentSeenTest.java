/**
 * 
 */
package edu.upenn.cis.cis555;

import java.security.MessageDigest;

/**
 * @author VamseeKYarlagadda
 *
 */
public class ContentSeenTest {

	public static boolean  isContentSeen(String content,String Directory)
	{
		 MessageDigest md;
		 String DataHash;
		  try{
		    md = MessageDigest.getInstance("SHA-1");
		    byte[] sha1hash = new byte[40];
		    md.update(content.getBytes(), 0, content.length());
		    sha1hash = md.digest();
		    DataHash=new String(sha1hash);
		    
		    DB db=DB.getInstance(Directory);
			for(CrawlData data: db.CrawlIndex.entities())
			{
				if(data.DataHash.equals(DataHash))
				{
					return true;
				}
			}
		    
		    
		  }
		  catch(Exception e)
		  {
			  return false;
		  }
		
		  return false;
	}
	
}
