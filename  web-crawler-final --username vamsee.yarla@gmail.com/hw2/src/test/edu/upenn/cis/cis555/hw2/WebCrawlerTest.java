/**
 * 
 */
package test.edu.upenn.cis.cis555.hw2;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.omg.CORBA.Current;

import edu.upenn.cis.cis555.ChannelData;
import edu.upenn.cis.cis555.CrawlData;
import edu.upenn.cis.cis555.DB;
import edu.upenn.cis.cis555.HttpClient;
import edu.upenn.cis.cis555.UserData;
import edu.upenn.cis.cis555.XPathCrawler;
import junit.framework.TestCase;

/**
 * @author VamseeKYarlagadda
 *
 */
public class WebCrawlerTest extends TestCase {

	
	public void testCase1()
	{
	
		
		DB db=DB.getInstance("JEDB");
		
		db.addUser("vam","kri","Vamsee");
		
		String[] xpaths=new String[2];
		xpaths[0]="/xml";
		xpaths[1]="/html";
		ChannelData data=new ChannelData("1","CH1",xpaths,"http://www.upenn.edu/vam.xsl");
		db.addChannel(data);
		
		UserData datax=db.login("vam", "kri");
		ArrayList<String> tempx=new ArrayList<String>();
		tempx.add("1");
		datax.Channels=tempx;
		db.updateData(datax);
		
		xpaths=new String[4];
		xpaths[0]="http://crawltest.cis.upenn.edu/";
		xpaths[1]="JEDB";
		xpaths[2]="10";
		xpaths[3]="1";
		XPathCrawler.main(xpaths);
		 
	
		assertEquals(true, db.checkURLCrawled("http://crawltest.cis.upenn.edu/"));
	}
	
	public void testCase2()
	{
		
		DB db=DB.getInstance("JEDB");
		
		db.addUser("vam","kri","Vamsee");
		
		String[] xpaths=new String[2];
		xpaths[0]="/xml";
		xpaths[1]="/html";
		ChannelData data=new ChannelData("1","CH1",xpaths,"http://www.upenn.edu/vam.xsl");
		db.addChannel(data);
		
		UserData datax=db.login("vam", "kri");
		ArrayList<String> tempx=new ArrayList<String>();
		tempx.add("1");
		datax.Channels=tempx;
		db.updateData(datax);
		db.deleteCrawlData();
		
		
		xpaths=new String[4];
		xpaths[0]="http://crawltest.cis.upenn.edu/";
		xpaths[1]="JEDB";
		xpaths[2]="10";
		xpaths[3]="2";
		XPathCrawler.main(xpaths);
		 
		int count=0;
		for(CrawlData datca: db.CrawlIndex.entities())
		{
			count++;
		}
	
		assertEquals(true, (count <= 2));
	}
		
}
