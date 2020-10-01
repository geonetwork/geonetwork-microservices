<!--

    (c) 2020 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:include href="constant.xsl"/>
  <xsl:include href="iso19139-utility.xsl"/>


  <xsl:variable name="multilingualProperties">
    <property field="resourceTitle" xpath=""/>
  </xsl:variable>

  <xsl:variable name="propertyNames" as="node()*">
    <property name="metadataIdentifier" nodeName="fileIdentifier"/>
    <property name="standardName" nodeName="metadataStandardName"/>
  </xsl:variable>


  <xsl:template match="/">
    <xsl:apply-templates mode="index" select="*"/>
  </xsl:template>


  <xsl:template mode="index"
                match="gmd:MD_Metadata">
    <indexRecord>
      <docType>metadata</docType>
      <documentStandard>iso19139</documentStandard>
      <indexingDate>
        <xsl:value-of select="format-dateTime(current-dateTime(), $dateTimeFormat)"/>
      </indexingDate>

      <xsl:variable name="languages" as="node()*">
        <xsl:call-template name="get-languages">
          <xsl:with-param name="metadata" select="."/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:for-each select="$languages[@id = 'default']">
        <mainLanguage><xsl:value-of select="@code"/></mainLanguage>
      </xsl:for-each>
      <xsl:for-each select="$languages[@id != 'default']">
        <otherLanguage><xsl:value-of select="@code"/></otherLanguage>
        <otherLanguageId><xsl:value-of select="@id"/></otherLanguageId>
      </xsl:for-each>

      <xsl:apply-templates mode="index" select="*">
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:apply-templates>
    </indexRecord>
  </xsl:template>


  <xsl:template mode="index"
                match="gmd:fileIdentifier
                       |gmd:metadataStandardName">
    <xsl:variable name="property"
                  select="$propertyNames[@nodeName = current()/local-name()]"/>

    <xsl:element name="{if ($property) then $property/@name else local-name()}">
      <xsl:value-of select="*/text()"/>
    </xsl:element>
  </xsl:template>


  <xsl:template mode="index"
                match="gmd:identificationInfo/*/gmd:citation/*/gmd:title">
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="element" select="."/>
    <resourceTitleObject>
      <xsl:for-each select="$languages">
        <entry>
          <key><xsl:value-of select="@id"/></key>
          <value><xsl:value-of select="$element/*/text()"/></value>
        </entry>
      </xsl:for-each>
    </resourceTitleObject>
  </xsl:template>

  <xsl:template mode="index"
                match="*|@*">
    <xsl:param name="languages" as="node()*"/>
    <xsl:apply-templates mode="index" select="*|@*">
      <xsl:with-param name="languages" select="$languages"/>
    </xsl:apply-templates>
  </xsl:template>
</xsl:stylesheet>