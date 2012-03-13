package test.edu.upenn.cis.cis555.hw2;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(XPathEngineTest.class);
		suite.addTestSuite(HttpClientTest.class);
		
		//$JUnit-END$
		return suite;
	}

}
