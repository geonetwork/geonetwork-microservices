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
    <xsl:param name="collections" as="node()*"/>

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
            <xsl:variable name="properties"
                          select="tokenize(name, '\|')"
                          as="xs:string*"/>

            <xsl:call-template name="render-collection">
              <xsl:with-param name="title"
                              select="if (type = 'harvester') then name else $properties[1]"/>
              <xsl:with-param name="subTitle"
                              select="if (type = 'harvester') then '' else $properties[2]"/>
              <xsl:with-param name="logo" select="logo"/>
              <xsl:with-param name="url" select="concat('collections/', uuid)"/>
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
      <div class="flex-1 bg-white rounded-t rounded-b-none overflow-hidden shadow transition duration-500 ease-in-out">
        <a href="{$url}" class="flex flex-wrap no-underline hover:no-underline">
          <div class="w-full font-bold text-xl text-center text-gray-800 px-6">
            <xsl:value-of select="$title"/>
          </div>
          <p class="text-gray-800 text-base px-6 mb-5 w-full">
            <xsl:if test="$subTitle">
              <xsl:value-of select="$subTitle"/>
            </xsl:if>

            <xsl:if test="$logo">
              <img src="{$base}{$harvestingFolder}/{$logo}"
                   class="object-cover rounded-sm h-48 w-full"/>
            </xsl:if>
          </p>
        </a>
      </div>
    </div>
  </xsl:template>



  <xsl:template match="/">
    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head"/>
      <xsl:call-template name="html-body">
        <xsl:with-param name="content">

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

          <div class="pt-24">
            <div class="container px-3 mx-auto flex flex-wrap flex-col md:flex-row items-center">
              <div class="flex flex-col w-full md:w-2/5 justify-center items-start text-center md:text-left">
                <p class="uppercase tracking-loose w-full">
                  <xsl:value-of select="$mainCollection/name"/>
                </p>
              </div>
            </div>
          </div>
          <div class="relative -mt-12 lg:-mt-24">
            <svg viewBox="0 0 1428 174" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
              <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                <g transform="translate(-2.000000, 44.000000)" fill="#FFFFFF" fill-rule="nonzero">
                  <path d="M0,0 C90.7283404,0.927527913 147.912752,27.187927 291.910178,59.9119003 C387.908462,81.7278826 543.605069,89.334785 759,82.7326078 C469.336065,156.254352 216.336065,153.6679 0,74.9732496" opacity="0.100000001"></path>
                  <path d="M100,104.708498 C277.413333,72.2345949 426.147877,52.5246657 546.203633,45.5787101 C666.259389,38.6327546 810.524845,41.7979068 979,55.0741668 C931.069965,56.122511 810.303266,74.8455141 616.699903,111.243176 C423.096539,147.640838 250.863238,145.462612 100,104.708498 Z" opacity="0.100000001"></path>
                  <path d="M1046,51.6521276 C1130.83045,29.328812 1279.08318,17.607883 1439,40.1656806 L1439,120 C1271.17211,77.9435312 1140.17211,55.1609071 1046,51.6521276 Z" id="Path-4" opacity="0.200000003"></path>
                </g>
                <g transform="translate(-4.000000, 76.000000)" fill="#FFFFFF" fill-rule="nonzero">
                  <path d="M0.457,34.035 C57.086,53.198 98.208,65.809 123.822,71.865 C181.454,85.495 234.295,90.29 272.033,93.459 C311.355,96.759 396.635,95.801 461.025,91.663 C486.76,90.01 518.727,86.372 556.926,80.752 C595.747,74.596 622.372,70.008 636.799,66.991 C663.913,61.324 712.501,49.503 727.605,46.128 C780.47,34.317 818.839,22.532 856.324,15.904 C922.689,4.169 955.676,2.522 1011.185,0.432 C1060.705,1.477 1097.39,3.129 1121.236,5.387 C1161.703,9.219 1208.621,17.821 1235.4,22.304 C1285.855,30.748 1354.351,47.432 1440.886,72.354 L1441.191,104.352 L1.121,104.031 L0.457,34.035 Z"></path>
                </g>
              </g>
            </svg>
          </div>

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