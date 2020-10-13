<!--

    (c) 2020 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:include href="constant.xsl"/>
  <xsl:include href="iso19139-utility.xsl"/>

  <xsl:variable name="properties" as="node()*">
    <property name="resourceTitle" xpath="gmd:identificationInfo/*/gmd:citation/*/gmd:title" type="multilingual"/>
    <property name="resourceAbstract" xpath="gmd:identificationInfo/*/gmd:abstract" type="multilingual"/>
    <property name="metadataIdentifier" xpath="gmd:fileIdentifier"/>
    <property name="standardName" xpath="gmd:metadataStandardName"/>
    <property name="tagNumber" xpath="count(//gmd:keyword)"/>
    <property name="contact" xpath="gmd:contact" type="contact"/>
  </xsl:variable>


  <xsl:template match="/">
    <indexRecords>
      <xsl:apply-templates mode="index" select="*"/>
    </indexRecords>
  </xsl:template>


  <xsl:template mode="index"
                match="indexRecord">
    <xsl:copy>
      <xsl:copy-of select="*[name() != 'document']"/>
      <xsl:variable name="xml"
                    select="parse-xml(document)"/>
      <xsl:apply-templates mode="index"
                           select="$xml"/>
    </xsl:copy>
  </xsl:template>


  <xsl:template mode="index"
                match="gmd:contact">
    <contact>
      <role><xsl:value-of select="*/gmd:role/*/@codeListValue"/></role>
      <name><xsl:value-of select="*/gmd:organisationName/*/text()"/></name>
      <organisation><xsl:value-of select="*/gmd:organisationName/*/text()"/></organisation>
      <mail><xsl:value-of select="*//gmd:electronicMailAddress/*/text()"/></mail>
    </contact>
  </xsl:template>


  <xsl:template mode="index"
                match="gmd:MD_Metadata">
    <indexingDate>
      <xsl:value-of select="format-dateTime(current-dateTime(), $dateTimeFormat)"/>
    </indexingDate>

    <xsl:variable name="languages" as="node()*">
      <xsl:call-template name="get-languages">
        <xsl:with-param name="metadata" select="."/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:for-each select="$languages[@id = 'default']">
      <mainLanguage>
        <xsl:value-of select="@code"/>
      </mainLanguage>
    </xsl:for-each>
    <xsl:for-each select="$languages[@id != 'default']">
      <otherLanguage>
        <xsl:value-of select="@code"/>
      </otherLanguage>
      <otherLanguageId>
        <xsl:value-of select="@id"/>
      </otherLanguageId>
    </xsl:for-each>

    <xsl:variable name="record"
                  select="."/>

    <xsl:for-each select="$properties">
      <xsl:variable name="property"
                    select="current()"/>
      <xsl:variable name="values">
        <xsl:evaluate xpath="$property/@xpath"
                      context-item="$record"/>
      </xsl:variable>

      <xsl:choose>
        <xsl:when test="@type = 'codelist'"></xsl:when>
        <xsl:when test="@type = 'integer'"></xsl:when>
        <xsl:when test="@type = 'contact'">
          <xsl:apply-templates mode="index"
                               select="$values"/>
        </xsl:when>
        <xsl:when test="@type = 'link'"></xsl:when>
        <xsl:when test="@type = 'multilingual'">
          <xsl:for-each select="$values/*">
            <xsl:variable name="value" select="."/>

            <xsl:element name="{$property/@name}">
              <xsl:for-each select="$languages">
                <entry>
                  <key>
                    <xsl:value-of select="@id"/>
                  </key>
                  <value>
                    <!-- TODO: match translation-->
                    <xsl:value-of select="normalize-space($value/*/text())"/>
                  </value>
                </entry>
              </xsl:for-each>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="$values castable as xs:int
                            or $values castable as xs:string">
              <xsl:element name="{$property/@name}">
                <xsl:value-of select="normalize-space($values)"/>
              </xsl:element>
            </xsl:when>
            <xsl:otherwise>
              <xsl:for-each select="$values/*">
                <xsl:element name="{$property/@name}">
                  <xsl:value-of select="normalize-space(current()/*/text())"/>
                </xsl:element>
              </xsl:for-each>
            </xsl:otherwise>
            <!--<xsl:when test="$values instance of xs:int">
            </xsl:when>-->
          </xsl:choose>

        </xsl:otherwise>
      </xsl:choose>

    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>