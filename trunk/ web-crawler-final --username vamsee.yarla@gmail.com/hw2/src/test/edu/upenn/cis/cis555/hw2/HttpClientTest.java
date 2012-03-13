/**
 * 
 */
package test.edu.upenn.cis.cis555.hw2;

import java.io.ByteArrayOutputStream;

import edu.upenn.cis.cis555.HttpClient;
import junit.framework.TestCase;

/**
 * @author VamseeKYarlagadda
 *
 */
public class HttpClientTest extends TestCase {

	
	public void testCase1()
	{
		String url="http://en.wikipedia.org/wiki/XML";
		HttpClient client=new HttpClient(url);
		ByteArrayOutputStream out=client.fetchData();
		assertEquals(true, (out!=null)&& client.ConType.equalsIgnoreCase("HTML"));
	}
	
	public void testCase2()
	{
		String url="http://www.w3schools.com/xml/simple.xml";
		HttpClient client=new HttpClient(url);
		ByteArrayOutputStream out=client.fetchData();
		assertEquals(true, (out!=null)&& client.ConType.equalsIgnoreCase("XML"));
	}
		
}
