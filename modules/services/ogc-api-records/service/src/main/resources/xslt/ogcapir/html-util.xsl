<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">


  <xsl:template name="render-field">
    <xsl:param name="label" as="xs:string?"/>
    <xsl:param name="text" as="xs:string?"/>

    <div class="px-3 py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-5 w-full border-b border-gray-200 odd:bg-gray-50">
      <div class="text-sm font-medium text-gray-500">
        <xsl:value-of select="$label"/>
      </div>
      <div class="text-sm text-gray-900">
        <xsl:value-of select="$text"/>
      </div>
    </div>
  </xsl:template>


  <xsl:template name="render-record-preview-title">
    <div
      class="w-full border-b hover:bg-gray-100 border-gray-200">
      <a href="items/{uuid}">
        <div class="h-full flex flex-row items-center px-3 py-3 sm:px-5">
          <div class="h-10 w-10 relative flex-shrink-0 overflow-hidden">
            <xsl:choose>
              <xsl:when test="source/overview/url != ''">
                <img class="w-10 h-full flex-shrink-0 border border-gray-200 bg-gray-100 rounded"
                     src="{source/overview/url}"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:call-template name="render-overview-missing"/>
              </xsl:otherwise>
            </xsl:choose>
          </div>
          <div class="flex-grow h-full px-3 py-3 flex flex-col overflow-hidden">
            <h3 class="text-sm text-gray-900 truncate md:overflow-clip">
              <xsl:value-of
                select="(source/resourceTitleObject/entry[key = 'default']/value|uuid)[1]"/>
            </h3>
          </div>
        </div>
      </a>
    </div>
  </xsl:template>


  <xsl:template name="render-page-format-links">
    <xsl:param name="formats"
               as="xs:string*"/>

    <xsl:if test="count($formats) > 0">
      <div class="flex flex-row-reverse flex-wrap pb-6 pl-4">
        <xsl:for-each select="$formats">
          <a class="ml-2 mb-2 bg-blue-500 hover:bg-blue-600 text-white text-xs p-3 rounded leading-none flex items-center focus:outline-none focus:shadow-outline
                   transform transition hover:scale-105 duration-300 ease-in-out"
             href="?f={.}"><xsl:value-of select="."/></a>
        </xsl:for-each>
      </div>
    </xsl:if>
  </xsl:template>


  <xsl:template name="render-overview-missing">
    <svg
      class="text-gray-200 absolute h-12 w-12 transform -translate-x-1/2 -translate-y-1/2"
      style="top: 50%; left: 50%;"
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        stroke-width="2"
        d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
      />
    </svg>
  </xsl:template>
</xsl:stylesheet>