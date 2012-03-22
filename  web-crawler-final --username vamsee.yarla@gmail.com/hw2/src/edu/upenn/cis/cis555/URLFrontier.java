/**
 * 
 */
package edu.upenn.cis.cis555;

import java.util.ArrayList;

/**
 * @author VamseeKYarlagadda
 *
 */
/*
 * URL Frontier will store all the URLs to be parsed by the crawler and retrives them one by one and gives it to the crawler.
 * It also performs the check whether the URl has already been parsed or not.
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
