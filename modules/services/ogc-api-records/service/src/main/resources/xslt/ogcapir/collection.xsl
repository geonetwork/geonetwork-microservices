<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gn-util="https://geonetwork-opensource.org/gn-util"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>

  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>
  <xsl:import href="collection-util.xsl"/>

  <xsl:template match="/">
    <xsl:variable name="collection"
                  select="model/collection"
                  as="node()?"/>

    <xsl:variable name="label"
                  select="gn-util:getCollectionName($collection, $language)"
                  as="xs:string"/>

    <xsl:variable name="properties"
                  select="tokenize($label, '\|')"
                  as="xs:string*"/>

    <xsl:variable name="title"
                    select="if (empty($properties[1])) then name else $properties[1]"/>
    <xsl:variable name="subTitle"
                    select="if (empty($properties[2])) then '' else $properties[2]"/>

    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$title"/>
      </xsl:call-template>
      <xsl:call-template name="html-body">
        <xsl:with-param name="logo">
          <img src="{gn-util:getCollectionLogo($collection)}"
               class=""/>
        </xsl:with-param>
        <xsl:with-param name="title">
          <xsl:value-of select="$title"/>
        </xsl:with-param>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb" select="$subTitle"/>
          </xsl:call-template>

          <section class="bg-white border-b py-8">
            <div class="container mx-auto flex flex-wrap pt-4 pb-12 text-gray-800">
              <xsl:choose>
                <xsl:when test="model/items">
                  <ul>
                    <xsl:for-each select="model/items/item">
                      <li>
                        <a href="items/{uuid}">
                          <xsl:value-of select="uuid"/>
                        </a>
                      </li>
                    </xsl:for-each>
                  </ul>
                </xsl:when>
                <xsl:otherwise>
                  <a href="{$collection/uuid}/items">
                    <button class="p-4 rounded-full bg-gray-800 text-white
                               transition duration-500 ease-in-out
                               hover:underline
                               focus:outline-none focus:shadow-outline
                               transform transition hover:scale-105 duration-300 ease-in-out">
                      Browse datasets and maps...</button>
                  </a>
                </xsl:otherwise>
              </xsl:choose>
            </div>
          </section>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>