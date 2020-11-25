<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:import href="../../schemas-utils/util-iso19139.xsl"/>
  <xsl:import href="../../html-util.xsl"/>

  <xsl:template match="gmd:*[gco:CharacterString]"
                mode="landingpage" priority="2">
    
    <xsl:call-template name="html-field">
      <xsl:with-param name="label" select="name(.)"/>
      <xsl:with-param name="text">
        <xsl:call-template name="getText"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>
</xsl:stylesheet>