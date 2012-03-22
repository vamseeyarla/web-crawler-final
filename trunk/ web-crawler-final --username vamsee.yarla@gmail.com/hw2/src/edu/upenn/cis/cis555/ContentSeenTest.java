/**
 * 
 */
package edu.upenn.cis.cis555;

import java.security.MessageDigest;

/**
 * @author VamseeKYarlagadda
 *
 */

/*
 * Content Seen test to check whether the fetched data has already been 
 * in the Crawled data or not. Returns a boolean type and all the checking s done with
 * the help of the SHA-1 message digest. 
 * It returns a 160 bit key and checked for matching with already present data.
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
