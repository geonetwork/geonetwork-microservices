<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">

  <xsl:template match="/">
    <xsl:message><xsl:copy-of select="."/></xsl:message>
    <error>
      <xsl:copy-of select="."/>
    </error>
  </xsl:template>
</xsl:stylesheet>