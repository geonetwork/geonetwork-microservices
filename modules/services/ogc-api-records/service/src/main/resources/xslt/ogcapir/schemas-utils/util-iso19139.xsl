<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">


  <xsl:template mode="getTitle" match="gmd:MD_Metadata">
    <xsl:apply-templates mode="getText"
                         select="gmd:identificationInfo/*/gmd:citation/*/gmd:title"/>
  </xsl:template>

  <xsl:template mode="getAbstract" match="gmd:MD_Metadata">
    <xsl:apply-templates mode="getText"
                         select="gmd:identificationInfo/*/gmd:abstract"/>
  </xsl:template>

  <xsl:template mode="getOverviews" match="gmd:MD_Metadata" as="element(overview)*">
    <xsl:for-each select="gmd:identificationInfo/*/gmd:graphicOverview/*">
      <overview>
        <url>
          <xsl:value-of select="gmd:fileName/gco:CharacterString/text()"/>
        </url>
        <label>
          <xsl:apply-templates mode="getText"
                               select="gmd:description"/>
        </label>
      </overview>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="getText" match="*[gco:CharacterString]">
    <xsl:value-of select="gco:CharacterString/text()"/>
  </xsl:template>
</xsl:stylesheet>