<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                exclude-result-prefixes="#all"
                version="3.0">


  <xsl:template name="render-field">
    <xsl:param name="label"/>
    <xsl:param name="text"/>

    <div class="w-1/3">
      <xsl:value-of select="$label"/>
    </div>
    <div class="w-2/3 font-medium">
      <xsl:value-of select="$text"/>
    </div>
  </xsl:template>


  <xsl:template name="render-record-preview-title">
    <div
      class="h-10 w-full border-b border-gray-200 transition duration-200 hover:bg-gray-100 border-gray-300">
      <a href="items/{uuid}">
        <div class="h-full flex flex-row items-center">
          <div class="h-12 w-12 relative flex-shrink-0 overflow-hidden">
            <xsl:choose>
              <xsl:when test="source/overview/url != ''">
                <img class="w-10 h-full flex-shrink-0 border-r border-gray-200 bg-gray-100"
                     src="{source/overview/url}"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:call-template name="render-overview-missing"/>
              </xsl:otherwise>
            </xsl:choose>
          </div>
          <div class="flex-grow h-full px-1 py-1 flex flex-col overflow-hidden">
            <h1 class="title-font text-sm font-bold text-gray-900 truncate md:overflow-clip">
              <xsl:value-of
                select="(source/resourceTitleObject/entry[key = 'default']/value|uuid)[1]"/>
            </h1>
          </div>
        </div>
      </a>
    </div>
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