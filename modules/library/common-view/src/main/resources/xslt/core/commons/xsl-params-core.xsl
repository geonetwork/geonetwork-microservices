<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:param name="language" select="'en'" as="xs:string"/>
  <xsl:param name="language3letter" select="'eng'" as="xs:string"/>
  <xsl:param name="base" select="'http://localhost:8080/geonetwork'" as="xs:string"/>

  <xsl:variable name="harvestingFolder" select="'/images/harvesting'" as="xs:string"/>
  <xsl:variable name="logoFolder" select="'/images/logos'" as="xs:string"/>
</xsl:stylesheet>