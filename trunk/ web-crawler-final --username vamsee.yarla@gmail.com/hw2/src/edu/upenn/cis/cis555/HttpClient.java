/**
 * 
 */
package edu.upenn.cis.cis555;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

/**
 * @author VamseeKYarlagadda
 *
 */
public class HttpClient {
	String URL;
	ByteArrayOutputStream outBytes;
	public String Hostname;
	public String Link;
	public String ConType=null;
	public String ConLength=null;
	//public String useragent="WebCrawler";
	public String useragent="cis455crawler1";
	Hashtable<String,ArrayList<String>> robots=null;
	int MaxSize=-1;
	/*
	 * Constructor of HttpClient that takes a string which contains URL and saves it
	 * in one of the global variables.
	 */
public HttpClient(String url)
{
	URL=url;
}

public HttpClient(String URL,int MaxSize)
{
	this.URL=URL;
	this.MaxSize=MaxSize;
}

/*
 * Function which uses the URL passed to the constructor and check whether it is
 * 
 * 1-> A local system file
 * 2-> Remote file
 * 
 *  According to the type, it fetches the file from he system appropriately. 
	For all the local system files, it directly reads from the system and return the bytes otherwise null or 404 exception.
	For all remote clients, it frames the header which accepts html and xml 
	and looks for response and checks the content type field accordingly 
	and read all the data and store it In bytes and return it to the calling function.

 */

public ByteArrayOutputStream fetchData()
{
	System.out.println("URL in FETCHDATA:  "+URL);
	if(URL=="" || URL==null)
	{
		outBytes=null;
	}
	else if(URL.indexOf("http://")==0 || URL.indexOf("HTTP://")==0)
	{
		String address;
		String request;
		String port="80";
		
			//Considering it as Local Server File
			//COsidering it as Global file
		
			if(URL.indexOf("/",7)==-1)
			{
				return null;
			}
			else
			{
				address=URL.substring(0,URL.indexOf("/",7));
				if(address.indexOf(":",7)!=-1)
				{
					port=address.substring(address.indexOf(":",7)+1,address.length());
					address=address.substring(0,address.indexOf(":",7));
				}
				request=URL.substring(URL.indexOf("/",7),URL.length());
			}
			address=address.trim();
			request=request.trim();
			if(address =="" || request=="")
			{
				return null;
			}
			else
			{
				//TIME TO REQUEST THE SERVER FOR DATA
				//
				// Create a connection to the server socket on the server application
				//
				Socket socket=null;
				OutputStream  out=null;
				BufferedReader br = null;
				
				try{
					address=address.substring(7,address.length());
				InetAddress host = InetAddress.getByName(address);
				socket = new Socket (host.getHostAddress(), Integer.parseInt(port));
				
				out=(socket.getOutputStream());
				br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				
				
				/*
				 * Check for the status of the Robots.txt file
				 */
							
				out.write(("GET "+"/robots.txt"+" HTTP/1.1\n").getBytes());
				out.write(("User-Agent: "+useragent+"\n").getBytes());
				out.write(("Host: "+address+"\n\n").getBytes());
				
				out.flush();	
				
				String firstHeadRobot=br.readLine();
				if(firstHeadRobot.indexOf("404")!=-1 || firstHeadRobot.indexOf("500")!=-1)
				{
					System.out.println("NO ROBOTS");
					//NO ROBOTS.TXT AT SERVER
					
				}
				else
				{
					robots=new Hashtable<String,ArrayList<String>>();
					
					while(!br.readLine().equalsIgnoreCase(""));
					
				String presentLine=null;
				String UserAgent=null;
				while((presentLine=br.readLine())!=null)
				{
					presentLine=presentLine.trim();
					
					if(presentLine.length()>11 && presentLine.substring(0,10).equalsIgnoreCase("User-agent"))
					{
						try{
						String[] split=presentLine.split(":");
							if(split.length<2)
							{
							System.out.println("FORMAT OF ROBOTS NOT CORRECT");
							robots=null;
							break;
							}
						split[1]=split[1].trim();
						UserAgent=split[1];
						System.out.println("User-Agent: #"+split[1]);
						if(!robots.containsKey(split[1]))
						{
						ArrayList<String> data=new ArrayList<String>();
						robots.put(split[1],data);
						}
						
						}
						catch(Exception e)
						{
							System.out.println("FORMAT OF ROBOTS NOT CORRECT");
							robots=null;
							break;
						}
					 }
					
					if(presentLine.length()>8 && presentLine.substring(0,8).equalsIgnoreCase("Disallow"))
					{
						try{
						String[] split=presentLine.split(":");
							if(split.length<2)
							{
								/*
								 * ALL ACCESS TO FILES
								 */
								
								split=new String[2];
								split[0]="Disallow";
								split[1]="ALL";
								
								/*
							System.out.println("FORMAT OF ROBOTS NOT CORRECT");
							robots=null;
							break;
							*/
							}
						split[1]=split[1].trim();
						if(split[1].equalsIgnoreCase(""))
						{
							split[1]="ALL";
						}
						if(UserAgent==null)
						{
							System.out.println("FORMAT OF ROBOTS NOT CORRECT");
							robots=null;
							break;
						}
						
						if(split[1].indexOf("#")!=-1)
						{
							split[1]=split[1].substring(0,split[1].indexOf("#")).trim();
						}
						System.out.println("Disallow: #"+split[1]);
						ArrayList<String> set=robots.get(UserAgent);
						set.add(split[1]);
						robots.put(UserAgent, set);
						
						}
						catch(Exception e)
						{
							System.out.println("FORMAT OF ROBOTS NOT CORRECT");
							robots=null;
							break;
						}
					 }
					
					//TODO: Write code for Crawl-delay
					
				}
					
				}
				br.close();
				out.close();
				socket.close();
				
				//CHECK WHETHER THE REQUEST IS IN COMPLAINCE WITH ROBOTS.TXT
				
				if(robots!=null)
				{
					ArrayList<String> list=robots.get(useragent);
					if(list==null)
					{
						list=robots.get("*");						
					}
					
					if(list!=null)
					{
						
						if(!list.contains("ALL"))
						{
						for(int i=0;i<list.size();i++)
						{
							if(request.indexOf(list.get(i))==0)
							{
								return null;
							}
						}
						}
					}
				}
					System.out.println("CLEARED ROBOTS");
					
				/*
				 * SEND HEAD REQ's TO CHECK THE INCOMING FORMAT!!
				 */
				host = InetAddress.getByName(address);
				socket = new Socket (host.getHostAddress(), Integer.parseInt(port));
				
				out=(socket.getOutputStream());
				br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				
				out.write(("HEAD "+request+" HTTP/1.1\n").getBytes());
				out.write(("User-Agent: "+useragent+"\n").getBytes());
				out.write(("Host: "+address+"\n\n").getBytes());
			
				
				
				String firstHeadHEAD=br.readLine();
				System.out.println(firstHeadHEAD);
				if(firstHeadHEAD.indexOf("404")!=-1 || firstHeadHEAD.indexOf("500")!=-1 || firstHeadHEAD.indexOf("301")!=-1 || firstHeadHEAD.indexOf("403")!=-1)
				{
					System.out.println("TRACK2");
					////System.out.println("SERVER ERROR AT REMOTE LOCATION");
					/*
					outBytes=new ByteArrayOutputStream();
					outBytes.write("404".getBytes());
					return outBytes;
					*/
					return null;
				}
				else
				{
					System.out.println("TRACK3");
					//String contentLength=null;
					String contentType=null;
					String contentLength=null;
					String temp;
					while((temp=br.readLine())!=null)
					{
						if(temp.indexOf("Content-Type:")!=-1)
						{
							contentType=temp;
						}
						else if(temp.indexOf("Content-Length:")!=-1)
						{
							contentLength=temp;
						}
						
					}
					
					if(contentType!=null && contentLength!=null)
					{
					    if(contentType.indexOf("Content-Type:")!=-1)
					    {
					////System.out.println("ENTERED CONTENT-TYPE");
					System.out.println(URL+"     "+contentType);
					String type=contentType.substring(contentType.indexOf(":")+1,contentType.length()).trim();
					
					if(type.indexOf("xml")!=-1 || type.indexOf("XML")!=-1)
					{
						System.out.println("VA0");
						ConType="XML";
						
					}
					/*
					else if(type.indexOf("xhtml")!=-1 || type.indexOf("XHTML")!=-1)
					{
						ConType="XML";
					}
					*/
					else if(type.indexOf("html")!=-1 || type.indexOf("HTML")!=-1)
					{
						System.out.println("VA1");
						ConType="HTML";
						
					}
					else
					{
						/*
						 * Illegal MIME Type Document
						 * It is other than XML and HTML
						 */
						return null;
					}
					
					    }
					    if(contentLength.indexOf("Content-Length:")!=-1)
					    {
					    	String length=contentLength.substring(contentLength.indexOf(":")+1,contentLength.length()).trim();
							ConLength=length;
							
							if(MaxSize!=-1)
							{
							  
								if(MaxSize < Integer.parseInt(ConLength))
								{					
									/*
									 * Huge File Size
									 * 
									 */
									return null;
								}
								System.out.println("CONTENT LENGTH: "+ConLength);
								System.out.println("PASSED CONTENT LENGTH");
							}
					    }
					}
					else
					{
						
							/*
							 * There is no content type or content Length attribute in response headers.
							 * Illegal headers!!!
							 */
							return null;
						
					}
					
				
				}
	
				br.close();
				out.close();
				socket.close();
				
				
				//
				// Send a message to the client application
				//
				host = InetAddress.getByName(address);
				socket = new Socket (host.getHostAddress(), Integer.parseInt(port));
				
				out=(socket.getOutputStream());
				br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
			
				
				
				out.write(("GET "+request+" HTTP/1.1\n").getBytes());
				out.write(("Host: "+address+"\n").getBytes());
				out.write(("User-Agent: "+useragent+"\n\n").getBytes());
				
				
			//	out.write(("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n").getBytes());
			//	out.write(("Accept-Encoding: gzip, deflate\n").getBytes());
			//	out.write(("Accept-Language: en-us\n\n").getBytes());
				
				
				Hostname=address;
				
				if(request.equalsIgnoreCase("/"))
				{
					Link="/";
				}
				else if(request.charAt(request.length()-1)=='/')
				{
					request=request.substring(0,request.length()-1);
				}
				Link=request;
				System.out.println("TRACK1");
				
				
			
									
				String firstHead=br.readLine();
			
					/*
					while((contentLength=br.readLine()).indexOf("Content-Length")==-1);
				
					Length=Integer.parseInt((contentLength.substring(contentLength.indexOf(":")+1,contentLength.length()).trim()));
						*/
					
					/*
					 char[] tempc=new char[Length];
					 br.read(tempc, 0, Length);
					 StringBuilder sb=new StringBuilder();
       			  	 sb.append(tempc);
					  outBytes.write(sb.toString().getBytes());
					 */
					int x;
				
					while(!br.readLine().equalsIgnoreCase(""));
					
					/*
					try{
					do
					{
						int a=br.read();
						int b=br.read();
					   if((a==13 && b==13)||(a==10 && b==10))
					   {
						   break;
					   }
					   else if((a==13 && b==10) && (x=br.read())==13 && (x=br.read())==10)
					   {
						   break;
					   }
					}while(true);
					//while(!((x=br.read())==13 && (x=br.read())==10 && (x=br.read())==13 && (x=br.read())==10));
					}
					catch(Exception e)
					{
						System.out.println("END OF STREAM REACHED");
					}
					*/
					
					
					
					System.out.println("VA3");
					outBytes=new ByteArrayOutputStream();
					while((x=br.read())!=-1)
					{
						outBytes.write(x);
					}
					
					System.out.println("krishna");
					/*
					System.out.println("INDEX:   "+outBytes.toString().indexOf("<!DOCTYPE"));
					if(outBytes.toString().indexOf("<!DOCTYPE")<15)
					{
						ConType="XML";
					}
					*/
					
       			  
					
					
				
				
				
				
				
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return null;		
				}
				finally
				{
					try{
					 br.close();
					 out.close();
       			  	 socket.close(); 
					}
					catch(Exception e)
					{
						
					}
				}
				
				
			}
			
	
			
			return outBytes;
	}
	else
	{
		//Considering it as Local FileSystem File
		try{
			FileReader fr=new FileReader(URL);
			outBytes=new ByteArrayOutputStream();
			char []buf=new char[2048];
			int ret;
			while((ret=fr.read(buf, 0, 2048))!=-1)
			{
				outBytes.write(String.valueOf(buf).getBytes(), 0, ret);
			}
		}
		catch(Exception e)
		{
			//System.out.println("Error in reading file");
			e.printStackTrace();
			outBytes=new ByteArrayOutputStream();
			try {
				outBytes.write("404".getBytes());
			} catch (IOException e1) {
				//  Auto-generated catch block
				e1.printStackTrace();
			}
			return outBytes;
			//return null;
		}
	}
	
	
	
	return outBytes;
}
}
