/**
 * 
 */
package edu.upenn.cis.cis555;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

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
			
			DB db= DB.getInstance("JEDB");
			
			System.out.println(data.Channels);
			
		
			db.deleteChannel(ID);
			
			data.Channels.remove(ID);
			//	db.UserIndex.put(data);
				db.updateData(data);
				
			//db.ChannelIndex.delete(ID);
			session.setAttribute("user", data);
			successLogin(out, data);
			
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
		out.println("<form action=\"http://localhost:1234/login\" method=\"POST\" >");
	
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
	
		out.println("<td> <a href=\"http://localhost:1234/login?status=NEWUSER\"> New Users! Signup.. </a></td>");
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
		out.println("<form action=\"http://localhost:1234/login\" method=\"POST\" >");
		
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
		
		out.println("<table>" +
				     "<tr><td>");
		out.println("<b>Enter Username:</b></td>");
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
	
		out.println("<td> <a href=\"http://localhost:1234/login\"> Back! </a></td>");
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
		out.println("<form action=\"http://localhost:1234/login\" method=\"POST\" >");
		
		
		out.println("<h3>Welcome "+data.Username+"<h3>");
		out.println("<a href=\"http://localhost:1234/login?status=LOGOUT\"> Logout </a>");
		
		
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
		
		DB db= DB.getInstance("JEDB");
		
		EntityCursor<ChannelData> channel_data=db.ChannelIndex.entities();
		for(ChannelData temp: channel_data)
		{
			if(data.Channels.contains(temp.ID))
			{
				out.println("<tr>  <td><a href=\"http://localhost:1234/login\"> Channel: "+temp.Name+" </a> </td>");
				out.println("<td><a href=\"http://localhost:1234/login?status=DELETECHANNEL&id="+temp.ID+"\"> Delete </a> </td>");
				out.println("</tr>");
			}
			else
			{
				out.println("<tr> <td> Channel: "+temp.Name+" </a> </td></tr>");
			}
		}
			
			
		out.println("</table>");
		
		out.println("</form>");
		out.println("</BODY>");
		out.println("</HTML>");
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("status").equalsIgnoreCase("NEWCRED"))
		{
			String Username=request.getParameter("username");
			String Password=request.getParameter("password");
			String Password1=request.getParameter("password1");
			
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
			DB db= DB.getInstance("JEDB");
			
			if(db.checkUserExists(Username))
			{
				PrintWriter out=response.getWriter();
				showNewUserSignUp(out, 4);
				
				return;
			}
		      db.addUser(Username, Password);
		      
		      
		      PrintWriter out=response.getWriter();
		      showDefaultLogin(out, 3);
		}
		
		else if(request.getParameter("status").equalsIgnoreCase("LOGIN"))
		{
			String Username=request.getParameter("username");
			String Password=request.getParameter("password");
			
			DB db= DB.getInstance("JEDB");
			
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
		else if(request.getParameter("status").equalsIgnoreCase("CHANNEL"))
		{
		
			
			String Name=request.getParameter("channel_name");
			String[] XPaths=request.getParameterValues("xpaths");
			String XSL=request.getParameter("xsl");
			
			if(Name.equalsIgnoreCase(""))
			{
				System.out.println("Name cannot be NULL");
			}
			else
			{
				DB db= DB.getInstance("JEDB");
				String ID=db.nextChannelID();
				
				ChannelData data=new ChannelData(ID,Name,XPaths,XSL);
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
	
}
	
