/**
 * 
 */
package edu.upenn.cis.cis555;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import com.sleepycat.persist.EntityCursor;

/**
 * @author cis555
 *
 */
public class XPathClientServlet extends HttpServlet{

	HttpServletRequest request;
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 	
	 	This function is the first one to be called when the server is activated with 
	 	the XPath Servlet. It just returns the fields necessary for html viewing.
		It also takes all the XPath inputs and URL from where the file has to be fetched 
		and calls the POST sequence of the same servlet for execution.

	 *
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		this.request=request;
		System.out.println(request.getParameterMap().size());
		System.out.println(request.getParameterMap().toString());
		
		if(request.getParameter("status")==null)
		{
		PrintWriter out=response.getWriter();
	
		  showDefaultLogin(out,0);
		}
	
		else if(request.getParameter("status").equalsIgnoreCase("NEWUSER"))
		{
			PrintWriter out=response.getWriter();
			showNewUserSignUp(out,0);
		}
		
		else if(request.getParameter("status").equalsIgnoreCase("LOGOUT"))
		{
			PrintWriter out=response.getWriter();
			HttpSession session = request.getSession();
			session.invalidate();
			showDefaultLogin(out,2);
		}
		else if(request.getParameter("status").equalsIgnoreCase("DELETECHANNEL"))
		{
			String ID=request.getParameter("id");
			PrintWriter out=response.getWriter();
			HttpSession session = request.getSession();
			UserData data=(UserData) session.getAttribute("user");
			
			
			
			ServletConfig config = getServletConfig();
			ServletContext context = config.getServletContext();
			DB db= DB.getInstance(context.getInitParameter("BDBstore"));
			
			System.out.println(data.Channels);
			
		
			db.deleteChannel(ID);
			
			data.Channels.remove(ID);
			//	db.UserIndex.put(data);
			db.updateData(data);
				
			//db.ChannelIndex.delete(ID);
			session.setAttribute("user", data);
			successLogin(out, data);
			
		}
		else if(request.getParameter("status").equalsIgnoreCase("OPENCHANNEL"))
		{
			//TODO: display of the channel
			HashMap<String,String> month=new HashMap<String, String>();
			month.put("Jan", "01");
			month.put("Feb", "02");
			month.put("Mar", "03");
			month.put("Apr", "04");
			month.put("May", "05");
			month.put("Jun", "06");
			month.put("Jul", "07");
			month.put("Aug", "08");
			month.put("Sep", "09");
			month.put("Oct", "10");
			month.put("Nov", "11");
			month.put("Dec", "12");
			
			
			String ID=request.getParameter("id");
			
			DB db=DB.getInstance("JEBD");
			ChannelData data=db.getChannelData(ID);
			PrintWriter out=response.getWriter();
			
			response.setContentType("text/xml");
			
			out.println("<?xml version=\"1.0\" encoding=\"UTF-16\"?>");
			out.println("<?xml-stylesheet type=\"text/xsl\" href=\""+data.URL+"\"?>");
			
			
			out.println("<documentcollection>");
			
			ArrayList<String> result=new ArrayList<String>();
			
			for(String xpath: data.XPaths.keySet())
			{
				for(int i=0;i<data.XPaths.get(xpath).size();i++)
				{
					String time=db.getURLTimestamp(data.XPaths.get(xpath).get(i));
					time=time.substring(0,time.length()-4);
					time=time.substring(5);
					System.out.println(time);
					String[] times=time.split(" ");
					
					DateFormat sysformatter=new SimpleDateFormat("HH:mm:ss");
					
					Date userdate=null;
					try{
						
					    userdate=sysformatter.parse(times[3]);
					    time=null;
					    time=times[2]+"-"+month.get(times[1])+"-"+times[0]+"T"+userdate.getHours()+":"+userdate.getMinutes()+":"+userdate.getSeconds();
					}
					catch(Exception e)
					{
						System.out.println("EXCEPTION IN CHANGING DATE");
					}
					//CrawlIndex.get(data.XPaths.get(xpath).get(i));
					
					if(!result.contains("crawled=\""+time+"\" location=\""+data.XPaths.get(xpath).get(i)+"\""))
					{
					result.add("crawled=\""+time+"\" location=\""+data.XPaths.get(xpath).get(i)+"\"");
					}
				}
			}
			
			for(int i=0;i<result.size();i++)
			{
				out.println("<document "+result.get(i)+">");
				//out.println();
				out.println("</document>");
			}
			
			out.println("</documentcollection>");
			
		//	out.println("			<student_list> 			       <student>			           <name>George Washington</name>			           <major>Politics</major>			           <phone>312-123-4567</phone>			           <email>gw@example.edu</email>			       </student>			       <student>			          <name>Janet Jones</name>			          <major>Undeclared</major>			          <phone>311-122-2233</phone>			          <email>janetj@example.edu</email>			      </student>			      <student>			          <name>Joe Taylor</name>			          <major>Engineering</major>			          <phone>211-111-2333</phone>			          <email>joe@example.edu</email>  </student>			  </student_list>");			
			/*
			out.println("Channel Dispaly");
			out.println("</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			out.println("<form action=\"http://localhost:1234/demo\" method=\"POST\" >");
			out.println("<table>" +
					     "<tr><td>");
			out.println("<b>Enter XPath</b></td>");
			out.println("<td><input type=\"text\" name=\"xpath\" size=\"30\"></td>");
			out.println("<td><input type=\"text\" name=\"xpath\" size=\"30\"></td>");
			out.println("<td><input type=\"text\" name=\"xpath\" size=\"30\"></td>");
			out.println("</tr><tr>");
			out.println("<td><b>Enter URL for HTML/XML</b></td>");
			out.println("<td><input type=\"text\" name=\"url\" size=\"30\"></td>");
			out.println("</tr><tr>");
			out.println("<td><input type=\"Submit\" name=\"Submit\" ></td>");
			out.println("</tr></table>");
			out.println("</br></br>");
			out.println("*** The URL can be File System Address or any Remote Address; All Remote Addresses are supposed to start with http://");
			out.println("</br></br>");
			out.println("Created By:-");
			out.println("</br>");
			out.println("Vamsee K Yarlagadda");
			out.println("</br>");
			out.println("PennKey: vamsee>");
			out.println("</form>");
			out.println("</BODY>");
			out.println("</documentcollection>");
			*/
		}
		else if(request.getParameter("status").equalsIgnoreCase("STARTCRAWL"))
		{
			boolean status=true;
			String[] args;
			 if(request.getParameter("URL2CRAWL").trim().equalsIgnoreCase("") || request.getParameter("SIZE2CRAWL").trim().equalsIgnoreCase(""))
			 {
				
				 System.out.println("ERROR IN GETTING DATA FROM USER");
				 openAdminControl(response.getWriter(), 1);
				 status=false;
			 }
			if(request.getParameter("FILES2CRAWL")==null || request.getParameter("FILES2CRAWL").trim().equalsIgnoreCase(""))
			{
				args=new String[3];
			}
			else
			{
				args=new String[4];
			}
			ServletConfig config = getServletConfig();
			ServletContext context = config.getServletContext();
		
			args[0]=request.getParameter("URL2CRAWL").trim();
			args[1]=(context.getInitParameter("BDBstore"));
			
			try{
				Double d=Double.parseDouble(request.getParameter("SIZE2CRAWL"));
				args[2]=request.getParameter("SIZE2CRAWL").trim();
				
				if(args.length==4)
				{	
					int e=Integer.parseInt(request.getParameter("FILES2CRAWL"));
					args[3]=request.getParameter("FILES2CRAWL").trim();	
				}
				
			   }
			catch(Exception e)
			{
				 status=false;
				 System.out.println("ERROR IN GETTING DATA FROM USER");
				 openAdminControl(response.getWriter(), 2);
			}
			
			
		
			if(status)
			{
			InvokeCrawler crawler=new InvokeCrawler();
			crawler.args=args;
			HttpSession session = request.getSession();
			session.setAttribute("crawl", crawler);
			crawler.start();
			
			openAdminControl(response.getWriter(), 5);
			}
		}
		
