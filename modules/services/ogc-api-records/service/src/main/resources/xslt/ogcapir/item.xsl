<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>

  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>

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

    <xsl:variable name="title" as="xs:string">
      <xsl:variable name="text">
        <xsl:apply-templates mode="getTitle" select="$metadata"/>
      </xsl:variable>
      <xsl:value-of select="normalize-space($text)"/>
    </xsl:variable>

    <xsl:variable name="abstract" as="xs:string*">
      <xsl:apply-templates mode="getAbstract" select="$metadata"/>
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
        <xsl:with-param name="title" select="$title"/>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="header">
              <div class="flex">
                <div class="w-2/3">
                  <xsl:value-of select="$abstract"/>
                </div>
                <div class="w-1/3">
                  <xsl:for-each select="$overviews">
                    <img class="rounded" src="{url}">
                      <xsl:if test="label">
                        <xsl:attribute name="title" select="label"/>
                      </xsl:if>
                    </img>
                  </xsl:for-each>
                </div>
              </div>
            </xsl:with-param>
          </xsl:call-template>


          <section class="bg-white border-b py-8">
            <div class="container mx-auto flex flex-wrap pt-4 pb-12 text-gray-800">
              <xsl:apply-templates mode="landingpage" select="$metadata"/>
            </div>
          </section>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>