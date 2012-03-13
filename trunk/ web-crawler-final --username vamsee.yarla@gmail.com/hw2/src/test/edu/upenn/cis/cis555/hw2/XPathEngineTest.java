/**
 * 
 */
package test.edu.upenn.cis.cis555.hw2;

import java.io.ByteArrayOutputStream;

import org.w3c.dom.Document;

import edu.upenn.cis.cis555.HttpClient;
import edu.upenn.cis.cis555.XPathEngine;
import junit.framework.TestCase;

/**
 * @author VamseeKYarlagadda
 *
 */
public class XPathEngineTest extends TestCase {

	public void testCase1()
	{
		String []xpaths=new String[1];
		xpaths[0]="/dblp/photos/images";
		
		XPathEngine engine=new XPathEngine(xpaths);
		assertEquals(true, engine.isValid(1));
	}
	
	public void testCase2()
	{
		String []xpaths=new String[1];
		xpaths[0]="/web-app/description[text()=\"cis\"]/mail";
		
		XPathEngine engine=new XPathEngine(xpaths);
		assertEquals(true, engine.isValid(1));
	}
	public void testCase3()
	{
		String []xpaths=new String[1];
		xpaths[0]="/dblp/photos/imdb/disney[gmaled/yahoo/gmail]";
		
		XPathEngine engine=new XPathEngine(xpaths);
		assertEquals(true, engine.isValid(1));
	}
	public void testCase4()
	{
		String []xpaths=new String[1];
		xpaths[0]="/web-app/description[@name=\"manoj\"][text()=\"cis\"][contains(text(),\"cis\")][mail[description[@name=\"vasu\"][text()=\"seas\"]]]";
		
		XPathEngine engine=new XPathEngine(xpaths);
		assertEquals(true, engine.isValid(1));
	}
	public void testCase5()
	{
		String []xpaths=new String[1];
		xpaths[0]="/web-app[@v%/@2=\"vamsee\"]";
		
		XPathEngine engine=new XPathEngine(xpaths);
		assertEquals(false, engine.isValid(1));
	}
	public void testCase6()
	{
		String []xpaths=new String[1];
		xpaths[0]="/web-a pp[@name=\"vamsee\"]";
		
		XPathEngine engine=new XPathEngine(xpaths);
		assertEquals(false, engine.isValid(1));
	}
	
	public void testCase7()
	{
		String []xpaths=new String[3];
		xpaths[0]="/web-app[@name=\"vamsee\"]/working";
		xpaths[1]="/web-app[@name=\"vamsee\"]/,great";
		xpaths[2]="/web-app[@name=\"vamsee\"]/8gmail";
		
		XPathEngine engine=new XPathEngine(xpaths);
		for(int i=1;i<=3;i++)
		{
		boolean state= engine.isValid(i);
		if(i==1)
		{
			assertEquals(true, state); 
		}
		else
		{
			assertEquals(false, state);
		}
		}
		
	}
	
	public void testCase8()
	{
		
		    String []xpaths=new String[1];
		    xpaths[0]="/breakfast_menu/food/name";
		
			String url="http://www.w3schools.com/xml/simple.xml";
			HttpClient client=new HttpClient(url);
			ByteArrayOutputStream out=client.fetchData();
			XPathEngine engine=new XPathEngine(xpaths);
			Document root=engine.createDOM(out, client);
			boolean[] stats=engine.evaluate(root);
			assertEquals(true, (out!=null)&& client.ConType.equalsIgnoreCase("XML") && stats[0]);

	}
	
	public void testCase9()
	{
		String []xpaths=new String[3];
		xpaths[0]="/breakfast_menu/food[calories[text()=\"650\"]]";
		xpaths[1]="/breakfast_menu/food[description[contains(text(),\"eggs\")]]";
		xpaths[2]="/breakfast&menu";
		
			String url="http://www.w3schools.com/xml/simple.xml";
			HttpClient client=new HttpClient(url);
			ByteArrayOutputStream out=client.fetchData();
			XPathEngine engine=new XPathEngine(xpaths);
			Document root=engine.createDOM(out, client);
			boolean[] stats=engine.evaluate(root);
			
		
			
			assertEquals(true, (out!=null)&& client.ConType.equalsIgnoreCase("XML") && stats[0] && stats[1] && !stats[2]);
		
			
	}
	
	public void testCase10()
	{
		

	    String []xpaths=new String[1];
	    xpaths[0]="/note/to";
	
		String url="http://www.w3schools.com/xml/note.xml";
		HttpClient client=new HttpClient(url);
		ByteArrayOutputStream out=client.fetchData();
		XPathEngine engine=new XPathEngine(xpaths);
		Document root=engine.createDOM(out, client);
		boolean[] stats=engine.evaluate(root);
		assertEquals(true, (out!=null)&& client.ConType.equalsIgnoreCase("XML") && stats[0]);

		/*
		    String []xpaths=new String[1];
		    xpaths[0]="/head/body";
		
			String url="http://www.cis.upenn.edu/";
			HttpClient client=new HttpClient(url);
			ByteArrayOutputStream out=client.fetchData();
			XPathEngine engine=new XPathEngine(xpaths);
			Document root=engine.createDOM(out, client);
			boolean[] stats=engine.evaluate(root);
			assertEquals(true, (out!=null)&& client.ConType.equalsIgnoreCase("HTML") && stats[0]);
*/
	}
	
}
