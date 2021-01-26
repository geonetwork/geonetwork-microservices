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
    <xsl:variable name="collections"
                  select="model/collections/collection"
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
        <xsl:with-param name="link" select="concat($requestUrl, '/main')"/>
        <xsl:with-param name="content">

          <div class="container mx-auto flex flex-wrap text-gray-800 md:px-4">           
            <div class="w-2/3">
              <xsl:call-template name="html-breadcrumb">
                <xsl:with-param name="breadcrumb">
                  <xsl:value-of select="map:get($i18n, 'ogcapir.collections.browseCollection') || '&#160;'"/>
                  <a href="{$requestUrl}/main" class="text-blue-600 underline"><xsl:value-of select="$mainCollectionName"/></a>
                </xsl:with-param>
              </xsl:call-template>
            </div>
            <div class="w-1/3">
              <xsl:call-template name="render-page-format-links">
                <xsl:with-param name="formats"
                                select="('json', 'xml')"/>
              </xsl:call-template>
            </div>
          </div>

          <xsl:call-template name="render-collection-family">
            <xsl:with-param name="title"
                            select="map:get($i18n, 'ogcapir.collections.portals')"/>
            <xsl:with-param name="collections"
                            select="$portals"/>
          </xsl:call-template>


          <xsl:call-template name="render-collection-family">
            <xsl:with-param name="title"
                            select="map:get($i18n, 'ogcapir.collections.harvestedCollection')"/>
            <xsl:with-param name="collections"
                            select="$harvesters"/>
          </xsl:call-template>


        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>


  <xsl:template name="render-collection-family">
    <xsl:param name="title" as="xs:string"/>
    <xsl:param name="collections" as="element(collection)*"/>

    <xsl:if test="count($collections) > 0">
      <section class="py-4">
        <div class="container mx-auto md:px-4">
          <xsl:if test="$title">
            <h1 class="w-full my-2 text-3xl font-bold leading-tight text-gray-800">
              <xsl:value-of select="$title"/>
            </h1>
          </xsl:if>

          <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-2">
            <xsl:for-each select="$collections">
              <xsl:variable name="label"
                            select="gn-ogcapir-util:getCollectionName(., $language)"
                            as="xs:string"/>

              <xsl:variable name="properties"
                            select="tokenize($label, '\|')"
                            as="xs:string*"/>

              <xsl:call-template name="render-collection">
                <xsl:with-param name="title"
                                select="if (empty($properties[1])) then name else $properties[1]"/>
                <xsl:with-param name="subTitle"
                                select="if (empty($properties[2])) then '' else $properties[2]"/>
                <xsl:with-param name="logo" select="gn-ogcapir-util:getCollectionLogo(.)"/>
                <xsl:with-param name="url" select="concat($requestUrl, '/', uuid)"/>
              </xsl:call-template>
            </xsl:for-each>
          </div>
        </div>
      </section>
    </xsl:if>
  </xsl:template>


  <xsl:template name="render-collection">
    <xsl:param name="title" as="xs:string"/>
    <xsl:param name="subTitle" as="xs:string?"/>
    <xsl:param name="logo" as="xs:string?"/>
    <xsl:param name="url" as="xs:string?"/>

    <section class="rounded shadow border border-gray-200 bg-white mr-4 mb-4">
      <a href="{$url}" class="no-underline hover:no-underline">
        <div class="px-3 py-4 sm:px-5 bg-gray-50">
          <h3 class="font-medium">
            <xsl:value-of select="$title"/>
          </h3>
        </div>
        <div class="border-t border-gray-200 flex flex-wrap h-40"
            style="background-image: url({$logo})">
        </div>
      </a>
    </section>

  </xsl:template>
</xsl:stylesheet>