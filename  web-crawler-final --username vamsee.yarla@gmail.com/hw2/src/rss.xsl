<?xml version="1.0" encoding="UTF-16"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
        <head></head>       
      <body>
            <xsl:for-each select="/documentcollection/document">
			
                <h2>
                   <a href="{link}"><xsl:value-of select="@location" /></a>
                </h2>
              
                     
            </xsl:for-each>
        
            
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>