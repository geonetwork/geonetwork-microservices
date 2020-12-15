<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-ogcapir-util="https://geonetwork-opensource.org/gn-ogcapir-util"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>

  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>

  <xsl:import href="collection-fn.xsl"/>
  <xsl:import href="formats/landingpage/landingpage.xsl"/>

  <xsl:template match="/">

    <xsl:variable name="item"
                  select="model/items/item"/>
    <xsl:variable name="uuid"
                  select="$item/uuid"
                  as="xs:string"/>
    <xsl:variable name="metadata"
                  select="parse-xml($item/xml)"
                  as="node()"/>
    <xsl:variable name="collection"
                  select="model/collection"
                  as="node()?"/>

    <xsl:variable name="title" as="xs:string">
      <xsl:variable name="text">
        <xsl:apply-templates mode="getTitle" select="$metadata"/>
      </xsl:variable>
      <xsl:value-of select="normalize-space($text)"/>
    </xsl:variable>

    <xsl:variable name="shortAbstract" as="xs:string*">
      <xsl:apply-templates mode="getShortAbstract" select="$metadata"/>
    </xsl:variable>

    <xsl:variable name="abstract" as="xs:string*">
      <xsl:apply-templates mode="getAfterShortAbstract" select="$metadata"/>
    </xsl:variable>

    <xsl:variable name="overviews" as="element(overview)*">
      <xsl:apply-templates mode="getOverviews" select="$metadata"/>
    </xsl:variable>

    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$title"/>
      </xsl:call-template>
      <xsl:call-template name="html-body">
        <xsl:with-param name="logo">
          <img src="{if ($overviews[1])
                     then $overviews[1]/url || '?size=20'
                     else gn-ogcapir-util:getCollectionLogo($collection)}"
               class=""/>
        </xsl:with-param>
        <xsl:with-param name="title" select="$title"/>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="header"
                            select="$shortAbstract"/>
          </xsl:call-template>


          <section class="bg-white border-b py-8">
            <div class="container mx-auto flex flex-wrap pt-4 pb-12 text-gray-800">

              <div class="flex items-center">
                <div class="w-2/3 whitespace-pre-line">
                  <xsl:value-of select="$abstract"/>
                </div>
                <div class="w-1/3">
                  <xsl:for-each select="$overviews">
                    <img class="rounded shadow" src="{url}">
                      <xsl:if test="label">
                        <xsl:attribute name="title" select="label"/>
                      </xsl:if>
                    </img>
                  </xsl:for-each>
                </div>
              </div>
            </div>

            <div class="w-full border-t-4 border-blue-900 bg-gray-100 p-2 text-gray-800">
              <xsl:apply-templates mode="landingpage" select="$metadata"/>
            </div>

          </section>


          <xsl:call-template name="render-page-format-links">
            <xsl:with-param name="formats"
                            select="('json', 'jsonld', 'xml', 'dcat', 'gn')"/>
          </xsl:call-template>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>