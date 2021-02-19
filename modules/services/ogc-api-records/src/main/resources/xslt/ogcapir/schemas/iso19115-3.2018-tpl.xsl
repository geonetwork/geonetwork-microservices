<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:gex="http://standards.iso.org/iso/19115/-3/gex/1.0"
                xmlns:lan="http://standards.iso.org/iso/19115/-3/lan/1.0"
                xmlns:srv="http://standards.iso.org/iso/19115/-3/srv/2.1"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:mco="http://standards.iso.org/iso/19115/-3/mco/1.0"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:mrd="http://standards.iso.org/iso/19115/-3/mrd/1.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:mrl="http://standards.iso.org/iso/19115/-3/mrl/2.0"
                xmlns:mrs="http://standards.iso.org/iso/19115/-3/mrs/1.0"
                xmlns:mdq="http://standards.iso.org/iso/19157/-2/mdq/1.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">


  <xsl:template mode="getTitle" match="mdb:MD_Metadata">
    <xsl:apply-templates mode="getText"
                         select="mdb:identificationInfo/*/mri:citation/*/cit:title"/>
  </xsl:template>


  <xsl:template mode="getShortAbstract" match="mdb:MD_Metadata">
    <xsl:variable name="abstract">
      <xsl:apply-templates mode="getText"
                           select="mdb:identificationInfo/*/mri:abstract"/>
    </xsl:variable>
    <xsl:value-of select="substring-before($abstract, '.') || '.'"/>
  </xsl:template>


  <xsl:template mode="getAfterShortAbstract" match="mdb:MD_Metadata">
    <xsl:variable name="abstract">
      <xsl:apply-templates mode="getText"
                           select="mdb:identificationInfo/*/mri:abstract"/>
    </xsl:variable>
    <xsl:value-of select="substring-after($abstract, '.')"/>
  </xsl:template>


  <xsl:template mode="getAbstract" match="mdb:MD_Metadata">
    <xsl:apply-templates mode="getText"
                         select="mdb:identificationInfo/*/mri:abstract"/>
  </xsl:template>


  <xsl:template mode="getOverviews" match="mdb:MD_Metadata" as="element(overview)*">
    <xsl:for-each select="mdb:identificationInfo/*/mri:graphicOverview/*">
      <overview>
        <url>
          <xsl:value-of select="mcc:fileName/gco:CharacterString/text()"/>
        </url>
        <label>
          <xsl:apply-templates mode="getText"
                               select="mcc:fileDescription"/>
        </label>
      </overview>
    </xsl:for-each>
  </xsl:template>


  <xsl:template name="getText-iso19115-3.2018"
                match="*[gco:CharacterString]">
    <xsl:value-of select="gco:CharacterString/text()"/>
  </xsl:template>
</xsl:stylesheet>