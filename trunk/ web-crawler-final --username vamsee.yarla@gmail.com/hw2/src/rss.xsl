<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
<xsl:output method="html"/>

  <xsl:template match="/">
  <html>
  <head></head>
  <body>
	
            <xsl:for-each select="/documentcollection/document">
              <h3>
             	  <a href="{@location}"><xsl:value-of select="@location" /></a>
			 </h3>
			 
			  <xsl:for-each select="rss/channel/item[title[contains(text(),'war')]]">
			  
			   <table>
			   
			   <tr>
			   <td> Title: </td>
				<td> <xsl:value-of select="title" /></td>
				</tr>
				
				
				<xsl:choose>
				<xsl:when test="description">
						
				<tr>
			    <td> Description: </td>
				<td>  <xsl:value-of select="description" /></td>
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>
				
				
				<xsl:choose>
				<xsl:when test="link">
				
				 <tr>
			  <td> Link: </td>
				<td> <a href="{link}"><xsl:value-of select="link" /></a></td>
				
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>
				

				</table>
				</xsl:for-each>    
				
				<xsl:for-each select="rss/channel/item[title[contains(text(),'peace')]]">
			  
				   <table>
			   
			   <tr>
			   <td> Title: </td>
				<td> <xsl:value-of select="title" /></td>
				</tr>
				
				<xsl:choose>
				<xsl:when test="description">
						
				<tr>
			    <td> Description: </td>
				<td>  <xsl:value-of select="description" /></td>
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>
				
				
				<xsl:choose>
				<xsl:when test="link">
				
				 <tr>
			  <td> Link: </td>
				<td> <a href="{link}"><xsl:value-of select="link" /></a></td>
				
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>

				</table>
				
				</xsl:for-each>    
				
				<xsl:for-each select="rss/channel/item[description[contains(text(),'war')]]">
			  
				  <table>
			   
			   
			 <xsl:choose>
				<xsl:when test="title">
						
				<tr>
			    <td> Title: </td>
				<td>  <xsl:value-of select="title" /></td>
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>
				
				 <tr>
			    <td> Description: </td>
				<td>  <xsl:value-of select="description" /></td>
				</tr>
				
				
				
				
				
				
				<xsl:choose>
				<xsl:when test="link">
				
				 <tr>
			  <td> Link: </td>
				<td> <a href="{link}"><xsl:value-of select="link" /></a></td>
				
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>
				

				</table>
				
				</xsl:for-each>    
				
				<xsl:for-each select="rss/channel/item[description[contains(text(),'peace')]]">
			  
				   <table>
			   
			   <xsl:choose>
				<xsl:when test="title">
						
				<tr>
			    <td> Title: </td>
				<td>  <xsl:value-of select="title" /></td>
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>
				
				 <tr>
			    <td> Description: </td>
				<td>  <xsl:value-of select="description" /></td>
				</tr>
				
				
				
				
				
				
				<xsl:choose>
				<xsl:when test="link">
				
				 <tr>
			  <td> Link: </td>
				<td> <a href="{link}"><xsl:value-of select="link" /></a></td>
				
				</tr>
				
				</xsl:when>
				<xsl:otherwise>
			
				</xsl:otherwise>
				</xsl:choose>
				
				

				</table>
				
				</xsl:for-each>    
             
            </xsl:for-each>            
 </body>
 </html>
  </xsl:template>
</xsl:stylesheet>