		else if(request.getParameter("status").equalsIgnoreCase("STOPCRAWL"))
		{
			HttpSession session = request.getSession();
			InvokeCrawler crawler = (InvokeCrawler) session.getAttribute("crawl");
			XPathCrawler data=crawler.crawlObj;
			if(crawler.running)
			{
				data.crawling.terminate=true;
			}
			
			
			System.out.println("DATAVAMSEE:   "+data.crawling.XPaths);
			session.invalidate();
			
			ServletConfig config = getServletConfig();
			ServletContext context = config.getServletContext();
			
			
			displayCrawlResult(response.getWriter(), data, context.getInitParameter("BDBstore"));
		}
		
		/*
		PrintWriter out=response.getWriter();
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>");
		out.println("XPathServlet");
		out.println("</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<form action=\"http://localhost:1234/demo\" method=\"POST\" >");
		out.println("<table>" +
				     "<tr><td>");
		out.println("<b>Enter XPath</b></td>");
		out.println("<td><input type=\"text\" name=\"xpath\" size=\"30\"></td>");
		out.println("<td><input type=\"text\" name=\"xpath\" size=\"30\"></td>");
		out.println("<td><input type=\"text\" name=\"xpath\" size=\"30\"></td>");
		out.println("</tr><tr>");
		out.println("<td><b>Enter URL for HTML/XML</b></td>");
		out.println("<td><input type=\"text\" name=\"url\" size=\"30\"></td>");
		out.println("</tr><tr>");
		out.println("<td><input type=\"Submit\" name=\"Submit\" ></td>");
		out.println("</tr></table>");
		out.println("</br></br>");
		out.println("*** The URL can be File System Address or any Remote Address; All Remote Addresses are supposed to start with http://");
		out.println("</br></br>");
		out.println("Created By:-");
		out.println("</br>");
		out.println("Vamsee K Yarlagadda");
		out.println("</br>");
		out.println("PennKey: vamsee>");
		out.println("</form>");
		out.println("</BODY>");
		out.println("</HTML>");
		
		*/
		
}
	
	
	public void showDefaultLogin(PrintWriter out, int status)
	{
		
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>");
		out.println("Login Page");
		out.println("</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		//out.println("<form action=\"http://localhost:1234/login\" method=\"POST\" >");
		out.println("<form action=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"\" method=\"POST\" >");
		
		if(status==1)
		{
		out.println("<h3>Invalid Username or Password.. Try Again<h3>");
		}
		else if(status==2)
		{
		out.println("<h3>Logout Successful.<h3>");
		}
		else if(status==3)
		{
		out.println("<h3>Account created Successfully.<h3>");
		}
		out.println("<table>" +
				     "<tr><td>");
		out.println("<b>Username:</b></td>");
		out.println("<td><input type=\"text\" name=\"username\" size=\"30\"></td>");
		
		out.println("</tr><tr>");
		out.println("<td><b>Password:</b></td>");
		out.println("<td><input type=\"password\" name=\"password\" size=\"30\"></td>");
		out.println("</tr><tr>");
		
		//out.println("<td><b>Re-enter Password:</b></td>");
		out.println("<td><input type=\"hidden\" name=\"status\" value=\"LOGIN\" ></td>");
		out.println("</tr><tr>");
		
		
		out.println("<td><input type=\"submit\" value=\"Submit\" name=\"Submit\"></td>");
		out.println("</tr><tr>");
	
		//out.println("<td> <a href=\"http://localhost:1234/login?status=NEWUSER\"> New Users! Signup.. </a></td>");
		
		out.println("<td> <a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"?status=NEWUSER\"> New Users! Signup.. </a></td>");
		
		
		out.println("</tr></table>");
		
	
		
		out.println("</form>");
		out.println("</BODY>");
		out.println("</HTML>");
		
	}
	
	public void showNewUserSignUp(PrintWriter out, int status)
	{
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>");
		out.println("SignUp Page");
		out.println("</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<form action=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"\" method=\"POST\" >");
		
		//out.println("<td> <a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"?status=NEWUSER\"> New Users! Signup.. </a></td>");
		
		if(status==1)
		{
		out.println("<h3>The entered username is too short; minimum 3 characters.. Try again<h3>");
		}
		else if(status==2)
		{
		out.println("<h3>The entered password is too short; minimum 3 characters.. Try again<h3>");
		}
		else if(status==3)
		{
		out.println("<h3>Passwords do not match!<h3>");
		}
		else if(status==4)
		{
		out.println("<h3>UserID already exits..Enter a different one<h3>");
		}
		else if(status==5)
		{
		out.println("<h3>Name entered is incorrect; either too small or bad characters<h3>");
		}
		
		out.println("<table>" +
				     "<tr>");
		
		out.println("<td><b>Enter Name:</b></td>");
		out.println("<td><input type=\"text\" name=\"name\" size=\"30\"></td>");
		
		out.println("</tr><tr>");
		
		out.println("<td><b>Enter Username:</b></td>");
		out.println("<td><input type=\"text\" name=\"username\" size=\"30\"></td>");
		
		out.println("</tr><tr>");
		out.println("<td><b>Enter Password:</b></td>");
		out.println("<td><input type=\"password\" name=\"password\" size=\"30\"></td>");
		out.println("</tr><tr>");
		
		out.println("<td><b>Re-enter Password:</b></td>");
		out.println("<td><input type=\"password\" name=\"password1\" size=\"30\"></td>");
		out.println("</tr><tr>");
		
		//out.println("<td><b>Re-enter Password:</b></td>");
		out.println("<td><input type=\"hidden\" name=\"status\" value=\"NEWCRED\" ></td>");
		out.println("</tr><tr>");
		
		out.println("<td><input type=\"submit\" value=\"Submit\" name=\"Submit\"></td>");
		out.println("</tr><tr>");
	
		out.println("<td> <a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"\"> Back! </a></td>");
		
		out.println("</tr></table>");
		
		out.println("</form>");
		out.println("</BODY>");
		out.println("</HTML>");
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 
	 *This function is called whenever the post method is checked in the form. 
	 *It takes the inputs from the user and calls appropriate functions for XPATH 
	 *Validation and also creating the DOM representations and calling the evaluate 
	 *function to verify whether the XPaths conform to the file provided as the URL.
	 *
	 */
	
	
	public void successLogin(PrintWriter out, UserData data)
	{
	
		
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>");
		out.println("Client Page");
		out.println("</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<form action=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"\" method=\"POST\" >");
		
		
		out.println("<h3>Welcome "+data.Name+"<h3>");
		out.println("<a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"?status=LOGOUT\"> Logout </a>");
		
		
		out.println("<table>");
		out.println("</br></br>");
		out.println("<tr>Create a New Channel</tr>");
		
		
		out.println("<tr><td>");
		out.println("<b>Enter Channel Name:</b></td>");
		out.println("<td><input type=\"text\" name=\"channel_name\" size=\"30\"></td>");
		
		out.println("</tr><tr>");
		out.println("<td><b>Enter XPaths to follow:</b></td>");
		out.println("<td><input type=\"text\" name=\"xpaths\" size=\"30\"></td>");
		out.println("<td><input type=\"text\" name=\"xpaths\" size=\"30\"></td>");
		out.println("<td><input type=\"text\" name=\"xpaths\" size=\"30\"></td>");
		out.println("</tr><tr>");
		
		out.println("<td><b>Enter XSL Path:</b></td>");
		out.println("<td><input type=\"text\" name=\"xsl\" size=\"30\"></td>");
		out.println("</tr><tr>");
	
		
		//out.println("<td><b>Re-enter Password:</b></td>");
		out.println("<td><input type=\"hidden\" name=\"status\" value=\"CHANNEL\" ></td>");
		out.println("</tr><tr>");
		
		
		out.println("<td><input type=\"submit\" value=\"Create Channel\" name=\"Create\"></td>");
		out.println("</tr>");
		out.println("<tr> <td>List of channels in System</td></tr>");
		
		ServletConfig config = getServletConfig();
		ServletContext context = config.getServletContext();
		DB db= DB.getInstance(context.getInitParameter("BDBstore"));
		
		EntityCursor<ChannelData> channel_data=db.ChannelIndex.entities();
		for(ChannelData temp: channel_data)
		{
			if(data.Channels.contains(temp.ID))
			{
				out.println("<tr>  <td><a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"?status=OPENCHANNEL&id="+temp.ID+"\"> Channel: "+temp.Name+" </a> </td>");
				
				out.println("<td><a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"?status=DELETECHANNEL&id="+temp.ID+"\"> Delete </a> </td>");
				
				out.println("</tr>");
			}
			else
			{
				out.println("<tr>  <td><a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"?status=OPENCHANNEL&id="+temp.ID+"\"> Channel: "+temp.Name+" </a> </td>");
			}
		}
			
			
		out.println("</table>");
		
		out.println("</form>");
		out.println("</BODY>");
		out.println("</HTML>");
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		this.request=request;
		
		if(request.getParameter("status").equalsIgnoreCase("NEWCRED"))
		{
			String Name=request.getParameter("name");
			String Username=request.getParameter("username");
			String Password=request.getParameter("password");
			String Password1=request.getParameter("password1");
			
			char[] temp=Name.toCharArray();
			for(int i=0;i<temp.length;i++)
			{
				if((temp[i]<65 && temp[i]>122) || (temp[i]>90 && temp[i]<97))
				{
					if(temp[i]!=32)
					{
						PrintWriter out=response.getWriter();
						showNewUserSignUp(out, 5);
						return;
					}
				}
			}
			
			if(Name.length()<2)
			{
				PrintWriter out=response.getWriter();
				showNewUserSignUp(out, 5);
				return;
			}	
			if(Username.length()<3)
			{
				PrintWriter out=response.getWriter();
				showNewUserSignUp(out, 1);
				return;
			}
			if(Password.length()<3 || Password1.length()<3)
			{
				PrintWriter out=response.getWriter();
				showNewUserSignUp(out, 2);
				return;
			}
			if(!Password.equals(Password1))
			{
				PrintWriter out=response.getWriter();
				showNewUserSignUp(out, 3);
				return;
			}
			
			ServletConfig config = getServletConfig();
			ServletContext context = config.getServletContext();
			DB db= DB.getInstance(context.getInitParameter("BDBstore"));
			
			if(db.checkUserExists(Username))
			{
				PrintWriter out=response.getWriter();
				showNewUserSignUp(out, 4);
				
				return;
			}
		      db.addUser(Username, Password, Name);
		      
		      
		      PrintWriter out=response.getWriter();
		      showDefaultLogin(out, 3);
		}
		
		else if(request.getParameter("status").equalsIgnoreCase("LOGIN"))
		{
			
			
			String Username=request.getParameter("username");
			String Password=request.getParameter("password");
			
			
			if(Username.equalsIgnoreCase("admin")&& Password.equalsIgnoreCase("adminadmin"))
			{
				System.out.println("ServerPort: "+request.getServerPort());
				System.out.println("ServerName: "+request.getServerName());
				System.out.println("Servlet: "+request.getRequestURI());
				System.out.println("http://localhost:"+request.getServerPort()+request.getRequestURI());
				
				PrintWriter out=response.getWriter();
				openAdminControl(out,0);
			}
			else
			{
				System.out.println("ENETERED NONDATA");
			ServletConfig config = getServletConfig();
			ServletContext context = config.getServletContext();
			DB db= DB.getInstance(context.getInitParameter("BDBstore"));
			
			UserData obj;
			if((obj=db.login(Username,Password))==null)
			{
				PrintWriter out=response.getWriter();
				showDefaultLogin(out, 1);
				
				return;
			}
			
			System.out.println("SUCCESSFUL USER");
		     
		       
		   	HttpSession session = request.getSession();
			session.setAttribute("user", obj);
		       PrintWriter out=response.getWriter();
		       successLogin(out,obj);
			}
			
		}
		
		
		else if(request.getParameter("status").equalsIgnoreCase("CHANNEL"))
		{
		
			
			String Name=request.getParameter("channel_name");
			String[] XPaths=request.getParameterValues("xpaths");
			int count=0;
			for(int i=0;i<XPaths.length;i++)
			{
				if(!XPaths[i].trim().equalsIgnoreCase(""))
				{
					count++;
				}
			}
			String[] temp=new String[count];
			System.out.println("COUNT:     "+count);
			count=0;
		
			for(int i=0;i<XPaths.length;i++)
			{
				if(!XPaths[i].trim().equalsIgnoreCase(""))
				{
					temp[count]=XPaths[i].trim();
					count++;
				}
			}
			
			String XSL=request.getParameter("xsl");
			
			if(Name.equalsIgnoreCase(""))
			{
				System.out.println("Name cannot be NULL");
			}
			else
			{
				ServletConfig config = getServletConfig();
				ServletContext context = config.getServletContext();
				DB db= DB.getInstance(context.getInitParameter("BDBstore"));
				
				String ID=db.nextChannelID();
				
				ChannelData data=new ChannelData(ID,Name,temp,XSL);
			/*
				data.ID=ID;
				data.Name=Name;
				if(!XPath1.equalsIgnoreCase("") && !(data.XPaths.contains(XPath1)))
					{
						data.XPaths.add(XPath1);
					}
				if(!XPath2.equalsIgnoreCase("") && !(data.XPaths.contains(XPath2)))
					{
					data.XPaths.add(XPath2);
					}
				if(!XPath3.equalsIgnoreCase("") && !(data.XPaths.contains(XPath3)))
					{
					data.XPaths.add(XPath3);
					}
				data.URL=XSL;
				*/
				
				if(!db.addChannel(data))
				{
					System.out.println("CHANNEL FAILURE");
				}
				else
				{
					
					HttpSession session = request.getSession();
					System.out.println(session.getId());
					UserData data_User= (UserData) session.getAttribute("user");
					System.out.println(data.ID);
					System.out.println(data_User.Channels);
					
					data_User.Channels.add(data.ID);
					
					db.updateData(data_User);
					
					session.setAttribute("user", data_User);
					PrintWriter out=response.getWriter();
					successLogin(out, data_User);
				}
			 	
			}
			
			
			
			
			
			
		}
		/*
		//System.out.println("XPATH is:   "+request.getParameterValues("xpath"));
		int reqNum=0;
		
		for(int count=0;count<request.getParameterValues("xpath").length;count++)
		{
			if(request.getParameterValues("xpath")[count].equalsIgnoreCase(""));
			else
			{
				reqNum++;
			}
		}
		String [] reqs=new String[reqNum];
		reqNum=0;
		for(int count=0;count<request.getParameterValues("xpath").length;count++)
		{
			if(request.getParameterValues("xpath")[count].equalsIgnoreCase(""));
			else
			{
				reqs[reqNum]=request.getParameterValues("xpath")[count];
				reqNum++;
			}
		}
		
		
		XPathEngine engine=new XPathEngine(reqs);
		
		for(int i=0;i<engine.xpathIsCorrect.length;i++)
		{
			//System.out.println((i+1)+":   "+String.valueOf(engine.xpathIsCorrect[i]).toUpperCase());
		}
		
		//System.out.println("URL:  "+request.getParameterValues("url"));
		
		HttpClient client=new HttpClient(request.getParameterValues("url")[0]);
		
		ByteArrayOutputStream outStream=client.fetchData();
	
	    if(outStream==null)
		{
			//: Problem with URL Specified..
			PrintWriter out=response.getWriter();
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>");
			out.println("URL Problem");
			out.println("</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			out.println("There has been an error assocaited while parsing the URL.</br> Program Terminated");
			out.println("</BODY>");
			out.println("</HTML>");
			
		}
		
		if(new String(outStream.toByteArray()).equalsIgnoreCase("404"))
		{
			PrintWriter out=response.getWriter();
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>");
			out.println("URL Retrival Problem");
			out.println("</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			out.println("File cannot be found.</br> Program Terminated");
			out.println("</BODY>");
			out.println("</HTML>");
		}
		else
		{
			
			
			
		//System.out.println("LENGTH INPUT: "+outStream.size());

		Document root=engine.createDOM(outStream,client);
		
		if(root!=null)
		{
			for(int i=1;i<=reqs.length;i++)
			{
			if(!engine.xpathIsCorrect[i-1])
			{
				// XPath not correct
				// Ignoring this XPath and proceeding with others
			}
			else
			{
				// CALL EVALAUTE FN
				boolean[] status=engine.evaluate(root);
			}
			}
			// PRINTING DATA TO USER
			PrintWriter out=response.getWriter();
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>");
			out.println("XPathEngine Results");
			out.println("</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			
			out.println("Supplied URL is: "+request.getParameterValues("url")[0]+"</br>");
			out.println("Status..</br></br>");
			
			out.println("<table>");
			
			for(int i=0;i<reqs.length;i++)
			{
				out.println("<tr>");
				out.println("<td><b>"+engine.xpaths[i]+"</b></td>");
				out.println("<td></td><td></td>");
				out.println("<td><b>"+String.valueOf(engine.statuses[i]).toUpperCase()+"</b></td>");
				out.println("</tr>");
			}
			
			out.println("</table>");
				
			
			out.println("</BODY>");
			out.println("</HTML>");
			
		}
		else
		{
		//SOMETHING WRONG WITH SUPPLIED URL
		//
			PrintWriter out=response.getWriter();
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>");
			out.println("URL LINK DATA Problem");
			out.println("</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			out.println("There has been an error assocaited while parsing the data of the URL. Or the MIME Type is not supported</br> Program Terminated");
			out.println("</BODY>");
			out.println("</HTML>");
		}
		
		
	}
	*/
		
		
	}
	
	
	public void openAdminControl(PrintWriter out, int Status)
	{
	//	System.out.println(XPathCrawler.crawler);
		if(XPathCrawler.crawler!=null || Status==5)
		{
			System.out.println("CRAWLER RUNNING");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Admin Page</title>");
			out.println("</head>");
			out.println("<body>");
			
			out.println("<h1>CRAWLING!!</h1>");
			out.println("<a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"?status=STOPCRAWL\">STOP CRAWL and DISPLAY REPORT</a> ");
			
			out.println("</body>");
			out.println("</html>");
		}
		else
		{
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Admin Page</title>");
			out.println("</head>");
			out.println("<body>");
			if(Status==1)
			{
				out.println("Error in giving data; the first two fields shouldn't be null");
			}
			else if(Status==2)
			{
				out.println("Error in giving data; Check size and # of files fields");
			}
			else if(Status==3)
			{
				out.println("Error in giving data; the number of files filed is not valid integer");
			}
			out.println("<form action=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"\" method=\"GET\" >");
			
			out.println("<h1>NOT CRAWLING!!</h1>");
			out.println("<table>");
			
			out.println("<tr>");
			out.println("<td>Enter URL to crawl: </td>");
			out.println("<td><input type=\"text\" name=\"URL2CRAWL\" size=\"30\"></td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td>Enter MaxSize: </td>");
			out.println("<td><input type=\"text\" name=\"SIZE2CRAWL\" size=\"30\"></td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td>Enter MaxFiles: </td>");
			out.println("<td><input type=\"text\" name=\"FILES2CRAWL\" size=\"30\"></td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td><input type=\"hidden\" name=\"status\" value=\"STARTCRAWL\"></td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<input type=\"submit\" name=\"submit\" value=\"Start Crawl\">");
			
		//	out.println("<td><a href=\"http://localhost:"+request.getServerPort()+request.getRequestURI()+"\"> HOME! </a> </td>");
			out.println("</tr>");
			
			out.println("</table>");
			
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
			
			
			System.out.println("CRAWLER NOT RUNNING");
		}
		//TODO: Crawler Web Interface!!
		
	}
	
	
	
	public void displayCrawlResult(PrintWriter out, XPathCrawler crawlData, String doc)
	{
		out.println("<html>");
		out.println("<head>");
		out.println("<title>");
		out.println("Crawl Results");
		out.println("</title>");
		out.println("</head>");
		out.println("<body>");
		
		out.println("<table>");
		
		out.println("<tr>");
		out.println("<td><h2>Crawl Statistics: </h2></td>");
		out.println("</tr>");
	
		out.println("<tr>");
		out.println("<td>The Number of HTML Crawled Pages: </td>");
		out.println("<td><b>"+crawlData.crawling.NumHtml+"</b></td>");
		out.println("</tr>");
	
		out.println("<tr>");
		out.println("<td>The Number of XML Crawled Docs: </td>");
		out.println("<td><b>"+crawlData.crawling.NumXml+"</b></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td>The Amount of data Downloaded: </td>");
		out.println("<td><b>"+crawlData.findTotalSize(doc)+"</b></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td>The Number of servers visited: </td>");
		out.println("<td><b>"+crawlData.crawling.servers.size()+"</b></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td><h3>Channel List: </h3></td></br>");
		out.println("</tr>");
		out.println("<tr></br></tr>");
		
		Hashtable<String,Integer> data=crawlData.findMatchedDocsSize(doc);
		
		for(String channel: data.keySet())
		{
			out.println("<tr>");
			out.println("<td>Channel "+channel+"</td>");
			out.println("<td><b>"+data.get(channel)+"</b></td>");
			out.println("</tr>");
			
		}
		
		String s=null;
		for(String s1: crawlData.crawling.servers.keySet())
		{
			s=s1;
			break;
		}
		
		out.println("<tr>");
		out.println("<td>The servers which is maximum matches visited: </td>");
		out.println("<td><b>"+s+"</b></td>");
		out.println("</tr>");
			
		
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
		
		
	}
	
}
	
