/**
 * 
 */
package test.edu.upenn.cis.cis555.hw2;

import java.io.ByteArrayOutputStream;

import edu.upenn.cis.cis555.ChannelData;
import edu.upenn.cis.cis555.DB;
import edu.upenn.cis.cis555.HttpClient;
import edu.upenn.cis.cis555.UserData;
import junit.framework.TestCase;

/**
 * @author VamseeKYarlagadda
 *
 */
public class DBTests extends TestCase {

	
	public void testCase1()
	{
		DB db=DB.getInstance("JEDB");
		db.addUser("vam","kri","Vamsee");
		db.addUser("Man","yar","Manoj");    
		assertEquals("Vamsee", db.login("vam", "kri").Name);
	}
	
	public void testCase2()
	{
		DB db=DB.getInstance("JEDB");	
		db.addUser("vam","kri","Vamsee");
		db.addUser("Man","yar","Manoj");  
		assertNotSame("Vamsee", db.login("Man", "yar").Name);
	}
	
	public void testCase3()
	{
		DB db=DB.getInstance("JEDB");	
		String[] xpaths=new String[2];
		xpaths[0]="/xml";
		xpaths[1]="/html";
		ChannelData data=new ChannelData("1","CH1",xpaths,"http://www.upenn.edu");
		db.addChannel(data);
		assertNotSame("CH1", db.getChannelData("1").Name);
	}
		
}
