<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
    <xsl:variable name="collection"
                  select="model/collection"
                  as="node()?"/>

    <xsl:variable name="label"
                  select="gn-ogcapir-util:getCollectionName($collection, $language)"
                  as="xs:string"/>

    <xsl:variable name="properties"
                  select="tokenize($label, '\|')"
                  as="xs:string*"/>

    <xsl:variable name="outputFormats"
                  select="/model/outputFormats/outputFormat/name"
                  as="node()*"/>

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
          <img src="{gn-ogcapir-util:getCollectionLogo($collection)}"
               class=""/>
        </xsl:with-param>
        <xsl:with-param name="title">
          <xsl:value-of select="$title"/>
        </xsl:with-param>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb" select="$subTitle"/>
          </xsl:call-template>

          <xsl:variable name="total"
                        as="xs:integer?"
                        select="model/results/total/total"/>

          <div class="container mx-auto flex flex-wrap pt-4 pb-12 text-gray-800 md:px-4">
            <div class="w-2/3">
              <xsl:choose>
                <xsl:when test="model/results">

                  <xsl:variable name="q"
                                as="xs:string?"
                                select="model/request/entry[key = 'q']/value/item/text()"/>
                  <form>
                    <div class="mt-1 mb-4 flex">
                      <input type="text"
                             name="q"
                             id="collection_search"
                             class="border border-gray-300
                                    flex-1 block w-full
                                    rounded-l
                                    sm:text-sm px-4"
                             placeholder="Search"
                             autofocus=""
                             value="{$q}"/>

                      <button class="inline-flex items-center px-4 py-2 border border-l-0 border-gray-300 bg-gray-800 text-white text-sm" type="submit">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>
                      </button>

                      <a href="items"
                         class="inline-flex items-center px-4 py-2 rounded-r border border-l-0 border-gray-300 text-sm">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-circle" viewBox="0 0 16 16">
                          <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                          <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                        </svg>
                      </a>
                    </div>
                  </form>

                  <section class="w-full rounded shadow border border-gray-200 bg-white mb-4">
                    <div class="px-3 py-4 sm:px-5 bg-gray-50">
                      <h2 class="font-medium">
                        <xsl:choose>
                          <xsl:when test="$total = 0">
                            No record found.
                            <a href="items" class="underline">Search for something else</a> or
                            <a href=".." class="underline">in another collection?</a>
                          </xsl:when>
                          <xsl:when test="$total = 1">
                            <xsl:value-of select="$total"/> record
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:value-of select="$total"/> records
                          </xsl:otherwise>
                        </xsl:choose>
                      </h2>
                    </div>

                    <div class="border-t border-gray-200 flex flex-wrap">
                      <xsl:for-each select="model/results/results">
                        <xsl:call-template name="render-record-preview-title"/>
                      </xsl:for-each>
                    </div>


                    <xsl:variable name="startindex"
                                  as="xs:integer"
                    select="if (model/request/entry[key = 'startindex']/value/item/text() castable as xs:integer)
                            then xs:integer(model/request/entry[key = 'startindex']/value/item/text())
                            else 10"/>

                    <xsl:variable name="limit"
                                  as="xs:integer"
                                  select="if (model/request/entry[key = 'limit']/value/item/text() castable as xs:integer)
                            then xs:integer(model/request/entry[key = 'limit']/value/item/text())
                            else 10"/>

                    <xsl:variable name="isEndOfResults"
                                  as="xs:boolean"
                                  select="($startindex + $limit) > $total"/>

                    <xsl:if test="not($isEndOfResults)">
                      <xsl:variable name="requestParameters"
                                    as="xs:string"
                                    select="concat(
                                    if($q) then concat('q=', $q, '&amp;') else '',
                                    if($startindex) then concat('startindex=', $startindex + $limit, '&amp;') else '',
                                    if($limit) then concat('limit=', $limit) else ''
                                    )"/>
                      <a href="items?{$requestParameters}"
                         class="inline-flex items-center px-4 py-2 text-sm">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-double-right" viewBox="0 0 16 16">
                          <path fill-rule="evenodd" d="M3.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L9.293 8 3.646 2.354a.5.5 0 0 1 0-.708z"/>
                          <path fill-rule="evenodd" d="M7.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L13.293 8 7.646 2.354a.5.5 0 0 1 0-.708z"/>
                        </svg>
                      </a>
                    </xsl:if>
                  </section>
                </xsl:when>
                <xsl:otherwise>
                  <a href="{$requestUrl}/items">
                    <button class="p-4 rounded-full bg-gray-800 text-white
                                transition duration-500 ease-in-out
                                focus:outline-none focus:shadow-outline
                                transform transition hover:scale-105 duration-300 ease-in-out">
                      Browse datasets and maps...</button>
                  </a>
                </xsl:otherwise>
              </xsl:choose>
            </div>
            <div class="w-1/3">
              <xsl:call-template name="render-page-format-links">
                <xsl:with-param name="formats"
                                select="$outputFormats"/>
              </xsl:call-template>
            </div>
          </div>

        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>