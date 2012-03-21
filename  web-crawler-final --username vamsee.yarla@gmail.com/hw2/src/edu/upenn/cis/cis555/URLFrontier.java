/**
 * 
 */
package edu.upenn.cis.cis555;

import java.util.ArrayList;

/**
 * @author VamseeKYarlagadda
 *
 */
public class URLFrontier {
public ArrayList<String> urls;

public URLFrontier()
{
	urls=new ArrayList<String>();
}

public String get()
{
	if(urls.size()>0)
	{
	return urls.remove(0);
	}
	else
		return null;
}


public boolean put(String e)
{
	try{
	urls.add(e);
	return true;
	}
	catch(Exception ex)
	{
		return false;
	}
	
}

public String toString()
{
	return urls.toString();
}
}
