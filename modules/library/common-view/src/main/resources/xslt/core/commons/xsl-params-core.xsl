<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:map="http://www.w3.org/2005/xpath-functions/map"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:param name="i18n" select="map {}" as="map(xs:string, xs:string)"/>
  <xsl:param name="language" select="'en'" as="xs:string"/>
  <xsl:param name="language3letters" select="'eng'" as="xs:string"/>
  <xsl:param name="base" select="'http://localhost:8080/geonetwork'" as="xs:string"/>

  <xsl:variable name="harvestingFolder" select="'/images/harvesting'" as="xs:string"/>
  <xsl:variable name="logoFolder" select="'/images/logos'" as="xs:string"/>
</xsl:stylesheet>