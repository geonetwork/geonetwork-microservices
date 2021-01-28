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

          <div class="container mx-auto flex flex-wrap pt-4 pb-12 text-gray-800 md:px-4">
            <div class="w-2/3">
              <xsl:choose>
                <xsl:when test="model/results">
                  <form>
                    <label for="collection_search"
                           class="block text-sm font-medium text-gray-800 uppercase">
                      search
                    </label>
                    <div class="mt-1 mb-4 flex">
                      <input type="text"
                             name="q"
                             id="collection_search"
                             class="focus:ring-indigo-500 focus:border-indigo-500 flex-1 block w-full rounded rounded-r-none focus:rounded-r-none sm:text-sm border border-gray-300 px-4"
                             placeholder="type a search term"
                             autofocus=""/>
                      <button class="inline-flex items-center px-4 py-2 rounded-r border border-l-0 border-gray-800 bg-gray-800 text-white text-sm" type="submit">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>
                      </button>
                    </div>
                  </form>

                  <section class="w-full rounded shadow border border-gray-200 bg-white mb-4">
                    <div class="px-3 py-4 sm:px-5 bg-gray-50">
                      <h2 class="font-medium">
                        <xsl:choose>
                          <xsl:when test="model/results/total/total = 0">
                            No record found. Search for something else or in another collection?
                          </xsl:when>
                          <xsl:when test="model/results/total/total = 1">
                            <xsl:value-of select="model/results/total/total"/> record
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:value-of select="model/results/total/total"/> records
                          </xsl:otherwise>
                        </xsl:choose>
                      </h2>
                    </div>

                    <div class="border-t border-gray-200 flex flex-wrap">
                      <xsl:for-each select="model/results/results">
                        <xsl:call-template name="render-record-preview-title"/>
                      </xsl:for-each>
                    </div>
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