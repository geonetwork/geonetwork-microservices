<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:map="http://www.w3.org/2005/xpath-functions/map"
                xmlns:gn-ogcapir-util="https://geonetwork-opensource.org/gn-ogcapir-util"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>

  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>
  <xsl:import href="collection-fn.xsl"/>
  <xsl:import href="html-util.xsl"/>

  <xsl:template match="/">
    <xsl:message><xsl:copy-of select="model" /></xsl:message>

    <xsl:variable name="collections"
                  select="model/collections/collection/source"
                  as="node()*"/>

    <xsl:variable name="mainCollection"
                  select="$collections[type = 'portal']"
                  as="node()?"/>
    <xsl:variable name="mainCollectionName"
                  select="gn-ogcapir-util:getCollectionName($mainCollection, $language)"/>

    <xsl:variable name="portals"
                  select="$collections[type = 'subportal']"
                  as="node()*"/>
    <xsl:variable name="harvesters"
                  select="$collections[type = 'harvester']"
                  as="node()*"/>
    <xsl:variable name="outputFormats"
                  select="/model/outputFormats/outputFormat/name"
                  as="node()*"/>

    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$mainCollectionName"/>
      </xsl:call-template>

      <xsl:call-template name="html-body">
        <xsl:with-param name="logo">
          <img src="{gn-ogcapir-util:getCollectionLogo($mainCollection)}"
               class=""/>
        </xsl:with-param>
        <xsl:with-param name="title">
          <xsl:value-of select="$mainCollectionName"/>
        </xsl:with-param>
        <xsl:with-param name="link" select="$requestUrl"/>
        <xsl:with-param name="content">

          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb">
              <a href="{$requestUrl}/collections">
                <xsl:value-of select="map:get($i18n, 'ogcapir.collections.browseCollections') || '&#160;'"/>
              </a>
            </xsl:with-param>
          </xsl:call-template>

          <xsl:call-template name="render-page-format-links">
            <xsl:with-param name="formats"
                            select="$outputFormats"/>
          </xsl:call-template>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>

</xsl:stylesheet>