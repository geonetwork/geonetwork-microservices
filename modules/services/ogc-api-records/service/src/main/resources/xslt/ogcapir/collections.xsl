<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>
  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>


  <xsl:template name="render-collection-family">
    <xsl:param name="title" as="xs:string"/>
    <xsl:param name="collections" as="element(values)*"/>

    <xsl:if test="count($collections) > 0">
      <section class="bg-white border-b py-8">
        <div class="container mx-auto flex flex-wrap pt-4 pb-12">
          <xsl:if test="$title">
            <h1 class="w-full my-2 text-5xl font-bold leading-tight text-center text-gray-800">
              <xsl:value-of select="$title"/>
            </h1>
          </xsl:if>
          <div class="w-full mb-4">
            <div class="h-1 mx-auto gradient w-64 opacity-25 my-0 py-0 rounded-t"></div>
          </div>



          <xsl:for-each select="$collections">
            <xsl:variable name="label"
                          select="(
                            labelTranslations/entry[key = $language]/value[. != '']
                            |name[. != '']
                            |uuid)[1]"
                          as="xs:string"/>

            <xsl:variable name="properties"
                          select="tokenize($label, '\|')"
                          as="xs:string*"/>

            <xsl:call-template name="render-collection">
              <xsl:with-param name="title"
                              select="if (empty($properties[1])) then name else $properties[1]"/>
              <xsl:with-param name="subTitle"
                              select="if (empty($properties[2])) then '' else $properties[2]"/>
              <xsl:with-param name="logo" select="if (type = 'harvester') then concat($logoFolder, '/', uuid, '.png') else concat($harvestingFolder, '/', logo)"/>
              <xsl:with-param name="url" select="concat('', uuid)"/>
            </xsl:call-template>
          </xsl:for-each>
        </div>
      </section>
    </xsl:if>
  </xsl:template>


  <xsl:template name="render-collection">
    <xsl:param name="title" as="xs:string"/>
    <xsl:param name="subTitle" as="xs:string?"/>
    <xsl:param name="logo" as="xs:string?"/>
    <xsl:param name="url" as="xs:string?"/>

    <div class="w-full md:w-1/3 p-6 flex flex-col flex-shrink">
      <div class="flex-1 bg-white rounded-t rounded-b-none overflow-hidden shadow transition duration-200 hover:bg-gray-100">
        <a href="{$url}" class="flex flex-wrap no-underline hover:no-underline">
          <div class="flex items-end bg-cover w-full h-60"
               style="background-image: url({$base}{$logo})">
            <div class="w-full bg-gray-900 bg-opacity-75 font-bold text-xl text-center text-white py-6 mb-6">
              <xsl:value-of select="$title"/>
            </div>
          </div>

          <div class="text-gray-800 text-center p-5 w-full">
            <xsl:if test="$subTitle">
              <xsl:value-of select="$subTitle"/>
            </xsl:if>
          </div>
        </a>
      </div>
    </div>
  </xsl:template>



  <xsl:template match="/">

    <xsl:variable name="collections"
                  select="list/values"
                  as="node()*"/>

    <xsl:variable name="mainCollection"
                  select="$collections[type = 'portal' or not(type)][1]"
                  as="node()?"/>
    <xsl:variable name="portals"
                  select="$collections[type = 'subportal']"
                  as="node()*"/>
    <xsl:variable name="harvesters"
                  select="$collections[type = 'harvester']"
                  as="node()*"/>

    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$mainCollection/name"/>
      </xsl:call-template>
      <xsl:call-template name="html-body">
        <xsl:with-param name="title" select="$mainCollection/name"/>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb" select="$mainCollection/name"/>
          </xsl:call-template>

          <xsl:call-template name="render-collection-family">
            <xsl:with-param name="title" select="'Thematic portals'"/>
            <xsl:with-param name="collections" select="$portals"/>
          </xsl:call-template>

          <xsl:call-template name="render-collection-family">
            <xsl:with-param name="title" select="'Harvested collections'"/>
            <xsl:with-param name="collections" select="$harvesters"/>
          </xsl:call-template>

        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>