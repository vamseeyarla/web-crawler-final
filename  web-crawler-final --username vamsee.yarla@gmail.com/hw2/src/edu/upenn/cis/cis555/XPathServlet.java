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

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

/**
 * @author cis555
 *
 */
public class XPathServlet extends HttpServlet{

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
	
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
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
	
	}
	
}
	
